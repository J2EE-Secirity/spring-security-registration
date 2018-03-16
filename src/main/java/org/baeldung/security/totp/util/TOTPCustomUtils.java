package org.baeldung.security.totp.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.baeldung.security.totp.Crypto;
import org.baeldung.security.totp.TOTPCustom;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.io.UnsupportedEncodingException;

public class TOTPCustomUtils {

    public static final int SECRET_SIZE = 20;

    public static final int PASS_CODE_LENGTH = 6;

    public static final int INTERVAL = 30;

    public static final int WINDOW = 1;

    public static final Crypto CRYPTO = Crypto.HMAC_SHA1;

    public static boolean checkCode(String secret, long code) throws DecoderException, GeneralSecurityException {
        int window = WINDOW;
        long currentInterval = getCurrentInterval();
        byte[] key = Hex.decodeHex(secret.toCharArray());

        for (int i = -window; i <= window; ++i) {
            long hash = TOTPCustom.generateTOTP(key, currentInterval + i, PASS_CODE_LENGTH, CRYPTO);
            if (hash == code) {
                return true;
            }
        }
        return false;
    }

    public static String generateSecret() {
        return HMAC.hmacDigest(UUID.randomUUID().toString(), "key", CRYPTO)
            .substring(0, SECRET_SIZE)
            .toUpperCase();
    }

    public static long getRfcOTPCode(String secretKey) throws DecoderException, GeneralSecurityException {
        byte[] key = Hex.decodeHex(secretKey.toCharArray());
        long currentInterval = getCurrentInterval();
        return TOTPCustom.generateTOTP(key, currentInterval, PASS_CODE_LENGTH, CRYPTO);
    }

    private static long getCurrentInterval() {
        long currentInterval = (System.currentTimeMillis() / 1000) / INTERVAL;
        return currentInterval;
    }


    static class HMAC {
        public static String hmacDigest(String msg, String keyString, Crypto crypto) {
            String digest = null;
            try {
                SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), crypto.getValue());
                Mac mac = Mac.getInstance(crypto.getValue());
                mac.init(key);

                byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
                StringBuffer hash = new StringBuffer();
                for (int i = 0; i < bytes.length; i++) {
                    String hex = Integer.toHexString(0xFF & bytes[i]);
                    if (hex.length() == 1) {
                        hash.append('0');
                    }
                    hash.append(hex);
                }
                digest = hash.toString();
            } catch (UnsupportedEncodingException|InvalidKeyException|NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return digest;
        }
    }
}
