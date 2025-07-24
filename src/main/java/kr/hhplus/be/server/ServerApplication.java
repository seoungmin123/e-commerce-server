package kr.hhplus.be.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@EnableJpaRepositories(basePackages = "kr.hhplus.be.server")
@EntityScan(basePackages = "kr.hhplus.be.server")
@SpringBootApplication(scanBasePackages = "kr.hhplus.be.server")
public class ServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner debugContext(ApplicationContext context) {
		return args -> {
			System.out.println("============== BEANS ==============");
			Arrays.stream(context.getBeanDefinitionNames())
					.filter(name -> name.contains("coupon"))
					.forEach(System.out::println);
		};
	}
}