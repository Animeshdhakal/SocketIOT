package animesh.app.server.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA256Util {
    public static String createHash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] byteData = md.digest(createHash(salt.toLowerCase()));
            return Base64.getEncoder().encodeToString(byteData);
        } catch (NoSuchAlgorithmException e) {
            // ignore, will never happen.
        }
        return password;
    }

    private static byte[] createHash(String val) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256").digest(val.getBytes(StandardCharsets.UTF_8));
    }

}
