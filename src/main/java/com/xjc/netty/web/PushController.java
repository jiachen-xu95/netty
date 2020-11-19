package com.xjc.netty.web;

import com.xjc.netty.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Version 1.0
 * @ClassName PushController
 * @Author jiachenXu
 * @Date 2020/11/19
 * @Description
 */
@RequestMapping("/push")
@RestController
public class PushController {

    @Autowired
    private PushService pushService;

    @RequestMapping("/pushMsgAll")
    public void pushMsgAll(String msg){
        pushService.pushMsgAll(msg);
    }

}
