package top.yzlin.beichen.component.weibo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.yzlin.beichen.component.apiquery.WeiBoQuery;
import top.yzlin.beichen.entity.WeiBoInfo;
import top.yzlin.cqrobotsdk.CQRobot;

import java.util.List;
import java.util.Optional;

@Component
public class WeiBoScheduled {
    private boolean todaySend=true;
    private WeiBoQuery weiBoQuery;
    private CQRobot cqRobot;
    private String groupId;

    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Autowired
    public void setCqRobot(CQRobot cqRobot) {
        this.cqRobot = cqRobot;
    }

    @Autowired
    public void setWeiBoQuery(WeiBoQuery weiBoQuery) {
        this.weiBoQuery = weiBoQuery;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanTodaySend(){
        todaySend=true;
    }

    @Scheduled(cron = "0 0/2 10-12 * * ?")
    public void scheduledDaily(){
        if(todaySend){
            List<WeiBoInfo> weiBoData = weiBoQuery.getWeiBoData();
            for (int i = 0; i < 2; i++) {
                WeiBoInfo weiBoInfo = weiBoData.get(i);
                if (weiBoInfo.getText().indexOf("#剑网3江湖百晓生#")==0) {
                    cqRobot.sendGroupMsg(groupId, weiBoInfo.getText());
                    cqRobot.sendGroupMsg(groupId,"[CQ:image,file="+weiBoInfo.getImg()[0]+"]");
                    break;
                }
            }
            todaySend=false;
        }
    }
}
