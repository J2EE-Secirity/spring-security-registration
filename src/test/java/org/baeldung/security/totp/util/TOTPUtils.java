package org.baeldung.security.totp.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.baeldung.security.totp.PasscodeGenerator;
import org.baeldung.security.totp.TOTP;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.io.UnsupportedEncodingException;

public class TOTPUtils {

    public static final int SECRET_SIZE = 20;

    public static final int PASS_CODE_LENGTH = 6;

    public static final int INTERVAL = 30; // (точность)

    public static final int WINDOW = 1; // 30 sec.

    public static final Crypto CRYPTO = Crypto.HMAC_SHA1;

    public static boolean checkCode(String secret, long code) throws DecoderException, GeneralSecurityException {
        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go.
        int window = WINDOW;
        long currentInterval = getCurrentInterval();
        byte[] key = Hex.decodeHex(secret.toCharArray());

        for (int i = -window; i <= window; ++i) {
            long hash = TOTP.generateTOTP(key, currentInterval + i, PASS_CODE_LENGTH, CRYPTO);
            if (hash == code) {
                return true;
            }
        }

        // The validation code is invalid.
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
        return TOTP.generateTOTP(key, currentInterval, PASS_CODE_LENGTH, CRYPTO);
    }

    public static long getGoogleOTPCode(String secretKey) throws DecoderException, GeneralSecurityException {
        byte[] key = Hex.decodeHex(secretKey.toCharArray());
        Mac hmac;
        hmac = Mac.getInstance(CRYPTO.getValue());
        SecretKeySpec macKey = new SecretKeySpec(key, "RAW");
        hmac.init(macKey);
        PasscodeGenerator passcodeGenerator = new PasscodeGenerator(hmac);
        return Long.parseLong(passcodeGenerator.generateTimeoutCode());
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
            } catch (UnsupportedEncodingException|InvalidKeyException|NoSuchAlgorithmException e) {}
            return digest;
        }
    }
}
