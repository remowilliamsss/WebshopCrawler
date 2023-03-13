package ru.egorov.StoreCrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StoreCrawlerApplication {
	public static void main(String[] args) {
		SpringApplication.run(StoreCrawlerApplication.class, args);
	}
}
