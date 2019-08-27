package top.yzlin.beichen.keywordfunc.keyfunc;

import top.yzlin.beichen.keywordfunc.KeyWord;
import top.yzlin.beichen.keywordfunc.KeyWordFunction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddKeyWord implements KeyWordFunction {
    private KeyWord keyWord;
    private final static Pattern PATTERN = Pattern.compile("^添加关键字[:|：](?<key>[\u4e00-\u9fa5|\\w]+)[:|：](?<word>[\\S\\s]+)$",
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
            keyWord.addKeyWord(key, m.group("word"));
            return "关键字:" + key + "已添加";
        } else {
            return null;
        }
    }
}
