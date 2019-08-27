package top.yzlin.beichen.component.qiyu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.yzlin.beichen.component.apiquery.MiaoQiYuQuery;
import top.yzlin.beichen.entity.QiYu;
import top.yzlin.cqrobotsdk.cqinfo.GroupMsgInfo;
import top.yzlin.cqrobotsdk.msginterface.reply.GroupMsgReply;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class QueryQiYuByUserName implements GroupMsgReply {
    private String groupId;
    private static final Pattern PATTERN=Pattern.compile("^查询(?<name>[\\u4e00-\\u9fa5@]{2,12})的奇遇$");
    private MiaoQiYuQuery miaoQiYuQuery;
    private static final SimpleDateFormat sdf=new SimpleDateFormat("yy年M月d日H:mm");



    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Autowired
    public void setMiaoQiYuQuery(MiaoQiYuQuery miaoQiYuQuery) {
        this.miaoQiYuQuery = miaoQiYuQuery;
    }

    @Override
    public boolean fromGroup(String from) {
        return groupId.equals(from);
    }

    @Override
    public boolean checkMsg(String from) {
        return PATTERN.matcher(from).matches();
    }

    @Override
    public String replyMsg(GroupMsgInfo groupMsgInfo) {
        Matcher matcher = PATTERN.matcher(groupMsgInfo.getMsg());
        if(matcher.find()){
            List<QiYu> qiYuList = miaoQiYuQuery.getQiYuDataByUserName(matcher.group("name"));
            if(qiYuList==null){
                return "登录验证";
            }else if(qiYuList.size()==0){
                return "这个黑鬼没有奇遇哦！QAQ";
            }else{
                StringBuilder stringBuilder=new StringBuilder().append("这位大哥有这些奇遇");
                qiYuList.forEach(i-> stringBuilder
                        .append("\n").append(i.getSerendipity())
                        .append("于").append(sdf.format(new Date(i.getTime()*1000))).append("触发"));
                return stringBuilder.toString();
            }
        }
        return "无法解析的名字";
    }
}
