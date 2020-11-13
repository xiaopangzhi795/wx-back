package com.geek45.wxback.common;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 状态枚举值
 */
public enum BizStatusEnum {

    NON_STATUS("NON_STATUS", "无状态"),
    EVENT_CONFIRM("EVENT_CONFIRM", "事件确认"),
    EVENTING("EVENTING", "事件中"),
    EXIT_CONFIRM("EXIT_CONFIRM", "退出确认"),
    UNKNOWN("UNKNOWN", "未知状态"),
    ;

    private String code;
    private String description;

    BizStatusEnum(String code, String description) {
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

    public static BizStatusEnum valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return UNKNOWN;
        }
        return Arrays.stream(BizStatusEnum.values()).filter(bizStatusEnum -> bizStatusEnum.getCode().equals(code)).findAny().orElse(UNKNOWN);
    }

}
