package com.geek45.wxback.common;

import com.alibaba.fastjson.JSONObject;
import com.geek45.wxback.vo.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MessageUtil {

    public static String getXml(InputStream inputStream) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        return document.getRootElement().asXML();
    }

    private static final String url = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";

    public static String getReport(String message) throws IOException {
        Map<String, Object> result = HttpClientUtil.doGet(String.format("%s%s", url, message));
        log.info("result is :{}", JSONObject.toJSONString(result));
        JSONObject object = JSONObject.parseObject(Optional.ofNullable(result.get("result")).orElse("").toString());
        if (null!=object&&object.containsKey("content")) {
            return object.get("content").toString();
        }
        return "我不知道该怎么回答了呢";
    }

    public static String vo2Xml(Object obj, String rootNodeName) throws IllegalAccessException {
        Document document = DocumentFactory.getInstance().createDocument();
        document.addElement(rootNodeName);
        Element element = document.getRootElement();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            element.addElement(toUpperCaseFirstOne(field.getName())).addText(Optional.ofNullable(field.get(obj)).orElse("").toString());
        }
        return document.getRootElement().asXML();
    }

    public static void main(String[] args) throws DocumentException, IllegalAccessException, InstantiationException, IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xml><ToUserName><![CDATA[gh_977d40e24552]]></ToUserName>\n" +
                "<FromUserName><![CDATA[osedc6VD8_k8GNf4ExUAch8HBidA]]></FromUserName>\n" +
                "<CreateTime>1605012955</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[1]]></Content>\n" +
                "<MsgId>22978263018873148</MsgId>\n" +
                "</xml>";

        TextMessage textMessage = MessageUtil.xml2Vo(xml, TextMessage.class);
        TextMessage reportMessage = new TextMessage();
        reportMessage.setContent(getReport(textMessage.getContent()));
        reportMessage.setToUserName(textMessage.getFromUserName());
        reportMessage.setFromUserName(textMessage.getToUserName());
        reportMessage.setCreateTime(String.valueOf(new Date().getTime()));
        reportMessage.setMsgType("text");
        String message = MessageUtil.vo2Xml(reportMessage, "xml");
        System.err.println(message);
    }

    public static <T> T xml2Vo(String xml, Class<T> clazz) throws DocumentException, IllegalAccessException, InstantiationException {
        log.error("xml is :{}", xml);
        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(xml.getBytes()));
        Iterator<Element> iterator = document.getRootElement().elementIterator();
        Object obj = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Field> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, field -> field));
        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (!fieldMap.containsKey(element.getName()) && !fieldMap.containsKey(toLowerCaseFirstOne(element.getName()))) {
                return null;
            }
            Field field = fieldMap.get(toLowerCaseFirstOne(element.getName()));
            if (null == field) {
                field = fieldMap.get(element.getName());
            }
            field.setAccessible(true);
            field.set(obj, element.getText());
        }
        return (T) obj;
    }

    public static String toLowerCaseFirstOne(String str){
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        }else{
            return String.format("%s%s", Character.toLowerCase(str.charAt(0)), str.substring(1));
        }
    }

    public static String toUpperCaseFirstOne(String str){
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        }else{
            return String.format("%s%s", Character.toUpperCase(str.charAt(0)), str.substring(1));
        }
    }

}
