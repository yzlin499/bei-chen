package top.yzlin.beichen.component.qiyu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.yzlin.beichen.component.apiquery.MiaoQiYuQuery;
import top.yzlin.cqrobotsdk.CQRobot;

import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

@Component
public class AsyncRelogin {
    private CQRobot cqRobot;
    private String groupId;
    private MiaoQiYuQuery miaoQiYuQuery;
    private final static Pattern PATTERN=Pattern.compile("[0-9a-zA-Z]{4}");

    @Autowired
    public void setMiaoQiYuQuery(MiaoQiYuQuery miaoQiYuQuery) {
        this.miaoQiYuQuery = miaoQiYuQuery;
    }

    @Autowired
    public void setCqRobot(CQRobot cqRobot) {
        this.cqRobot = cqRobot;
    }

    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Async
    void relogin(QiYuRelogin qiYuRelogin, Semaphore semaphore) throws InterruptedException {
        cqRobot.sendGroupMsg(groupId,"[CQ:image,file=base64://"+miaoQiYuQuery.getVerify()+"]");
        semaphore.acquire();
        miaoQiYuQuery.login(qiYuRelogin.getVer());
        cqRobot.sendGroupMsg(groupId,"验证完成，如果无法查询请联系开发者");
        semaphore.release();
    }
}
