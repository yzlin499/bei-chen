package top.yzlin.beichen.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.yzlin.cqrobotsdk.CQRobot;
import top.yzlin.cqrobotsdk.cqinfo.GroupMsgInfo;
import top.yzlin.cqrobotsdk.msginterface.GroupMsgSolution;

@Component
@ConfigurationProperties(prefix = "cqrobot.reread-machine")
public class RereadMachine implements GroupMsgSolution {
    private String groupId;
    private String lastMsg = "";
    private int count = 1;
    private int threshold;
    private CQRobot cqRobot;

    @Autowired
    @Lazy
    public void setCqRobot(CQRobot cqRobot) {
        this.cqRobot = cqRobot;
    }

    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }


    @Override
    public void msgSolution(GroupMsgInfo groupMsgInfo) {
        if (groupId.equals(groupMsgInfo.getFromGroup())) {
            String msg = groupMsgInfo.getMsg();
            if (msg.equals(lastMsg)) {
                count++;
                if (count == threshold) {
                    cqRobot.sendGroupMsg(groupId, lastMsg);
                }
            } else {
                lastMsg = msg;
                count = 1;
            }
        }
    }
}
