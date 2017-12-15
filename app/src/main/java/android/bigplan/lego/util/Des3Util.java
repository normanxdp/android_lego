package android.bigplan.lego.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.bigplan.lego.global.AbConstant;

/**
 * 3DES加密工具类
 */
public class Des3Util{
    
    /**
     *  密钥
     */
    private final static String secretKey = AbConstant.CRYPT_KEY;

    /**
     * 字符串加密
     * @param plain
     * @return
     */
    @SuppressLint("TrulyRandom")
    public static String enCode(String plain) {
        try {
            String KEY_CRPTY_ALGORITHM = "DESede";
            
            byte[] tripleDesKeyData = new byte[24];
            byte[] kyebyte = secretKey.getBytes();
            int len = (kyebyte.length > 24) ? 24 : kyebyte.length;
            for (int i = 0; i < len; i++) {
                tripleDesKeyData[i] = kyebyte[i];
            }
            SecretKey secretKey = new SecretKeySpec(tripleDesKeyData, KEY_CRPTY_ALGORITHM);
            
            Cipher cipher = Cipher.getInstance(KEY_CRPTY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedMessage = cipher.doFinal(plain.getBytes());
            
            return new String(Base64.encode(encryptedMessage, Base64.NO_WRAP));
        }
        catch (Exception e) {
            return "";
        }
    }
    
    /**
     * 字符串解密
     * @param plain
     * @return
     */
    public static String deCode(String plain) {
        try {
            String KEY_CRPTY_ALGORITHM = "DESede";

            byte[] tripleDesKeyData = new byte[24];
            byte[] kyebyte = secretKey.getBytes();
            int len = (kyebyte.length > 24) ? 24 : kyebyte.length;
            for (int i = 0; i < len; i++) {
                tripleDesKeyData[i] = kyebyte[i];
            }
            SecretKey secretKey = new SecretKeySpec(tripleDesKeyData, KEY_CRPTY_ALGORITHM);

            Cipher cipher = Cipher.getInstance(KEY_CRPTY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] bytes = plain.trim().getBytes();
            byte[] decode = Base64.decode(bytes, Base64.DEFAULT);
            byte[] bytes1 = cipher.doFinal(decode);
            String res = new String(bytes1);
            return res ;
        }
        catch (Exception e) {
            return "";
        }
    }
}
