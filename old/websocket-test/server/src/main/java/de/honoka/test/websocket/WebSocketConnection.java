package de.honoka.test.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Objects;

@ServerEndpoint("/websocket")
public class WebSocketConnection {

    private static WebSocketServer webSocketServer;

    private Session session;

    private JSONObject data;

    public WebSocketConnection() {}

    public WebSocketConnection(WebSocketServer server) {
        webSocketServer = server;
    }

    @SneakyThrows
    public void sendMessage(WebSocketMessage message) {
        this.session.getBasicRemote().sendText(JSON.toJSONString(message));
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String messageStr) {
        WebSocketMessage message = JSON.parseObject(messageStr,
                WebSocketMessage.class);
        switch(message.getType()) {
            case "login":
                sendMessage(onLogin(message));
                break;
            case "query_online":
                sendMessage(queryOnline(message));
                break;
            case "group_message":
                onGroupMessage(message);
                break;
        }
    }

    @OnClose
    public void onClose() {
        webSocketServer.getConnections().remove(this);
        for(WebSocketConnection connection : webSocketServer.getConnections()) {
            connection.sendMessage(new WebSocketMessage(null)
                    .setType("user_logout")
                    .setData(data));
        }
    }

    private WebSocketMessage onLogin(WebSocketMessage message) {
        WebSocketMessage res = new WebSocketMessage(message.getId());
        res.setType("login");
        JSONObject data = message.getData();
        Long userId = data.getLong("accountId");
        String username = data.getString("name");
        if(ObjectUtils.anyNull(userId, username)) {
            res.getData().fluentPut("status", false)
                    .fluentPut("message", "账号和用户名不能为空");
            return res;
        }
        for(WebSocketConnection connection : webSocketServer.getConnections()) {
            if(connection.data.getLong("accountId").longValue() ==
                    userId.longValue()) {
                res.getData().fluentPut("status", false)
                        .fluentPut("message", "该账号已登录");
                return res;
            }
            if(Objects.equals(connection.data.getString("name"), username)) {
                res.getData().fluentPut("status", false)
                        .fluentPut("message", "该用户名已存在");
                return res;
            }
            if(StringUtils.equalsIgnoreCase(connection.data.getString(
                    "name"), "robot")) {
                res.getData().fluentPut("status", false)
                        .fluentPut("message", "不能使用Robot作为用户名");
                return res;
            }
        }
        this.data = (JSONObject) data.clone();
        webSocketServer.getConnections().add(this);
        webSocketServer.getExecutor().execute(() -> {
            for(WebSocketConnection connection : webSocketServer.getConnections()) {
                if(connection.data.getString("name").equals(username))
                    continue;
                connection.sendMessage(new WebSocketMessage(null)
                        .setType("new_user_login")
                        .setData(new JSONObject()
                                .fluentPut("name", username)
                        )
                );
            }
        });
        res.getData().fluentPut("status", true);
        return res;
    }

    private WebSocketMessage queryOnline(WebSocketMessage message) {
        WebSocketMessage res = new WebSocketMessage(message.getId());
        JSONArray messageData = new JSONArray();
        for(WebSocketConnection connection : webSocketServer.getConnections()) {
            messageData.add(connection.data.getString("name"));
        }
        res.setData(new JSONObject().fluentPut("online", messageData));
        return res;
    }

    private void onGroupMessage(WebSocketMessage message) {
        sendMessage(new WebSocketMessage(message.getId())
                .setType("group_message_response")
                .setData(new JSONObject().fluentPut("status", true))
        );
        webSocketServer.getExecutor().execute(() -> {
            for(WebSocketConnection connection : webSocketServer.getConnections()) {
                if(connection.data.getString("name").equals(
                        this.data.getString("name")))
                    continue;
                connection.sendMessage(new WebSocketMessage(null)
                        .setType("group_message")
                        .setData(new JSONObject()
                                .fluentPut("name", this.data.getString("name"))
                                .fluentPut("content", message.getData()
                                        .getJSONArray("content"))
                        )
                );
            }
        });
    }
}
