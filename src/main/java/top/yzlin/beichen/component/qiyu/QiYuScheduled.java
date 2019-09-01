package top.yzlin.beichen.component.qiyu;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.yzlin.beichen.component.apiquery.MiaoQiYuQuery;
import top.yzlin.beichen.entity.QiYu;
import top.yzlin.cqrobotsdk.CQRobot;
import top.yzlin.cqrobotsdk.cqinfo.PersonMsgInfo;
import top.yzlin.cqrobotsdk.msginterface.reply.PersonMsgReply;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class QiYuScheduled implements InitializingBean, PersonMsgReply {
    private RedisTemplate<String, String> redisTemplate;
    private MiaoQiYuQuery miaoQiYuQuery;
    private String banQiYuSet;
    private String keyWordAdminSet;
    private QiYu lastQiYu;
    private CQRobot cqRobot;
    private String groupId;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        lastQiYu=new QiYu();
        lastQiYu.setTime(System.currentTimeMillis()/1000);
        banQiYuSet = "cqrobot.banQiYuSet." + groupId;
        keyWordAdminSet = "cqrobot.keyword." + groupId + ".keyWordAdminSet";
    }

    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Autowired
    @Lazy
    public void setCqRobot(CQRobot cqRobot) {
        this.cqRobot = cqRobot;
    }

    @Autowired
    public void setMiaoQiYuQuery(MiaoQiYuQuery miaoQiYuQuery) {
        this.miaoQiYuQuery = miaoQiYuQuery;
    }

    @Scheduled(fixedRate = 1000*60)
    public void monitoringQiYu(){
        List<QiYu> qiYuData = miaoQiYuQuery.getQiYuData("", "");
        if(qiYuData!=null){
            List<QiYu> collect = qiYuData.stream()
                    .filter(q -> q.getTime() > lastQiYu.getTime())
                    .filter(q -> !Optional.ofNullable(redisTemplate.opsForSet()
                            .isMember(banQiYuSet, q.getSerendipity())).orElse(true))
                    .sorted(Comparator.comparingLong(QiYu::getTime).reversed())
                    .collect(Collectors.toList());
            if (!collect.isEmpty()) {
                lastQiYu=collect.get(0);
                StringBuilder sb=new StringBuilder("这里有个人触发了奇遇:");
                collect.forEach(qiYu -> sb.append("\n").append(qiYu.getUserName()).append(" 触发了 ").append(qiYu.getSerendipity()));
                cqRobot.sendGroupMsg(groupId,sb.toString());
            }
        }
    }

    @Override
    public boolean fromQQ(String from) {
        return Optional.ofNullable(redisTemplate.opsForSet().isMember(keyWordAdminSet, from)).orElse(false);
    }

    @Override
    public boolean checkMsg(String from) {
        return from.indexOf("屏蔽奇遇") == 0;
    }

    @Override
    public String replyMsg(PersonMsgInfo personMsgInfo) {
        String substring = personMsgInfo.getMsg().substring(4);
        redisTemplate.opsForSet().add(banQiYuSet, substring);
        return substring + "已经屏蔽";
    }
}
