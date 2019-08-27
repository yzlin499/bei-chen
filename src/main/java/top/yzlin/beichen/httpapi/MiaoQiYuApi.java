package top.yzlin.beichen.httpapi;

import com.alibaba.fastjson.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface MiaoQiYuApi {

    String SUCCESS="200";
    String FALSE="401.4";

    @GET("/home/qyinfo?m=1")
    Call<JSONObject> getQiYuData(@Query("R")String district,
                                 @Query("S")String server,
                                 @Query("n")String userName,
                                 @Query("t")String qiYuKind,
                                 @Query("u")String serendipity,
                                 @Query("csrf")long time);

    @GET("/user/login/verify.html")
    Call<ResponseBody> getVerify();

    @FormUrlEncoded
    @POST("/user/login/index.html")
    Call<ResponseBody> login(@Field("username") String username,
               @Field("password") String password,
               @Field("vercode") String vercode);

}
