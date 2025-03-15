package app.com.tw.monster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "app.com.tw")
public class MonsterApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonsterApplication.class, args);
	}

}
