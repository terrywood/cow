package app.bean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2016/8/30 0030.
 */
@Data
@JsonDeserialize(using = WeiCardDeserializer.class)
public class WeiCard {
    String  title;
    List<WeiBo> list;
}
