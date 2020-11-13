package com.geek45.wxback.service.impl;

import com.geek45.wxback.common.*;
import com.geek45.wxback.service.MessageService;
import com.geek45.wxback.vo.TextMessage;
import com.geek45.wxback.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private static Map<String, String> EVENT_MAP = new HashMap<>();
    private static Map<String, User> messageCache = new HashMap<>();
    static {
        EVENT_MAP.put("1", EventEnum.CHAT.getCode());
        EVENT_MAP.put("2", EventEnum.TRANSLATE.getCode());
        EVENT_MAP.put("3", EventEnum.GET_RESOURCE.getCode());
    }

    @Override
    public String getMessage(TextMessage textMessage) {
        try {
            TextMessage reportMessage = new TextMessage();
            User user = messageCache.get(textMessage.getFromUserName());
            if (null == user) {
                user = new User();
                user.setUserName(textMessage.getFromUserName());
                user.setOpTime(textMessage.getCreateTime());
                messageCache.put(user.getUserName(), user);
            }
            reportMessage.setContent(checkStatus(textMessage.getContent(), user));
            reportMessage.setToUserName(textMessage.getFromUserName());
            reportMessage.setFromUserName(textMessage.getToUserName());
            reportMessage.setCreateTime(String.valueOf(new Date().getTime()));
            reportMessage.setMsgType("text");
            return MessageUtil.vo2Xml(reportMessage, "xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测状态
     * @param message
     * @param user
     * @return
     */
    private String checkStatus(String message, User user) {
        checkTimeOut(user);
        switch (BizStatusEnum.valueOfCode(user.getStatus())) {
            case EVENTING:
                return eventingMessage(user, message);
            case NON_STATUS:
                return nonMessage(user,message);
            case EXIT_CONFIRM:
                return exitConfirmMessage(user, message);
            case EVENT_CONFIRM:
                return eventConfirmMessage(user, message);
            default:
                user.setStatus(BizStatusEnum.NON_STATUS.getCode());
                return Constant.MessageCodeConstant.NON_MESSAGE;
        }
    }

    /**
     * 事件确认消息
     * @param user
     * @param message
     * @return
     */
    private String eventConfirmMessage(User user, String message) {
        user.setLastOpTime(nowTime());
        if (!Arrays.stream(Constant.KeyWorldConstant.YES).collect(Collectors.toList()).contains(message.toUpperCase())) {
            user.setStatus(BizStatusEnum.NON_STATUS.getCode());
            return Constant.MessageCodeConstant.NON_MESSAGE;
        }
        user.setStatus(BizStatusEnum.EVENTING.getCode());
        return MessageFormat.format(Constant.MessageCodeConstant.EVENTING_MESSAEG, EventEnum.valueOfCode(user.getEvent()).getDescription(), Constant.TIME_OUT_MIN);
    }

    /**
     * 退出确认消息
     * @param user
     * @param message
     * @return
     */
    private String exitConfirmMessage(User user, String message) {
        user.setLastOpTime(nowTime());
        if (!Arrays.stream(Constant.KeyWorldConstant.YES).collect(Collectors.toList()).contains(message.toUpperCase())) {
            return Constant.MessageCodeConstant.CONTINUE_MESSAGE;
        }
        user.setStatus(BizStatusEnum.NON_STATUS.getCode());
        user.setEvent(null);
        return Constant.MessageCodeConstant.NON_MESSAGE;
    }

    /**
     * 无状态消息
     * @param user
     * @param message
     * @return
     */
    private String nonMessage(User user, String message) {
        user.setLastOpTime(nowTime());
        if (Arrays.stream(Constant.KeyWorldConstant.HELP).collect(Collectors.toList()).contains(message.toUpperCase())) {
            return Constant.MessageCodeConstant.NON_MESSAGE;
        }
        if (EVENT_MAP.containsKey(message)) {
            String event = EVENT_MAP.get(message);
            user.setStatus(BizStatusEnum.EVENT_CONFIRM.getCode());
            user.setEvent(event);
            return MessageFormat.format(Constant.MessageCodeConstant.EVENT_CONFIRM_MESSAGE, EventEnum.valueOfCode(event).getDescription());
        } else {
            return Constant.MessageCodeConstant.ERROR_MESSAGE;
        }
    }

    /**
     * 事件中消息
     * @param user
     * @param message
     * @return
     */
    private String eventingMessage(User user, String message) {
        user.setLastOpTime(nowTime());
        if (Arrays.stream(Constant.KeyWorldConstant.EXIT).collect(Collectors.toList()).contains(message.toUpperCase())) {
            user.setStatus(BizStatusEnum.EXIT_CONFIRM.getCode());
            return MessageFormat.format(Constant.MessageCodeConstant.EXIT_CONFIRM_MESSAGE, EventEnum.valueOfCode(user.getEvent()).getDescription());
        }
        switch (EventEnum.valueOfCode(user.getEvent())) {
            case CHAT:
                return eventChat(user, message);
            case TRANSLATE:
                return eventTranslate(message);
            case GET_RESOURCE:
                return eventResource(message);
            default:
                return Constant.MessageCodeConstant.ERROR_MESSAGE;
        }
    }

    /**
     * 获取资源
     * @param message
     * @return
     */
    private String eventResource(String message) {
        if (StringUtils.isBlank(message)) {
            return "需要什么资源？";
        }
        //todo
        return MessageFormat.format("你要获取的资源【{0}】在这呢，拿去吧~", message);
    }

    /**
     * 获取翻译
     * @param message
     * @return
     */
    private String eventTranslate(String message) {
        if (StringUtils.isBlank(message)) {
            return "请输入要翻译的内容";
        }
        //todo api进行翻译
        return MessageFormat.format("超牛皮的翻译来喽~{0}原文：{1}，译文：{2}", Constant.LINE_FEED, message, message);
    }

    /**
     * 获取消息
     * @param user
     * @param message
     * @return
     */
    private String eventChat(User user, String message) {
        try {
            return MessageUtil.getReport(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void checkTimeOut(User user) {
        if (null == user.getStatus()) {
            return;
        }
        if (null == user.getLastOpTime()) {
            return;
        }
        if (Constant.TIME_OUT > (user.getLastOpTime() - nowTime())) {
            return;
        }
        user.setStatus(null);
    }

    private long nowTime() {
        return System.currentTimeMillis();
    }

}
