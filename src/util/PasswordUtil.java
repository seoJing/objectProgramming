package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest; //자바에서 SHA-256, SHA-512, MD5 같은 해시 알고리즘을 구현한 클래스
import java.security.NoSuchAlgorithmException;

/**
 * 비밀번호 해시 유틸리티
 * - salt로 userId를 사용
 * - SHA-256 기반
 */
public class PasswordUtil {

    public static String hashPasswordWithIdSalt(String userId, String rawPassword) {
        String salted = userId + ":" + rawPassword;
        return sha256(salted);    //salted 문자열 전체를 SHA-256으로 해싱
    }

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
