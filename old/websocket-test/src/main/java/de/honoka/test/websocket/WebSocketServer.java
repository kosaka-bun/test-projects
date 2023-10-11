package de.honoka.test.websocket;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Getter
@Component
public class WebSocketServer {

    private final List<WebSocketConnection> connections =
            Collections.synchronizedList(new LinkedList<>());

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor)
            Executors.newCachedThreadPool();
}
