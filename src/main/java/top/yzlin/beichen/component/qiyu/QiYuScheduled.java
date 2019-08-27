package top.yzlin.beichen.component.qiyu;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.yzlin.beichen.component.apiquery.MiaoQiYuQuery;
import top.yzlin.beichen.entity.QiYu;
import top.yzlin.cqrobotsdk.CQRobot;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QiYuScheduled implements InitializingBean {
//    private RedisTemplate<String,String> redisTemplate;
//    @Autowired
//    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
    private MiaoQiYuQuery miaoQiYuQuery;
    private QiYu lastQiYu;
    private CQRobot cqRobot;
    private String groupId;


    @Override
    public void afterPropertiesSet() throws Exception {
        lastQiYu=new QiYu();
        lastQiYu.setTime(System.currentTimeMillis()/1000);
    }

    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Autowired
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
            List<QiYu> collect = qiYuData.stream().filter(q -> q.getTime() > lastQiYu.getTime())
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
}
