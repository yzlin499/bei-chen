package top.yzlin.beichen;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WSTest {

    @Test
    public void test() throws InterruptedException {
        Pattern pattern = Pattern.compile("^推送(?<msg>[\u4e00-\u9fa5|a-zA-Z0-9]+)(| (?<describe>.+))$");
        Matcher matcher = pattern.matcher("推送test testMsg");
        System.out.println(matcher.matches());
//        if (matcher.find()) {
//            System.out.println("|"+matcher.group("msg")+"---"+matcher.group("describe")+"|");
//        }
    }
}
