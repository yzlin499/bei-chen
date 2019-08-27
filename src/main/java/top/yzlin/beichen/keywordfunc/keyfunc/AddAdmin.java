package top.yzlin.beichen.keywordfunc.keyfunc;

import top.yzlin.beichen.keywordfunc.KeyWord;
import top.yzlin.beichen.keywordfunc.KeyWordFunction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddAdmin implements KeyWordFunction {
    private KeyWord keyWord;
    private final static Pattern PATTERN=Pattern.compile("^添加管理员[:|：](?<admin>[0-9]{9,10})");
    @Override
    public void setKeyWord(KeyWord keyWord) {
        this.keyWord=keyWord;
    }

    @Override
    public boolean judgeText(String msg) {
        return PATTERN.matcher(msg).matches();
    }

    @Override
    public String dealWithText(String fromQQ, String msg) {
        Matcher m = PATTERN.matcher(msg);
        if (m.find()) {
            String admin = m.group("admin");
            if (keyWord.containsAdmin(admin)) {
                return "管理员" + msg + "已经存在";
            } else {
                keyWord.addAdmin(admin);
                return "管理员" + msg + "已经添加";
            }
        }else{
            return null;
        }
    }
}
