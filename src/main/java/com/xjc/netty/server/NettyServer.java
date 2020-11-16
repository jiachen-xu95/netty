package com.xjc.netty.server;

import com.xjc.netty.handler.WebSocketHandler;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Version 1.0
 * @ClassName NettyServer
 * @Author jiachenXu
 * @Date 2020/11/16
 * @Description
 */
@Slf4j
@Component
public class NettyServer {

    private final static ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static final String WEBSOCKET_PROTOCOL = "webSocket";

    private final int port = 58080;

    private String webSocketPath = "/webSocket";

    @Autowired
    private WebSocketHandler webSocketHandler;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;





}
