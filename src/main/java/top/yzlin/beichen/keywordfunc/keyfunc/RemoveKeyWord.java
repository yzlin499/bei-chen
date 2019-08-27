package top.yzlin.beichen.keywordfunc.keyfunc;

import top.yzlin.beichen.keywordfunc.KeyWord;
import top.yzlin.beichen.keywordfunc.KeyWordFunction;
import top.yzlin.tools.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveKeyWord implements KeyWordFunction {
    private KeyWord keyWord;
    private final static Pattern PATTERN = Pattern.compile("^删除关键字[:|：](?<key>[\u4e00-\u9fa5|\\w]+)$",
            Pattern.DOTALL);


    @Override
    public void setKeyWord(KeyWord keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public boolean judgeText(String msg) {
        return PATTERN.matcher(msg).matches();
    }

    @Override
    public String dealWithText(String fromQQ, String msg) {
        Matcher m = PATTERN.matcher(msg);
        if (m.find()) {
            String key = m.group("key");
            if (keyWord.containsKeyWord(key)) {
                keyWord.removeKeyWord(key);
                return "关键字:" + msg + "已删除";
            } else {
                return "不存在关键字:" + msg;
            }
        } else {
            return null;
        }
    }
}
