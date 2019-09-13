package top.yzlin.beichen.subscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.yzlin.cqrobotsdk.cqinfo.GroupMsgInfo;
import top.yzlin.cqrobotsdk.msginterface.reply.GroupMsgReply;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UnsubscribeMsg implements GroupMsgReply {
    private String groupId;
    private RedisTemplate<String, String> redisTemplate;
    private Pattern pattern = Pattern.compile("^取消订阅(?<msg>[\\u4e00-\\u9fa5|a-zA-Z0-9]+)");

    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
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
            Long aLong = redisTemplate.opsForSet().remove("cqrobot.subscribe." + groupId + "." + msg,
                    groupMsgInfo.getFromQQ());
            if (aLong != null && aLong > 0) {
                return "取消订阅成功";
            }
        }
        return "取消订阅失败";
    }
}
