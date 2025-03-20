package com.criticalblunder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.criticalblunder",
        "config",
        "security",
        "mapper"
})
public class CriticalBlunderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CriticalBlunderApplication.class, args);
	}

}
