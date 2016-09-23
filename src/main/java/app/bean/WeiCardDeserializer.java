package app.bean;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class WeiCardDeserializer  extends StdDeserializer<WeiCard> {

    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.US);

    protected WeiCardDeserializer(Class<?> vc) {
        super(vc);
    }
    public WeiCardDeserializer() {
        this(null);
    }

    @Override
    public WeiCard deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode card = node.path("cards").get(5);
        JsonNode group = card.path("card_group");
        List<WeiSheep> list = new ArrayList<>();
        for(JsonNode jsonNode : group ){
            JsonNode blog =jsonNode.path("mblog");
            String create = blog.path("created_at").asText();
            String text = blog.path("text").asText();
            //System.out.println("create->" + create +" text =>" +text);
            WeiSheep weiBo = new WeiSheep();
            weiBo.setText(text.substring(0,3));
            try {
                weiBo.setDate(sdf.parse(create));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list.add(weiBo);
        }
        WeiCard weiCard = new WeiCard();
        weiCard.setList(list);
        //weiCard.setTitle(title);
        return weiCard;
    }
}
