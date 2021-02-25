package com.changgou.oauth.interceptor;

import com.changgou.oauth.util.AdminToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author Xu Rui
 * @date 2021/2/25 14:49
 */
@Configuration
public class TokenRequestInterceptor implements RequestInterceptor {
    /**
     * Feign执行之前，进行拦截
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //生成admin令牌
        String token  = AdminToken.adminToken();
        requestTemplate.header("Authorization", "bearer " + token);

//        try {
//            //使用RequestContextHolder工具获取request相关变量
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            if (attributes != null) {
//                //取出request
//                HttpServletRequest request = attributes.getRequest();
//                //获取所有头文件信息的key
//                Enumeration<String> headerNames = request.getHeaderNames();
//                if (headerNames != null) {
//                    while (headerNames.hasMoreElements()) {
//                        //头文件的key
//                        String name = headerNames.nextElement();
//                        //头文件的value
//                        String values = request.getHeader(name);
//                        //将令牌数据添加到头文件中
//                        requestTemplate.header(name, values);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
