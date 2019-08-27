package top.yzlin.beichen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit4.SpringRunner;
import top.yzlin.beichen.component.apiquery.MiaoQiYuQuery;
import top.yzlin.beichen.component.qiyu.QiYuRelogin;
import top.yzlin.beichen.keywordfunc.KeyWord;
import top.yzlin.cqrobotsdk.CQRobot;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeiChenApplicationTests {


    @Autowired
    KeyWord keyWord;



    @Test
    public void miaomiQiyu(){
        keyWord.addAdmin("499680328");
    }
}
