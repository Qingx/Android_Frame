package cn.wl.android.lib.data.core;


import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ace on 2017/9/10.
 */
public class RsaKeyHelper {

    public static Map<String, byte[]> generateKey(String password) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(password.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        Map<String, byte[]> map = new HashMap<>();
        map.put("pub", publicKeyBytes);
        map.put("pri", privateKeyBytes);
        return map;
    }

    public static byte[] toBytes(String s) {
        return Base64.getDecoder().decode(s.replace("\r\n", "").getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取公钥
     *
     * @param filename 文件名
     * @return PublicKey
     * @throws Exception e
     */
    PublicKey getPublicKey(String filename) throws Exception {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filename);
        if (null != resourceAsStream) {
            DataInputStream dis = new DataInputStream(resourceAsStream);
            byte[] keyBytes = new byte[resourceAsStream.available()];
            dis.readFully(keyBytes);
            dis.close();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } else {
            return null;
        }
    }

    /**
     * 获取密钥
     *
     * @param filename 文件名
     * @return PrivateKey
     * @throws Exception e
     */
    PrivateKey getPrivateKey(String filename) throws Exception {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filename);
        if (null != resourceAsStream) {
            DataInputStream dis = new DataInputStream(resourceAsStream);
        byte[] keyBytes = new byte[resourceAsStream.available()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
        } else {
            return null;
        }
    }

    /**
     * 获取公钥
     *
     * @param publicKey 二进制PK
     * @return PublicKey
     * @throws Exception e
     */
    PublicKey getPublicKey(byte[] publicKey) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * 获取密钥
     *
     * @param privateKey 二进制PK
     * @return PrivateKey
     * @throws Exception e
     */
    PrivateKey getPrivateKey(byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


}

