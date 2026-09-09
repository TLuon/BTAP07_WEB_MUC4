package vn.iotstar.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.DispatcherType;

@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  return http.authorizeHttpRequests(a -> a
    .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
    .requestMatchers("/", "/home", "/products/**", "/login", "/register/**", "/forgot/**", "/reset/**", "/uploads/**", "/css/**").permitAll()
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .anyRequest().authenticated())
   .formLogin(f -> f.loginPage("/login").loginProcessingUrl("/login").usernameParameter("email").passwordParameter("password").defaultSuccessUrl("/", true).failureUrl("/login?error=true").permitAll())
   .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true")).build();
 }
}
