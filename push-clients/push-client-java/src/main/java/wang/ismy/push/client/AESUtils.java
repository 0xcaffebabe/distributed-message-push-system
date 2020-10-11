package wang.ismy.push.client;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author MY
 * @date 2020/10/8 21:22
 */
public class AESUtils {

    public static String generateKey() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(secureRandom);
        return new String(Base64.getEncoder().encode(keyGenerator.generateKey().getEncoded()));
    }

    public static byte[] decodeKey(String base64EncodedStr){
        return Base64.getDecoder().decode(base64EncodedStr);
    }

}
