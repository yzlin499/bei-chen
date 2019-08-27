package top.yzlin.beichen.httpapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.ResponseBody;
import org.springframework.util.ResourceUtils;
import retrofit2.Call;
import retrofit2.http.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public interface MinYiQiYuQuery {

//    @GET("http://www.jx3qy.cn/home/qyinfo")
//    JSONObject getQiYuData(@Query("csrf")String time,@Query("n") String name,);

    @FormUrlEncoded
    @POST("https://recaptcha.net/recaptcha/api2/reload?k=6LdPUIsUAAAAAOy2fTQlOPhFU72hDUkvTuZxQhZY")
    Call<ResponseBody> getParamRstr(@FieldMap Map<String,String> body);

    default String getParamRstr(){
        Map<String,String> param=new HashMap<>();
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(ResourceUtils.getFile("classpath:Rstr.properties")));
            properties.forEach((k,v)->param.put(k.toString(),v.toString()));
            return JSONArray.parseArray(
                    Optional.ofNullable(
                            getParamRstr(param)
                                    .execute()
                                    .body()
                    ).map(v-> {
                        try {
                            return v.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "";
                        }
                    }).orElse("")
                            .substring(4)
            ).getString(1);
        } catch (IOException e) {
            return "";
        }
    }


    @GET("https://jx3.derzh.com/serendipity")
    Call<JSONObject> getQiYuData(@QueryMap Map<String,String> body);

}
