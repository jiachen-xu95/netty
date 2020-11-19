package com.xjc.netty.service;

import com.xjc.netty.config.NettyConfig;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Version 1.0
 * @ClassName PushServiceImpl
 * @Author jiachenXu
 * @Date 2020/11/19
 * @Description
 */
@Service
public class PushServiceImpl implements PushService{

    @Autowired
    private NettyConfig nettyConfig;

    @Override
    public void pushMsgToOne(String userId, String msg) {
        ConcurrentHashMap<String, Channel> channelConcurrentHashMap = nettyConfig.getChannelConcurrentHashMap( );
        Channel channel = channelConcurrentHashMap.get(userId);
        channel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    @Override
    public void pushMsgAll(String msg) {
        nettyConfig.getChannelGroup().writeAndFlush(new TextWebSocketFrame(msg));
    }
}
