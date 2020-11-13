package com.geek45.wxback.common;

public interface Constant {

    Long TIME_OUT_MIN = 30L;
    Long TIME_OUT = 1000 * 60L * TIME_OUT_MIN;
    String LINE_FEED = "\n";

    interface KeyWorldConstant{
        String[] HELP = {"HELP", "?", "？"};
        String[] YES = {"YES", "是", "OK"};
        String[] EXIT = {"EXIT", "Q", "退出"};
    }

    interface MessageCodeConstant{
        String NON_MESSAGE = String.format("欢迎关注极客阿钱，一看你就是最帅的仔。%s%s我们目前支持的服务有：%s1、闲聊。%s2、翻译。%s3、获取资源%s%s请回复对应的编号，进行选择。%s回复'help'或'?'重新获取本菜单哟", LINE_FEED, LINE_FEED, LINE_FEED, LINE_FEED, LINE_FEED, LINE_FEED, LINE_FEED, LINE_FEED);

        String PLEASE_LOGIN = "请帅气的你先登录哟";

        String ERROR_MESSAGE = String.format("非常抱歉，我们还没有支持到该服务，程序员正夜以继日的赶工，争取早日支持该模式哟，非常感谢您的支持。%s如果想要闲聊模式的话，请选择1，进入闲聊模式。%s回复'help'或'?'获取帮助菜单。", LINE_FEED, LINE_FEED);

        String EVENT_CONFIRM_MESSAGE = String.format("您将要切换到{0}模式，%s%s回复'是'或'ok'或'yes'进行确认。%s回复其他或者不回复，将退出该模式。", LINE_FEED, LINE_FEED, LINE_FEED);

        String EVENTING_MESSAEG = String.format("您已经成功开启{0}模式，请尽情使用。%s%sTIPS:如果{1}分钟未操作，将会退出该模式。%s如果需要主动退出，请输入'退出'或'exit'或'q'即可退出该模式", LINE_FEED, LINE_FEED, LINE_FEED);

        String EXIT_CONFIRM_MESSAGE = String.format("您将退出{0}模式，%s%s回复'是'或'ok'或'yes'进行确认，回复其他或不回复，继续留在该模式", LINE_FEED, LINE_FEED);

        String CONTINUE_MESSAGE = "咱们继续吧";

    }

    interface FieldConstant{
        String TO_USER_NAME = "ToUserName";
        String FROM_USER_NAME = "FromUserName";


    }
}
