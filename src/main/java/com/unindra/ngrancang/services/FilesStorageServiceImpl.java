package com.unindra.ngrancang.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private Path root = Paths.get("src/main/resources/static");
    
    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {
            String randomChars = RandomStringUtils.randomAlphabetic(10);
            String filename = randomChars+"_"+file.getOriginalFilename();
            filename.trim();
            Files.copy(file.getInputStream(), this.root.resolve(filename));
            return filename;
          } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
              throw new RuntimeException("A file of that name already exists.");
            }
      
            throw new RuntimeException(e.getMessage());
          }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
      
            if (resource.exists() || resource.isReadable()) {
              return resource;
            } else {
              throw new RuntimeException("Could not read the file!");
            }
          } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
          }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
    
}
