package top.yzlin.beichen.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class QiYu {
    private String server;
    @JSONField(name="name")
    private String userName;
    private String serendipity;
    private long time;
}
