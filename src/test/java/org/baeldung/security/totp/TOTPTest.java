package org.baeldung.security.totp;

import static org.junit.Assert.assertEquals;
import java.security.GeneralSecurityException;
import org.apache.commons.codec.DecoderException;
import org.baeldung.security.totp.util.TOTPUtils;
import org.junit.Test;

/**
 * @see https://www.programcreek.com/java-api-examples/index.php?source_dir=TOTP-authentication-demo-master/src/main/java/me/brandonc/security/totp/util/TOTP.java
 * https://pritomkumar.blogspot.com/2014/10/java-code-for-calculating-hmac-sha1.html
 */

public class TOTPTest {

    @Test
    public void testGenerateTOTP() {
        try {
            String secret = TOTPUtils.generateSecret();

            System.out.println("secret: " + secret);
            System.out.println("(code) googleOTP: " + TOTPUtils.getGoogleOTPCode(secret));

            long code = TOTPUtils.getRfcOTPCode(secret);
            System.out.println("(code) rfcOTP: " + code);
            assertEquals(TOTPUtils.getGoogleOTPCode(secret), TOTPUtils.getRfcOTPCode(secret));

            System.out.println("check (code): " + TOTPUtils.checkCode(secret, code));
            try {
                Thread.sleep(75000); // 75000
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("check (code): " + TOTPUtils.checkCode(secret, code));
        } catch (DecoderException|GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
