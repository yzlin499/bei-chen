package top.yzlin.beichen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.yzlin.beichen.subscribe.PublishMsg;
import top.yzlin.cqrobotsdk.cqinfo.GroupMsgInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeiChenApplicationTests {


    @Autowired
    PublishMsg publishMsg;



    @Test
    public void miaomiQiyu(){
        GroupMsgInfo groupMsgInfo = new GroupMsgInfo();
        groupMsgInfo.setMsg("推送test testMsg");
        groupMsgInfo.setFromQQ("499680328");
        groupMsgInfo.setFromGroup("650201758");

        System.out.println(publishMsg.filter(groupMsgInfo));

    }
}
