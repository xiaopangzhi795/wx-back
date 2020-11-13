package com.geek45.wxback.vo;

import lombok.Data;

@Data
public class TextMessage {
    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String msgType;
    private String content;
    private String msgId;
}
