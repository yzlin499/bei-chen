package top.yzlin.beichen.component.apiquery;

import com.alibaba.fastjson.JSONObject;
import io.reactivex.functions.BiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.yzlin.beichen.component.qiyu.QiYuRelogin;
import top.yzlin.beichen.entity.QiYu;
import top.yzlin.beichen.httpapi.MiaoQiYuApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

import static top.yzlin.beichen.httpapi.MiaoQiYuApi.FALSE;
import static top.yzlin.beichen.httpapi.MiaoQiYuApi.SUCCESS;

@Component
@ConfigurationProperties(prefix = "cqrobot.miao-qi-yu")
public class MiaoQiYuQuery{
    private MiaoQiYuApi miaoQiYuApi;
    private String username;
    private String password;
    private QiYuRelogin qiYuRelogin;

    public MiaoQiYuQuery(MiaoQiYuApi miaoQiYuApi) {
        this.miaoQiYuApi = miaoQiYuApi;
    }

    @Autowired
    public void setQiYuRelogin(QiYuRelogin qiYuRelogin) {
        this.qiYuRelogin = qiYuRelogin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerify() {
        try {
            InputStream inputStream = miaoQiYuApi.getVerify().execute().body().byteStream();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            byte[] buff=new byte[1024];
            while((inputStream.read(buff))!=-1){
                baos.write(buff);
            }
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void login(String vercode){
        try {
            miaoQiYuApi.login(username,password,vercode).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<QiYu> getQiYuDataByUserName(String userName){
        return getQiYuData(userName,"");
    }

    public List<QiYu> getQiYuDataBySerendipity(String serendipity){
        return getQiYuData("",serendipity);
    }

    public List<QiYu> getQiYuData(String userName, String serendipity) {
        try {
            JSONObject body = miaoQiYuApi.getQiYuData("电信五区", "梦江南", userName, "绝世奇遇" ,
                    serendipity, System.currentTimeMillis()).execute().body();
            if (body!=null && SUCCESS.equals(body.getString("code"))) {
                return body.getJSONArray("result").toJavaList(QiYu.class);
            }else if(body!=null && FALSE.equals(body.getString("code"))){
                qiYuRelogin.relogin();
                return null;
            }else{
                System.out.println(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
