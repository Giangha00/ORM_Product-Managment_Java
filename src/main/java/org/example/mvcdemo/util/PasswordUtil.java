package org.example.mvcdemo.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Tien ich bam/kiem tra mat khau bang BCrypt (khong bao gio luu plain text password).
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    public static boolean matches(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
