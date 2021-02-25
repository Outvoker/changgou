package com.changgou.filter;

/**
 * 不需要认证就能访问的路径校验
 * @author Xu Rui
 * @date 2021/2/25 15:56
 */
public class URLFilter {

    /**
     * 要放行的路径
     */
    private static final String noAuthorizedUrls = "/api/user/add,/api/user/login";


    /**
     * 判断 当前的请求的地址中是否在已有的不拦截的地址中存在,如果存在 则返回true  表示  不拦截   false表示拦截
     *
     * @param uri 获取到的当前的请求的地址
     * @return
     */
    public static boolean hasAuthorize(String uri) {
        String[] split = noAuthorizedUrls.split(",");

        for (String s : split) {
            if (s.equals(uri)) {
                return true;
            }
        }
        return false;
    }


}