package vn.iotstar.service;
import java.time.*;
import java.util.Random;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
@Service public class AuthService {
 private final UserRepository users; private final PasswordEncoder encoder;
 public AuthService(UserRepository users, PasswordEncoder encoder){this.users=users;this.encoder=encoder;}
 public User register(String username,String email,String password,String fullname){ if(users.findByEmailIgnoreCase(email).isPresent()) throw new IllegalArgumentException("Email đã tồn tại."); User u=new User();u.setUsername(username);u.setEmail(email);u.setPassword(encoder.encode(password));u.setFullname(fullname);u.setRole(0);u.setStatus(0);setOtp(u);return users.save(u); }
 public User createByAdmin(String username,String email,String password,String fullname,String phone,int role,int status){if(users.findByEmailIgnoreCase(email).isPresent())throw new IllegalArgumentException("Email đã tồn tại.");User u=new User();u.setUsername(username);u.setEmail(email);u.setPassword(encoder.encode(password));u.setFullname(fullname);u.setPhone(phone);u.setRole(role);u.setStatus(status);return users.save(u);}
 public void setOtp(User u){u.setOtpCode(String.valueOf(100000+new Random().nextInt(900000)));u.setOtpExpiry(java.util.Date.from(Instant.now().plus(Duration.ofMinutes(10))));}
 public boolean verify(User u,String otp){return u.getOtpCode()!=null&&u.getOtpCode().equals(otp)&&u.getOtpExpiry()!=null&&u.getOtpExpiry().after(new java.util.Date());}
 public void activate(User u){u.setStatus(1);u.setOtpCode(null);u.setOtpExpiry(null);users.save(u);}
 public void resetPassword(User u,String raw){u.setPassword(encoder.encode(raw));u.setOtpCode(null);u.setOtpExpiry(null);users.save(u);}
}
