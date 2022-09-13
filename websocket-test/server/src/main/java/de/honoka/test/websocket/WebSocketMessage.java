package de.honoka.test.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebSocketMessage {

    private String id;

    private String type;

    private JSONObject data = new JSONObject();

    public WebSocketMessage(String id) {
        this.id = id;
    }
}
