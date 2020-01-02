package cn.wl.android.lib.data.core;


/**
 * @author ace
 * @create 2017/12/17.
 */
public class KeyConfiguration {

    private static String userPriKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM9F9zgDlICamLexJapEGT9AvZRD+6+f8" +
            "Gxto2hrmbPUBpYlLd4/uVIEV7M9ZsKyQiGrpYVOEou0YOWh875BVvuLeHSFEvln7jO4Bs5JjiI" +
            "y/W9ivIcDKM8Zfa+5saWvfKuuO3dANjBtO6PzneDzOBnR7kbQuF1yVZkscQ9oWkUjAgMBAAECg" +
            "YArXRjmmCEmcrGGpMIbiBm8GQBQl5R9Xrm/BYUNYI2MiMxD901MVQqhdRUgA7WPABpDzxRW+kl" +
            "4/ujSwforkKHwqAtHeb834L3QBicjvZ9Tkzo0GErt+wOsZhDI3CxogRLD4vL9Xqh6k/53dm0sx";
//            "6LohKFtQOQabRmWupz5Jc3wMQJBAOqtVOaKRL6ycOzPdFeHAK3hvhtdD4P65WLOoQ8XYYwq/qXlt1Z6wxH73mWO520v5AiwAP5GY5rerp+4AtsNyUkCQQDiGzaHPPbB9bYPjK4xrTA48+X1oVVd84B" +
//            "86XF+WNRwLt+nxSeGl1mhgf1dbUwlmfkJ8JWUSEcBdKQ6/HTQ1KcLAkBsJfjsTWgk4aL83xXkiEid2Vx8y8QstGElycebZtEDgYTc+yIkbmqbTRFOiC7KuLlD76hlhha89kZPQMPAI3hRAkBQUhx3xEd" +
//            "gNZocQfxrdzuHL9VEAbDitCqztPX1TTcCNxSKc7YL0N4tSpEnzDjdrqnSRx3L1DUtJjNlJOOWf8RrAkEAu5juRM2OfYUXXe7p3MjwutiIS+XodK/tl4q/4eQsHj/Fos27MlBCtORYCyNXF/Y0u5hOQuUqf35DK2U/f5ICBg==";


    public static KeyProvider mProvider;

    public static byte[] getKey() {
        String g0 = userPriKey;

        KeyProvider provider =
                KeyConfiguration.mProvider;

        if (provider != null) {
            g0 += provider.g1();
            g0 += provider.g2();
            g0 += provider.g3();
        }

        return RsaKeyHelper.toBytes(g0);
    }


    public interface KeyProvider {

        String g1();

        String g2();

        String g3();

    }
//    public static byte[] userPriKey = RsaKeyHelper.toBytes("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM9F9zgDlICamLexJapEGT9AvZRD+6+f8Gxto2hrmbPUBpYlLd4/uVIEV7M9ZsKyQiGrpYVOEou0YOWh875BVvuLeHSFEvln7jO4Bs5JjiIy/W9ivIcDKM8Zfa+5saWvfKuuO3dANjBtO6PzneDzOBnR7kbQuF1yVZkscQ9oWkUjAgMBAAECgYArXRjmmCEmcrGGpMIbiBm8GQBQl5R9Xrm/BYUNYI2MiMxD901MVQqhdRUgA7WPABpDzxRW+kl4/ujSwforkKHwqAtHeb834L3QBicjvZ9Tkzo0GErt+wOsZhDI3CxogRLD4vL9Xqh6k/53dm0sx6LohKFtQOQabRmWupz5Jc3wMQJBAOqtVOaKRL6ycOzPdFeHAK3hvhtdD4P65WLOoQ8XYYwq/qXlt1Z6wxH73mWO520v5AiwAP5GY5rerp+4AtsNyUkCQQDiGzaHPPbB9bYPjK4xrTA48+X1oVVd84B86XF+WNRwLt+nxSeGl1mhgf1dbUwlmfkJ8JWUSEcBdKQ6/HTQ1KcLAkBsJfjsTWgk4aL83xXkiEid2Vx8y8QstGElycebZtEDgYTc+yIkbmqbTRFOiC7KuLlD76hlhha89kZPQMPAI3hRAkBQUhx3xEdgNZocQfxrdzuHL9VEAbDitCqztPX1TTcCNxSKc7YL0N4tSpEnzDjdrqnSRx3L1DUtJjNlJOOWf8RrAkEAu5juRM2OfYUXXe7p3MjwutiIS+XodK/tl4q/4eQsHj/Fos27MlBCtORYCyNXF/Y0u5hOQuUqf35DK2U/f5ICBg==");
}
