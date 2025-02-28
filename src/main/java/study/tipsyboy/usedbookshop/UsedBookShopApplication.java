package study.tipsyboy.usedbookshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UsedBookShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsedBookShopApplication.class, args);
	}

}
