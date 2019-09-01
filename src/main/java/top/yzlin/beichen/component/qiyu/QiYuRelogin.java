package top.yzlin.beichen.component.qiyu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.yzlin.cqrobotsdk.cqinfo.GroupMsgInfo;
import top.yzlin.cqrobotsdk.msginterface.GroupMsgSolution;

import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

@Component
public class QiYuRelogin implements GroupMsgSolution {
    private String ver="";
    private AsyncRelogin asyncRelogin;
    private String groupId;
    private final static Pattern PATTERN=Pattern.compile("[0-9a-zA-Z]{4}");
    private Semaphore semaphore = new Semaphore(1);

    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Autowired
    @Lazy
    public void setAsyncRelogin(AsyncRelogin asyncRelogin) {
        this.asyncRelogin = asyncRelogin;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getVer() {
        return ver;
    }

    public void relogin(){
        if (ver != null) {
            ver = null;
            try {
                semaphore.acquire();
                asyncRelogin.relogin(this, semaphore);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void msgSolution(GroupMsgInfo groupMsgInfo) {
        if(ver==null && groupId.equals(groupMsgInfo.getFromGroup()) &&
                PATTERN.matcher(groupMsgInfo.getMsg()).matches()){
            ver=groupMsgInfo.getMsg();
            semaphore.release();
        }
    }
}
