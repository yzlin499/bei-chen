package top.yzlin.beichen.httpapi;

import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import top.yzlin.beichen.entity.WeiBoInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface WeiBoApi {

    @GET("https://m.weibo.cn/api/container/getIndex")
    Call<JSONObject> getWeiBoData(@Query("containerid")String uid);

}
