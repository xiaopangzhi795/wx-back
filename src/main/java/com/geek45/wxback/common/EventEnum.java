package com.geek45.wxback.common;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 事件枚举值
 */
public enum EventEnum {

    CHAT("CHAT", "闲聊"),
    TRANSLATE("TRANSLATE", "翻译"),
    GET_RESOURCE("GET_RESOURCE", "获取资源"),
    UNKNOWN("UNKNOWN", "未知事件"),
    ;

    private String code;
    private String description;

    EventEnum(String code,String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static EventEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return UNKNOWN;
        }
        return Arrays.stream(EventEnum.values()).filter(eventEnum -> eventEnum.getCode().equals(code)).findAny().orElse(UNKNOWN);
    }
}
