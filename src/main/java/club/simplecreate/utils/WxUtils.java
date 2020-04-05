package club.simplecreate.utils;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class WxUtils {
    private static final String  WXURL="https://api.weixin.qq.com/sns/jscode2session";
    private static final String APPID="wxb2810458e3b57df3";
    private static final String APPSECRET="ee939c0ec78be8c02c6b2d1a58ce4f31";
    @SuppressWarnings("unchecked")
    public static String resolveCode(String code){
        String url = WXURL+ "?appid=" + APPID + "&secret=" + APPSECRET + "&js_code=" + code + "&grant_type="
                + "authorization_code";
        // 发送请求
        String data = HttpUtil.get(url);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> json = null;
        try {
            json = mapper.readValue(data, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 形如{"session_key":"6w7Br3JsRQzBiGZwvlZAiA==","openid":"oQO565cXXXXXEvc4Q_YChUE8PqB60Y"}的字符串
        for(Map.Entry<String, Object> entry: json.entrySet()){
            System.out.println(entry.getKey()+"     "+entry.getValue());
        }
        return (String) json.get("openid");
    }
}
