package cn.wl.android.lib.data.core;

import android.text.format.DateUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.wl.android.lib.utils.Times;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by ace on 2017/9/10.
 */
public class JWTHelper {

    private static long OFFSET_TOKEN = 7 * DateUtils.DAY_IN_MILLIS;
    private static RsaKeyHelper rsaKeyHelper = new RsaKeyHelper();

    private static Date getTokenTime() {
        long current = Times.current();

        return new Date(current + OFFSET_TOKEN);
    }

    /**
     * 密钥加密token
     *
     * @param jwtInfo    加密数据
     * @param priKeyPath 私钥
     * @param expire     过期时间
     * @return token
     */
    public static String generateToken(IJWTInfo jwtInfo, String priKeyPath, int expire) throws Exception {
        return Jwts.builder()
                .setSubject(jwtInfo.getUniqueName())
                .claim(CommonConstants.JWT_KEY_USER_ID, jwtInfo.getId())
                .claim(CommonConstants.JWT_KEY_NAME, jwtInfo.getName())
                .setExpiration(getTokenTime())
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKeyPath))
                .compact();
    }

    /**
     * 密钥加密token
     *
     * @param jwtInfo 加密数据
     * @param priKey  私钥
     * @param expire  过期时间
     * @return token
     */
    public static String generateToken(IJWTInfo jwtInfo, byte[] priKey, int expire) throws Exception {
        return Jwts.builder()
                .setSubject(jwtInfo.getUniqueName())
                .claim(CommonConstants.JWT_KEY_USER_ID, jwtInfo.getId())
                .claim(CommonConstants.JWT_KEY_NAME, jwtInfo.getName())
                .claim(CommonConstants.JWT_KEY_DEPT, jwtInfo.getDeptIds())
                .setExpiration(getTokenTime())
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKey))
                .compact();
    }

    /**
     * 公钥解析token
     *
     * @param token      token
     * @param pubKeyPath 公约
     * @return Jws<Claims>
     */
    public static Jws<Claims> parserToken(String token, String pubKeyPath) throws Exception {
        return Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKeyPath)).parseClaimsJws(token);
    }

    /**
     * 公钥解析token
     *
     * @param token  token
     * @param pubKey 公约
     * @return Jws<Claims>
     */
    public static Jws<Claims> parserToken(String token, byte[] pubKey) throws Exception {
        return Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKey)).parseClaimsJws(token);
    }
}
