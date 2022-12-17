package me.kirillirik.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public final class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final SecretKeyFactory FACTORY;

    static {
        try {
            FACTORY = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashPassword(String password, String salt) {
        try {
            final KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
            return toString(FACTORY.generateSecret(spec).getEncoded());
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateSalt() {
        final byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return toString(salt);
    }

    private static String toString(byte[] byteArray) {
        return Base64.getEncoder().encodeToString(byteArray);
    }
}
