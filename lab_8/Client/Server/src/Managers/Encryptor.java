package Managers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
    public static String getHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        md.update(password.getBytes());
        byte[] hashedPassword = md.digest();
        return byteArrayToHexString(hashedPassword);
    }
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
