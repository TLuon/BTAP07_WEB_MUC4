package vn.iotstar.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;

public interface IStorageService {
    void init();
    void store(MultipartFile file, String storeFilename);
    Path load(String filename);
    Resource loadAsResource(String filename);
    void delete(String storeFilename);
    String getSorageFilename(MultipartFile file, String id);
    String getStorageFilename(MultipartFile file, String id);
}
