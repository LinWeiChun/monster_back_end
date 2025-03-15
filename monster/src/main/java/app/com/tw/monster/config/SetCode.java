package app.com.tw.monster.config;

import java.security.MessageDigest;
import java.util.Random;

public class SetCode {
    public String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String confirmCode(){
        StringBuilder randomPart = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            randomPart.append(random.nextInt(10));
        }
        return randomPart.toString();
    }
}
