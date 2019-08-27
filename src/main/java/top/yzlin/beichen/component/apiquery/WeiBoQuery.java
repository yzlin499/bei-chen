package top.yzlin.beichen.component.apiquery;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import top.yzlin.beichen.entity.WeiBoInfo;
import top.yzlin.beichen.httpapi.WeiBoApi;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WeiBoQuery {
    private String paramUid;
    private String uid;
    private WeiBoApi weiBoQuery;

    public WeiBoQuery(WeiBoApi weiBoQuery, @Value("${cqrobot.weibo.uid}") String uid) {
        this.weiBoQuery=weiBoQuery;
        this.uid = uid;
        paramUid="107603"+uid;
    }


    private List<WeiBoInfo> parsingData() throws IOException {
        JSONObject jo = weiBoQuery.getWeiBoData(paramUid).execute().body();
        if (jo.getIntValue("ok") == 1) {
            return jo.getJSONObject("data").getJSONArray("cards").toJavaList(JSONObject.class).stream()
                    .filter(j -> j.getIntValue("card_type") == 9 && !j.getJSONObject("mblog").containsKey("title"))
                    .map(j -> {
                        WeiBoInfo weiBoInfo = new WeiBoInfo();
                        String url = j.getString("scheme");
                        int n = url.indexOf("/status/");
                        weiBoInfo.setUrl("https://weibo.com/" + uid + '/' + url.substring(n + 8, url.indexOf('?', n)));
                        JSONObject mblog = j.getJSONObject("mblog");
                        weiBoInfo.setId(mblog.getLongValue("id"));
                        if (mblog.containsKey("retweeted_status")) {
                            weiBoInfo.setText(mblog.getString("raw_text"));
                            weiBoInfo.setRepost(true);
                            weiBoInfo.setImg(WeiBoInfo.EMPTY_IMG);
                            weiBoInfo.setRepostText(Jsoup.parse(mblog.getJSONObject("retweeted_status").getString("text")).text());
                        } else {
                            weiBoInfo.setText(Jsoup.parse(mblog.getString("text")).text());
                            weiBoInfo.setRepost(false);
                            weiBoInfo.setImg(Optional.ofNullable(mblog.getJSONArray("pics"))
                                    .map(a -> a.toJavaList(JSONObject.class).stream()
                                            .map(i -> i.getString("url"))
                                            .toArray(String[]::new))
                                    .orElse(WeiBoInfo.EMPTY_IMG));
                        }
                        return weiBoInfo;
                    })
                    .sorted(Comparator.comparingLong(WeiBoInfo::getId).reversed())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }


    public List<WeiBoInfo> getWeiBoData() {
        try {
            return parsingData();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}

