package top.yzlin.beichen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.yzlin.beichen.component.RereadMachine;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeiChenApplicationTests {


    @Autowired
    RereadMachine rereadMachine;



    @Test
    public void miaomiQiyu(){
    }
}
