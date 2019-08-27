package top.yzlin.beichen.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.yzlin.cqrobotsdk.CQRobot;
import top.yzlin.cqrobotsdk.HttpAPI;
import top.yzlin.cqrobotsdk.msginterface.EventSolution;

import java.util.Map;

@Configuration
public class CQRobotConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public CQRobot cqRobot(@Value("${cqrobot.url}") String url,@Value("${cqrobot.port}") String port){
        HttpAPI httpApi = new HttpAPI(url,port);
        applicationContext.getBeansOfType(EventSolution.class)
                .forEach((k,v)-> httpApi.addMsgSolution(v));
        return httpApi;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
