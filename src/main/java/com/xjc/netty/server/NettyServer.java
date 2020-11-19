package com.xjc.netty.server;

import com.xjc.netty.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
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


    @PostConstruct
    public void init(){
        // 初始化netty server资源
        executorService.execute(() -> {
            try {
                start();
            } catch (InterruptedException e) {
                e.printStackTrace( );
            }
        });
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        // 进行资源释放
        if(bossGroup != null){
            bossGroup.shutdownGracefully().sync();
        }
        if(workGroup != null){
            workGroup.shutdownGracefully().sync();
        }
    }

    private void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.localAddress(new InetSocketAddress(port));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>( ) {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                        .addLast(new HttpServerCodec(), new ObjectEncoder(), new ChunkedWriteHandler())
                        // http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合，浏览器在发送大量数据时就会发送多次请求
                        .addLast(new HttpObjectAggregator(8192))
                        // 对应webSocket，数据将会以帧的形式，请求时将http协议升级为webSocket协议 保持长连接
                        .addLast(new WebSocketServerProtocolHandler(webSocketPath, WEBSOCKET_PROTOCOL, true, 65536 * 10))
                        // 自定义handler
                        .addLast(webSocketHandler);
            }
        });
        // 配置完成 进行绑定server 异步的方式绑定直到成功
        ChannelFuture channelFuture = bootstrap.bind( ).sync( );
        log.info("server started and listen on:{}", channelFuture.channel().localAddress());
        // 对关闭通道监听
        channelFuture.channel().closeFuture().sync();
    }

}
