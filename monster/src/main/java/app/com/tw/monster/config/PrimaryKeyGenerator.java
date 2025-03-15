package app.com.tw.monster.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PrimaryKeyGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 生成主鍵與時間戳的方法
    public static GeneratedKey generateKey(String prefix) {
        String randomPart = generateRandomNumber(15);
        String pk = prefix + randomPart;
        String currentDateTime = LocalDateTime.now().format(FORMATTER);
        return new GeneratedKey(pk, currentDateTime);
    }

    // 生成指定長度的數字亂碼
    private static String generateRandomNumber(int length) {
        StringBuilder randomPart = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomPart.append(random.nextInt(10));
        }
        return randomPart.toString();
    }

    // 儲存生成的 PK 和時間戳
    public static class GeneratedKey {
        private final String pk;
        private final String timestamp;

        public GeneratedKey(String pk, String timestamp) {
            this.pk = pk;
            this.timestamp = timestamp;
        }

        public String getPk() {
            return pk;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
