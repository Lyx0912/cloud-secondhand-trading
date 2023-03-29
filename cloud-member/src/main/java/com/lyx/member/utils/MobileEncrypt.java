package com.lyx.member.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author 黎勇炫
 * @date 2023年03月29日 11:54
 */
public class MobileEncrypt {

     /**
       * 手机号加密
       */
    public static String encrypt(String mobile){
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }
}
