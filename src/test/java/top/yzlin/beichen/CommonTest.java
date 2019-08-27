package top.yzlin.beichen;

import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CommonTest {

    @Async
    public void test() {
//        Thread.sleep(5234);
        String j="[\"rresp\",null,null,null,null,null,1]";
        System.out.println(JSONArray.parseArray(j));
    }
}
