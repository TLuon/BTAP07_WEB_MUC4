package vn.iotstar.controller;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.entity.User;
import vn.iotstar.repository.*;
import vn.iotstar.service.AuthService;
@Controller public class WebController {
 private final ProductRepository products; private final UserRepository users; private final AuthService auth;
 public WebController(ProductRepository p,UserRepository u,AuthService a){products=p;users=u;auth=a;}
 @GetMapping({"/","/home"}) public String home(Model m){m.addAttribute("products",products.findAll(PageRequest.of(0,8,Sort.by("productId").descending())).getContent());return "web/home";}
 @GetMapping("/products") public String productList(@RequestParam(defaultValue="") String keyword,@RequestParam(defaultValue="1") int page,Model m){m.addAttribute("data",products.findByProductNameContainingIgnoreCaseAndStatus(keyword,1,PageRequest.of(Math.max(0,page-1),9,Sort.by("productId").descending())));m.addAttribute("keyword",keyword);return "web/product-list";}
 @GetMapping("/products/{id}") public String detail(@PathVariable int id,Model m){m.addAttribute("product",products.findById(id).orElseThrow());return "web/product-detail";}
 @GetMapping("/login") public String login(){return "auth/login";}
 @GetMapping("/register") public String register(){return "auth/register";}
 @PostMapping("/register") public String registerPost(@RequestParam String username,@RequestParam String email,@RequestParam String password,@RequestParam String fullname,RedirectAttributes ra){try{User u=auth.register(username,email,password,fullname);ra.addFlashAttribute("email",u.getEmail());ra.addFlashAttribute("demoOtp",u.getOtpCode());return "redirect:/verify?email="+u.getEmail();}catch(IllegalArgumentException e){ra.addFlashAttribute("error",e.getMessage());return "redirect:/register";}}
 @GetMapping("/verify") public String verify(){return "auth/verify";}
 @PostMapping("/verify") public String verifyPost(@RequestParam String email,@RequestParam String otp,RedirectAttributes ra){var u=users.findByEmailIgnoreCase(email);if(u.isPresent()&&auth.verify(u.get(),otp)){auth.activate(u.get());ra.addFlashAttribute("message","Kích hoạt tài khoản thành công. Hãy đăng nhập.");return "redirect:/login";}ra.addFlashAttribute("error","OTP không đúng hoặc đã hết hạn.");return "redirect:/verify?email="+email;}
 @GetMapping("/forgot") public String forgot(){return "auth/forgot";}
 @PostMapping("/forgot") public String forgotPost(@RequestParam String email,RedirectAttributes ra){var u=users.findByEmailIgnoreCase(email);if(u.isEmpty()){ra.addFlashAttribute("error","Email không tồn tại.");return "redirect:/forgot";}auth.setOtp(u.get());users.save(u.get());ra.addFlashAttribute("demoOtp",u.get().getOtpCode());return "redirect:/reset?email="+email;}
 @GetMapping("/reset") public String reset(){return "auth/reset";}
 @PostMapping("/reset") public String resetPost(@RequestParam String email,@RequestParam String otp,@RequestParam String password,RedirectAttributes ra){var u=users.findByEmailIgnoreCase(email);if(u.isPresent()&&auth.verify(u.get(),otp)){auth.resetPassword(u.get(),password);ra.addFlashAttribute("message","Đặt lại mật khẩu thành công.");return "redirect:/login";}ra.addFlashAttribute("error","OTP không đúng hoặc đã hết hạn.");return "redirect:/reset?email="+email;}
 @GetMapping("/profile") public String profile(@AuthenticationPrincipal UserDetails p,Model m){m.addAttribute("profile",users.findByEmailIgnoreCase(p.getUsername()).orElseThrow());return "web/profile";}
 @PostMapping("/profile") public String profilePost(@AuthenticationPrincipal UserDetails p,@RequestParam String fullname,@RequestParam(required=false) String phone,RedirectAttributes ra){User u=users.findByEmailIgnoreCase(p.getUsername()).orElseThrow();u.setFullname(fullname);u.setPhone(phone);users.save(u);ra.addFlashAttribute("message","Đã cập nhật hồ sơ.");return "redirect:/profile";}
}
