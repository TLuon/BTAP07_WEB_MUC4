package vn.iotstar.service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
@Service public class CustomUserDetailsService implements UserDetailsService {
 private final UserRepository users; public CustomUserDetailsService(UserRepository users){this.users=users;}
 public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { User u=users.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản")); return org.springframework.security.core.userdetails.User.withUsername(u.getEmail()).password(u.getPassword()).authorities(new SimpleGrantedAuthority(u.getRole()==1?"ROLE_ADMIN":"ROLE_USER")).disabled(u.getStatus()!=1).build(); }
}
