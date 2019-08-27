package top.yzlin.beichen.keywordfunc.keyfunc;

import top.yzlin.beichen.keywordfunc.KeyWord;
import top.yzlin.beichen.keywordfunc.KeyWordFunction;

public class KeyWordHelp implements KeyWordFunction {
    @Override
    public void setKeyWord(KeyWord keyWord) {

    }

    @Override
    public boolean judgeText(String msg) {
        return "帮助".equals(msg);
    }

    @Override
    public String dealWithText(String fromQQ, String msg) {
        return "管理员具有以下七个指令，所有指令都对机器人私聊进行操作\n" +
                "\n" +
                "添加关键字指令\n" +
                "添加关键字：XXX：YYYY\n" +
                "XXX：群消息出现的关键字\n" +
                "YYY：群消息出现关键字之后，会回复的消息\n" +
                "例如\n" +
                "添加关键字：测试：测试回复\n" +
                "当群里出现“测试”的时候，机器人就会回复“测试回复”\n" +
                "\n" +
                "删除关键字指令\n" +
                "删除关键字：XXX\n" +
                "XXX：要删除的关键字\n" +
                "即可删除关键字\n" +
                "\n" +
                "查看所有关键字指令\n" +
                "查看所有关键字\n" +
                "即可获得所有关键字\n" +
                "\n" +
                "查询关键字指令\n" +
                "查询关键字：XXX\n" +
                "XXX：要查询具体内容的关键字\n" +
                "\n" +
                "添加管理员指令\n" +
                "添加管理员：XXX\n" +
                "XXX：要添加管理员的QQ号\n" +
                "\n" +
                "删除管理员指令\n" +
                "删除管理员：XXX\n" +
                "XXX：要删除的管理员\n" +
                "\n" +
                "帮助文档指令\n" +
                "帮助\n" +
                "返回当前文档\n" +
                "\n" +
                "注意事项\n" +
                "1.所有的关键字为全匹配\n" +
                "2.所有指令必定有回馈，如果没有回馈则说明操作失败\n" +
                "3.添加关键字可作为更新关键字使用，新的关键字会覆盖就得关键字\n" +
                "4.管理员之间是平等的，即，管理员相互之前可以删除权限，比如，甲为管理员，甲通过添加管理员操作将乙提升为管理员，则乙为管理员，并且乙有权删除甲的管理员权限，所以请慎重操作\n" +
                "5.管理员的权限甚至达到，可以删除自己权限，请谨慎操作\n" +
                "6.关键字管理员是与群管理员无关，群管理员并非一定有权限操作关键字，关键字管理员一般也不一定是群管理员";
    }
}
