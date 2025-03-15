package app.com.tw.monster.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transform {
    public String formatDate(String date) {
        try {
            LocalDate parsedDate;

            // 處理西元格式
            if (date.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) { // e.g., "2000-1-1"
                parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"));
            } else if (date.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) { // e.g., "2000/01/3"
                parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/M/d"));
            } else if (date.matches("\\d{8}")) { // e.g., "20000101"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                parsedDate = LocalDate.parse(date, formatter);
            }
            // 處理民國年格式
            else if (date.matches("\\d{3}/\\d{1,2}/\\d{1,2}")) { // e.g., "89/01/01"
                int year = Integer.parseInt(date.substring(0, 3)) + 1911;
                String monthDay = date.substring(4).replace("/", "-");
                parsedDate = LocalDate.of(year, Integer.parseInt(monthDay.split("-")[0]),
                        Integer.parseInt(monthDay.split("-")[1]));
            } else if (date.matches("\\d{3}\\d{2}\\d{2}")) { // e.g., "890101"
                int year = Integer.parseInt(date.substring(0, 3)) + 1911;
                String month = date.substring(3, 5);
                String day = date.substring(5, 7);
                parsedDate = LocalDate.of(year, Integer.parseInt(month), Integer.parseInt(day));
            } else if (date.matches("\\d{3}-\\d{1,2}-\\d{1,2}")) { // e.g., "89-1-1"
                String[] parts = date.split("-");
                int year = Integer.parseInt(parts[0]) + 1911;
                parsedDate = LocalDate.of(year, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            } else {
                throw new IllegalArgumentException("日期格式不正確！");
            }

            // 格式化為 yyyy-MM-dd
            return parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        } catch (Exception e) {
            throw new IllegalArgumentException("日期格式不正確或無效，例如月份或日期超出範圍！");
        }
    }
}
