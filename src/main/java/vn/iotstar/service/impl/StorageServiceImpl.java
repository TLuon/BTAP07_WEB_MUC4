package vn.iotstar.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.service.IStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Service
public class StorageServiceImpl implements IStorageService {

    private final Path rootLocation;

    public StorageServiceImpl(@Value("${app.upload-dir:uploads}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        init();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    @Override
    public String getSorageFilename(MultipartFile file, String id) {
        return getStorageFilename(file, id);
    }

    @Override
    public String getStorageFilename(MultipartFile file, String id) {
        if (file == null || file.isEmpty()) return null;
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        String ext = "";
        int index = originalFilename.lastIndexOf('.');
        if (index >= 0) {
            ext = originalFilename.substring(index);
        }
        String prefix = (id != null && !id.trim().isEmpty()) ? id + "_" : "";
        return prefix + UUID.randomUUID().toString() + ext;
    }

    @Override
    public void store(MultipartFile file, String storeFilename) {
        if (file == null || file.isEmpty() || storeFilename == null || storeFilename.isEmpty()) return;
        try {
            Path destinationFile = this.rootLocation.resolve(Paths.get(storeFilename)).normalize().toAbsolutePath();
            if (!destinationFile.getParent().startsWith(this.rootLocation)) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + storeFilename, e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void delete(String storeFilename) {
        try {
            if (storeFilename != null && !storeFilename.isBlank() && !storeFilename.startsWith("http")) {
                Path file = load(storeFilename);
                Files.deleteIfExists(file);
            }
        } catch (IOException e) {
            // Silence exception on delete
        }
    }
}
