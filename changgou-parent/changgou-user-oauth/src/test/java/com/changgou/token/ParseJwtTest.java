package com.changgou.token;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/*****
 * @Author: www.itheima
 * @Date: 2019/7/7 13:48
 * @Description: com.changgou.token
 *  使用公钥解密令牌数据
 ****/
public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.fzS86aBYF8dYcA9y8ITCgRBwQd6ihhdeQPOJmmrdOqBfyahx_I2BrlTxW557C9sdefHfHYfkwGih0_OdhaqrkbXJi_exMRiyFiBSNloKxuYf9yYJwbpx0lH0bHWcqtO-AQHHhf5cB18R2mgVbrCzb7DQUCiRuLvpYid4lnVVQOF8k2uGzicf2l68ADtgrSmN90Fk95qq7F3fcgYkOSv-f1acVkruWv_LPHfSCbymZAei01gLE9BP2qz4vtX_VbsHPndPWeuJV_xWCIGIq_fB_Ztx6cchzAU0KgSjZJESlVejTu6XcoM5mopnYcsFyGJPe0QhlBKr1GVTFtsRTWMO3Q";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuXY7hd5uSG70OK9eLOiLUFlDCgal0EGgOuzQACM3RbX+j3PiiyufokzVC6fMTgaLM0ctz1Jy6K350MuVh5kKJsz3FSO7faeWghV5RMmAoLIGDoYcmfImYF2/gCta2SOrkx+Dq7vo0JUiyFAoeaS2AbN77IgTKSXXKKi/CHdO7jfNx0L9npFcTZ/Rk7/ppZYYj0hDzuif1UKv9tr7qWmj8icU16l0W20pE4Dq1xUn5l0kfhwLriKojtsj5nT8rMGvEemJzBEZWbAgPnx7Vzmxz8hj7FyxHDnQroe4Sevt4/Vv2+hU6G3sb1l9EFtd+VVnWd6CEq1S/S8O2LbtnMeW0QIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容 载荷
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
