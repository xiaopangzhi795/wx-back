package com.geek45.wxback.controller;

import com.alibaba.fastjson.JSONObject;
import com.geek45.wxback.common.*;
import com.geek45.wxback.service.MessageService;
import com.geek45.wxback.vo.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 校验接口
 *
 * @author rubik
 * @version VerificationController of 钱志磊  0.1v created 2020/11/9 20:53
 **/
@RestController
@RequestMapping("verification")
@Slf4j
public class VerificationController {

    private static final String TOKEN = "geek45";

    @Autowired
    private MessageService messageService;

    @PostMapping("check/signature")
    public String sendMessage(HttpServletRequest request) throws IOException, DocumentException, InstantiationException, IllegalAccessException {
        String xml = MessageUtil.getXml(request.getInputStream());
        TextMessage textMessage = MessageUtil.xml2Vo(xml, TextMessage.class);
        log.info("message is :{}", JSONObject.toJSONString(textMessage));
        String message = messageService.getMessage(textMessage);
        log.info("result is :{}", message);
        return message;
    }

    @GetMapping("check/signature")
    public String checkSignature(HttpServletRequest request) throws AesException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echoStr = request.getParameter("echostr");
        log.info("param is :{},{},{},{}", signature, timestamp, nonce, echoStr);
//        String[] array = new String[]{timestamp, nonce, echoStr};
//        Arrays.sort(array);
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < array.length; i++) {
//            builder.append(array[i]);
//        }
//        log.info("builder is :{}", builder.toString());
        String enStr = SHA1Util.getSHA1(TOKEN, timestamp, nonce);
        log.info("enStr is {}", enStr);
        if (StringUtils.isNotBlank(enStr) && StringUtils.equals(signature, enStr)) {
            return echoStr;
        }
        return null;
    }

    @PostConstruct
    public void removeCacheTask() {
        new Thread(() -> {
            while (true) {
                try {
                    CacheUtil.getInstance().remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        Thread.sleep(Constant.TIME_OUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
