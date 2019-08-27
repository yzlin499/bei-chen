package top.yzlin.beichen.config;

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import sun.security.krb5.internal.NetClient;
import top.yzlin.beichen.tools.SSLSocketClient;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class HttpConfig {
    private final RedisTemplate<String,String> redisTemplate;

    public HttpConfig(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public Retrofit miaoQiYuRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://www.jx3qy.cn")
                .client(okHttpClient)
                .addConverterFactory(Retrofit2ConverterFactory.create())
                .build();
    }

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient().newBuilder()
//                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),X509Certificate)
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .addInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());
                    List<String> headers = originalResponse.headers("Set-Cookie");
                    if(!headers.isEmpty()){
                        Map<String, String> collect = headers.stream()
                                .flatMap(i -> Arrays.stream(i.split("\\s*;\\s*")))
                                .collect(Collectors.toMap(
                                        k -> k.contains("=")?k.substring(0, k.indexOf("=")):k,
                                        v -> v.contains("=")?v.substring(v.indexOf("=")+1):"",
                                        (o, n) -> n)
                                );
                        redisTemplate.opsForHash().putAll("miaoQiYu.cookie",collect);
                    }
                    return originalResponse;
                }).addInterceptor(chain -> {
                    HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
                    StringBuilder sb=new StringBuilder();
                    hash.entries("miaoQiYu.cookie").forEach((k,v)->{
                        if(sb.length()!=0){
                            sb.append("; ");
                        }
                        sb.append(k);
                        if(!"".equals(v.toString())){
                            sb.append("=").append(v);
                        }
                    });
                    return chain.proceed(chain.request().newBuilder().addHeader("Cookie",sb.toString()).build());
                }).build();
    }


}
