package top.yzlin.beichen.component.weibo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.yzlin.beichen.component.apiquery.WeiBoQuery;
import top.yzlin.beichen.entity.WeiBoInfo;
import top.yzlin.beichen.httpapi.WeiBoApi;
import top.yzlin.cqrobotsdk.cqinfo.GroupMsgInfo;
import top.yzlin.cqrobotsdk.msginterface.reply.GroupMsgReply;

import java.util.Optional;


@Component
public class QueryDaily implements GroupMsgReply {
    private String groupId;
    private static final String KED_WORD="查询日常";
    private WeiBoQuery weiBoQuery;

    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Autowired
    public void setWeiBoQuery(WeiBoQuery weiBoQuery) {
        this.weiBoQuery = weiBoQuery;
    }

    @Override
    public boolean fromGroup(String from) {
        return groupId.equals(from);
    }

    @Override
    public boolean checkMsg(String from) {
        return KED_WORD.equals(from);
    }

    @Override
    public String replyMsg(GroupMsgInfo groupMsgInfo) {
        for (WeiBoInfo weiBoDatum : weiBoQuery.getWeiBoData()) {
            if (weiBoDatum.getText().indexOf("#剑网3江湖百晓生#")==0) {
                return "[CQ:image,file="+weiBoDatum.getImg()[0]+"]";
            }
        }
        return "查询失败了QAQ";
    }
}
