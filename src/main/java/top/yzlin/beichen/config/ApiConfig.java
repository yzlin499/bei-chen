package top.yzlin.beichen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import top.yzlin.beichen.httpapi.MiaoQiYuApi;
import top.yzlin.beichen.httpapi.MinYiQiYuQuery;
import top.yzlin.beichen.httpapi.WeiBoApi;

@Configuration
public class ApiConfig {

    private Retrofit retrofit;

    @Autowired
    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    @Bean
    public MinYiQiYuQuery qiYuQuery(){
        return retrofit.create(MinYiQiYuQuery.class);
    }

    @Bean
    public MiaoQiYuApi miaoQiYuApi(){
        return retrofit.create(MiaoQiYuApi.class);
    }

    @Bean
    public WeiBoApi weiBoApi(){
        return retrofit.create(WeiBoApi.class);
    }

}
