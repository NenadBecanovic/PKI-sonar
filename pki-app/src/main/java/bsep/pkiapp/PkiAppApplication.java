package bsep.pkiapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PkiAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PkiAppApplication.class, args);
	}

}
