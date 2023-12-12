package study.tipsyboy.tipsyboyMall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TipsyboyMallApplication {

	public static void main(String[] args) {
		SpringApplication.run(TipsyboyMallApplication.class, args);
	}

}
