package com.xjc.netty.config;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Version 1.0
 * @ClassName NettyConfig
 * @Author jiachenXu
 * @Date 2020/11/16
 * @Description
 */
@Component
public class NettyConfig {

    /**
     * 定义Channel组 管理所以的channel
     * GlobalEventExecutor.INSTANCE 全局事件执行器
     */
    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存放用户与Chanel的对应信息，用于给指定用户发送消息
     */
    private ConcurrentHashMap<String, Channel> channelConcurrentHashMap = new ConcurrentHashMap<>();

    private Set<String> userIds = new LinkedHashSet<>();

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public ConcurrentHashMap<String, Channel> getChannelConcurrentHashMap() {
        return channelConcurrentHashMap;
    }

    public Set<String> getUserIds() {
        return userIds;
    }
}
