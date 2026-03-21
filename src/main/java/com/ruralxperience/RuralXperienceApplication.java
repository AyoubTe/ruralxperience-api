package com.ruralxperience;

import com.ruralxperience.config.StorageProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@OpenAPIDefinition(
        info = @Info(
                title = "RuralXperience",
                version = "1.0",
                description = "API documentation for RuralXperience"
        )
)

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableConfigurationProperties(StorageProperties.class)
public class RuralXperienceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuralXperienceApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner generateHash() {
//        return args -> {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            System.out.println("COPY THIS HASH: " + encoder.encode("Admin@2026!"));
//        };
//    }
}
