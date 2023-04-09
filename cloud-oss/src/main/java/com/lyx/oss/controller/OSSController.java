package com.lyx.oss.controller;

import com.lyx.common.base.result.R;
import com.lyx.oss.utils.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 黎勇炫
 * @date 2023年04月09日 11:09
 */
@RestController
@RequestMapping("/oss")
public class OSSController {

    @Autowired
    private OssUtil ossUtil;

     /**
       * 获取oss文件上传签名
       */
    @GetMapping("/policy")
    public R getUploadPolicy(){
        Map<String, String> uploadPolicy = ossUtil.getUploadPolicy();
        return R.ok(uploadPolicy);
    }
}
