package com.lyx.auth.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.lyx.common.base.exception.BizException;
import com.lyx.common.base.result.IResultCode;
import com.lyx.common.base.result.R;
import com.lyx.common.base.result.ResultCode;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.KeyPair;
import java.security.Principal;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * @author 黎勇炫
 * @date 2022年10月09日 19:56
 */
@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private KeyPair keyPair;
    private final TokenEndpoint tokenEndpoint;

    private final Producer producer;

    @PostMapping("/token")
    public Object postAccessToken(HttpServletRequest request,Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        // 首先校验验证码
        if(parameters.get("captcha").equals(request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY))){
            OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
            return R.ok(accessToken);
        }else {
            throw new BizException(ResultCode.CAPTCHA_ERROR);
        }
    }


    @GetMapping("/public-key")
    public Map<String, Object> getPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

     /**
       * 验证码
       */
    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response, HttpServletRequest request){
        // 设置响应信息
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        // 生成文字验证码
        String text = producer.createText();
        // 生成图片验证码
        BufferedImage image = producer.createImage(text);
        // 保存到验证码到 session
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
        } catch (IOException e) {
            throw new BizException(ResultCode.CAPTCHA_OUTPUT_ERROR);
        }
    }
}