package com.lyx.common.web.utils;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.lyx.common.base.constant.SecurityConstants;

import com.nimbusds.jose.JWSObject;
import net.minidev.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;

/**
 * @author 黎勇炫
 * @date 2022年10月29日 12:01
 */
public class UserContext {

    public static String getCurrentUserName(){
        String token = getToken();
        if(!StringUtils.isEmpty(token)){
            try {
                // 解析token
                JSONObject jsonObject = JWSObject.parse(token).getPayload().toJSONObject();
                if(!Objects.isNull(jsonObject)){
                    return (String) jsonObject.get("username");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getToken() {
        // 获取servlet请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 获取请求中的token
        String token = requestAttributes.getRequest().getHeader(SecurityConstants.AUTHORIZATION_KEY);
        // 截取掉前缀
        token = token.replace(SecurityConstants.JWT_PREFIX,"");
        return token;
    }

    public static Long getCurrentUserId(){
        // 获取servlet请求对象
        String token = getToken();
        if(!StringUtils.isEmpty(token)){
            try {
                // 解析token
                JSONObject jsonObject = JWSObject.parse(token).getPayload().toJSONObject();
                if(!Objects.isNull(jsonObject)){
                    return (Long) jsonObject.get("userId");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<String> getRoleList() {
        // 获取servlet请求对象
        String token = getToken();
        if(!StringUtils.isEmpty(token)){
            try {
                // 解析token
                JSONObject jsonObject = JWSObject.parse(token).getPayload().toJSONObject();
                if(!Objects.isNull(jsonObject)){
                    return (List<String>) jsonObject.get("authorities");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
