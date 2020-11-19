package com.xjc.netty.service;

/**
 * @Version 1.0
 * @ClassName PushService
 * @Author jiachenXu
 * @Date 2020/11/19
 * @Description
 */
public interface PushService {

    /**
     * 发消息给指定人员
     *
     * @param userId
     * @param msg
     */
    void pushMsgToOne(String userId, String msg);

    /**
     * 发消息给全体人员
     *
     * @param msg
     */
    void pushMsgAll(String msg);

}
