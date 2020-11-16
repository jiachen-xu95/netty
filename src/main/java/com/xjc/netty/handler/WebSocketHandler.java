package com.xjc.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xjc.netty.config.NettyConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Version 1.0
 * @ClassName WebSocketHandler
 * @Author jiachenXu
 * @Date 2020/11/16
 * @Description
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private NettyConfig nettyConfig;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("服务器收到消息：{}", ctx.channel( ).id( ).asLongText( ));
        nettyConfig.getChannelGroup( ).add(ctx.channel( ));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("服务器收到消息：{}", msg.text( ));
        JSONObject parse = JSON.parseObject(msg.text( ));
        String uid = (String) parse.get("uid");
        // 关联用户id 通道
        Set<String> userIds = nettyConfig.getUserIds( );
        userIds.add(uid);

        nettyConfig.getChannelConcurrentHashMap( ).putIfAbsent(uid, ctx.channel( ));
        //将用户id作为自定义数据加入channel中，方便随时channel获取用户id
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        ctx.channel().attr(key).setIfAbsent(uid);

        //回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器连接成功"));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        removeChannelGroup(ctx);
        removeUserId(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        removeChannelGroup(ctx);
        removeUserId(ctx);
    }

    private void removeChannelGroup(ChannelHandlerContext ctx){
        nettyConfig.getChannelGroup().remove(ctx.channel());
    }

    private void removeUserId(ChannelHandlerContext ctx){
        AttributeKey<String> userId = AttributeKey.valueOf("userId");
        String id = ctx.channel().attr(userId).get();
        nettyConfig.getChannelConcurrentHashMap().remove(id);
    }
}
