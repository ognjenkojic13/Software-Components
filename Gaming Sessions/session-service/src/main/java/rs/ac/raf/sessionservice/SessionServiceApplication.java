package rs.ac.raf.sessionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class SessionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SessionServiceApplication.class, args);
    }
}
