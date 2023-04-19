package com.lyx.common.web.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class TokenFeignClientInterceptor implements RequestInterceptor {

  /**
   * 通过feign调用微服务之前都先检查下头文件，将请求头文件中的令牌数据再放入到header中，再调用其他微服务
   */
  @Override
  public void apply(RequestTemplate requestTemplate) {
    try {
      ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes();
      if (null != servletRequestAttributes) {
        HttpServletRequest request = servletRequestAttributes.getRequest();
        // 获取所有头文件信息的key
        Enumeration<String> headerNames = request.getHeaderNames();
        if (null != headerNames) {
          while (headerNames.hasMoreElements()) {
            // 获取头文件的key和value
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            // 跳过content-length，不然可能会报too many bites written问题
            if ("content-length".equalsIgnoreCase(headerName)) {
              continue;
            }
            // 将令牌数据添加到头文件中，当用feign调用的时候，会传递给下一个微服务
            requestTemplate.header(headerName, headerValue);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}