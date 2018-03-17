package org.baeldung.security.totp.util;

import static org.junit.Assert.assertEquals;
import java.security.GeneralSecurityException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.baeldung.security.totp.PasscodeGenerator;
import org.junit.Test;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TOTPUtilsTest {

    @Test
    public void testGenerateTOTP() {
        try {
            String secret = TOTPCustomUtils.generateSecret();

            System.out.println("secret: " + secret);
            System.out.println("(code) googleOTP: " + getGoogleOTPCode(secret));

            long code = TOTPCustomUtils.getRfcOTPCode(secret);
            System.out.println("(code) rfcOTP: " + code);
            assertEquals(getGoogleOTPCode(secret), TOTPCustomUtils.getRfcOTPCode(secret));

            System.out.println("проверить (code - " + TOTPCustomUtils.getRfcOTPCode(secret) + "): " + TOTPCustomUtils.checkCode(secret, code));
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("проверить через 15 сек. (code - " + TOTPCustomUtils.getRfcOTPCode(secret) + "): " + TOTPCustomUtils.checkCode(secret, code));
            try {
                Thread.sleep(75000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("проверить через 75 сек. (code - " + TOTPCustomUtils.getRfcOTPCode(secret) + "): " + TOTPCustomUtils.checkCode(secret, code));
        } catch (DecoderException|GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private long getGoogleOTPCode(String secretKey) throws DecoderException, GeneralSecurityException {
        byte[] key = Hex.decodeHex(secretKey.toCharArray());
        Mac hmac;
        hmac = Mac.getInstance(TOTPCustomUtils.CRYPTO.getValue());
        SecretKeySpec macKey = new SecretKeySpec(key, "RAW");
        hmac.init(macKey);
        PasscodeGenerator passcodeGenerator = new PasscodeGenerator(hmac);
        return Long.parseLong(passcodeGenerator.generateTimeoutCode());
    }
}
