package com.ourhome.taehyeong.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class GenerateCodeUtil {

    private GenerateCodeUtil() {}

    public static String generateCode() {
        String code;

        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            code = String.valueOf(random.nextInt(900000) + 100000);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem occured when generating the random code.");
        }

        return code;
    }
}