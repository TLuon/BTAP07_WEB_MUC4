package vn.iotstar.service;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service public class UploadService {
 private final Path root;
 public UploadService(@Value("${app.upload-dir:uploads}") String dir) throws IOException { root=Paths.get(dir).toAbsolutePath().normalize(); Files.createDirectories(root); }
 public String save(MultipartFile file) throws IOException { if(file==null||file.isEmpty()) return null; String name=Paths.get(file.getOriginalFilename()).getFileName().toString(); String ext=name.contains(".")?name.substring(name.lastIndexOf('.')):""; String saved=UUID.randomUUID()+ext; Files.copy(file.getInputStream(),root.resolve(saved),StandardCopyOption.REPLACE_EXISTING); return saved; }
 
 public String save(MultipartFile file, String subDir) throws IOException {
    if(file==null||file.isEmpty()) return null;
    String name=Paths.get(file.getOriginalFilename()).getFileName().toString();
    String ext=name.contains(".")?name.substring(name.lastIndexOf('.')):"";
    String saved=UUID.randomUUID()+ext;
    Path subPath = root.resolve(subDir);
    Files.createDirectories(subPath);
    Files.copy(file.getInputStream(), subPath.resolve(saved), StandardCopyOption.REPLACE_EXISTING);
    return subDir + "/" + saved;
 }

 public void deleteLocal(String name) throws IOException { if(name!=null&&!name.startsWith("http")) Files.deleteIfExists(root.resolve(name).normalize()); }
}
