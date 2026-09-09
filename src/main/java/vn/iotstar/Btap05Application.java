package vn.iotstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Btap05Application extends SpringBootServletInitializer {
    public static void main(String[] args) { SpringApplication.run(Btap05Application.class, args); }
    @Override protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) { return builder.sources(Btap05Application.class); }
}
