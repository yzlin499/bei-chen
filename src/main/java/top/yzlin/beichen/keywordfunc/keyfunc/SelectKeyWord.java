package top.yzlin.beichen.keywordfunc.keyfunc;

import top.yzlin.beichen.keywordfunc.KeyWord;
import top.yzlin.beichen.keywordfunc.KeyWordFunction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectKeyWord implements KeyWordFunction {
    private KeyWord keyWord;
    private final static Pattern PATTERN = Pattern.compile("^查询关键字[:|：](?<key>[\u4e00-\u9fa5|\\w]+)",
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
            if (!keyWord.containsKeyWord(key)) {
                return "不存在该关键字";
            } else {
                return "该关键字的回复消息：" + keyWord.getKeyWord(key);
            }
        } else {
            return null;
        }
    }
}
