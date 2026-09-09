package vn.iotstar.config;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
@Configuration public class WebConfig implements WebMvcConfigurer {
 @Value("${app.upload-dir:uploads}") String uploadDir;
 public void addResourceHandlers(ResourceHandlerRegistry r){ r.addResourceHandler("/uploads/**").addResourceLocations(Paths.get(uploadDir).toAbsolutePath().toUri().toString()); }
}
