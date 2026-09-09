package vn.iotstar.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
@Configuration class SampleDataConfig {
 @Bean CommandLineRunner sampleUsers(UserRepository users,PasswordEncoder encoder){return a->{if(users.findByEmailIgnoreCase("admin@iotstar.vn").isEmpty()){User u=new User();u.setUsername("admin");u.setEmail("admin@iotstar.vn");u.setFullname("Quản trị viên");u.setPassword(encoder.encode("Admin@123"));u.setRole(1);u.setStatus(1);users.save(u);}if(users.findByEmailIgnoreCase("user@iotstar.vn").isEmpty()){User u=new User();u.setUsername("user");u.setEmail("user@iotstar.vn");u.setFullname("Người dùng mẫu");u.setPassword(encoder.encode("User@123"));u.setRole(0);u.setStatus(1);users.save(u);}};}
}
