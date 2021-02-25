package com.changgou.filter;

import com.changgou.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器
 * 实现用户权限鉴别
 * @author Xu Rui
 * @date 2021/2/24 14:00
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    //令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";

    /***
     * 全局过滤器
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取Request、Response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //获取请求的URI
        String path = request.getURI().getPath();

        //如果是登录、goods等开放的微服务[这里的goods部分开放],则直接放行,这里不做完整演示，完整演示需要设计一套权限系统
        if (!URLFilter.hasAuthorize(path)) {
            //放行
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        }

        //1.获取头文件中的令牌信息
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        boolean hasToken = true;

        //2.如果头文件中没有，则从请求参数中获取
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            hasToken = false;
        }

        //3.如果前两者中没有，则从Cookie中获取
        if (StringUtils.isEmpty(token)) {
            HttpCookie httpCookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(httpCookie != null)
                token = httpCookie.getValue();
            hasToken = false;
        }

        //如果为空，则输出错误代码
        if (StringUtils.isEmpty(token)) {
            //设置没有权限，401错误代码
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }else{
            if(!hasToken){
                //令牌判断是否为空，如果不为空，则将令牌放到头文件中，放行
                //判断令牌是否有bearer前缀
                if(!token.startsWith("bearer ") && !token.startsWith("Bearer ")){
                    token = "bearer " + token;
                }
                //将令牌封装到头文件中
                request.mutate().header(AUTHORIZE_TOKEN, token);
            }

        }


//        //解析令牌数据
//        try {
//            Claims claims = JwtUtil.parseJWT(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //解析失败，响应401错误
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }



        //放行
        return chain.filter(exchange);
    }


    /***
     * 过滤器执行顺序
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}