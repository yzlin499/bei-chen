package top.yzlin.beichen.subscribe;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.yzlin.cqrobotsdk.CQRobot;
import top.yzlin.cqrobotsdk.cqinfo.GroupMsgInfo;
import top.yzlin.cqrobotsdk.msginterface.reply.GroupMsgReply;

import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PublishMsg implements GroupMsgReply, InitializingBean {
    private String keyWordAdminSet;
    private RedisTemplate<String, String> redisTemplate;
    private String groupId;
    private Pattern pattern = Pattern.compile("^推送(?<msg>[\u4e00-\u9fa5|a-zA-Z0-9]+)(| (?<describe>.+))$");
    private CQRobot cqRobot;

    @Override
    public void afterPropertiesSet() {
        this.keyWordAdminSet = "cqrobot.keyword." + groupId + ".keyWordAdminSet";
    }

    @Autowired
    @Lazy
    public void setCqRobot(CQRobot cqRobot) {
        this.cqRobot = cqRobot;
    }

    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean fromQQ(String from) {
        return Optional.ofNullable(redisTemplate.opsForSet().isMember(keyWordAdminSet, from)).orElse(false);
    }

    @Override
    public boolean fromGroup(String from) {
        return groupId.equals(from);
    }

    @Override
    public boolean checkMsg(String from) {
        return pattern.matcher(from).matches();
    }

    @Override
    public String replyMsg(GroupMsgInfo groupMsgInfo) {
        Matcher matcher = pattern.matcher(groupMsgInfo.getMsg());
        if (matcher.find()) {
            String msg = matcher.group("msg");
            String redisKey = "cqrobot.subscribe." + groupId + "." + msg;
            String describe = matcher.group("describe");
            StringBuilder publishMsg = new StringBuilder();
            publishMsg.append("接收到订阅:").append(msg);
            if (describe != null) {
                publishMsg.append("\n附言:").append(describe);
            }
            String publishMsgStr = publishMsg.toString();
            for (String member : Optional.ofNullable(redisTemplate.opsForSet().members(redisKey))
                    .orElse(Collections.emptySet())) {
                cqRobot.sendPersonMsg(member, publishMsgStr);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "推送完成";
        } else {
            return "推送失败";
        }
    }
}
