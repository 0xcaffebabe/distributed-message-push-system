package wang.ismy.push.client;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.Assert.assertArrayEquals;

public class AESUtilsTest extends TestCase {

    String key = "gnjikldfgnjidkolfgjiukodf";

    public void testGenerateKey() {
        Key k1 = AESUtils.generateKey(this.key);
        Key k2 = AESUtils.generateKey(this.key);
        assertArrayEquals(k1.getEncoded(), k2.getEncoded());
    }

    public void testGenerateKeyBase64() {
        Key key = AESUtils.generateKey(this.key);
        String encoded = Base64.getEncoder().encodeToString(key.getEncoded());
        assertEquals(encoded, AESUtils.generateKeyBase64(this.key));
    }

    public void testEncrypt() {
        byte[] source = "123".getBytes();
        byte[] encrypt = AESUtils.encrypt(source,this.key);
        byte[] decrypt = AESUtils.decrypt(encrypt, this.key);
        assertArrayEquals(source, decrypt);
    }
}