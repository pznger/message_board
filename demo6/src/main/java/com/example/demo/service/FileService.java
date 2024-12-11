package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    // 设置可通过HTTP访问的目录
    private final Path rootLocation = Paths.get("src/main/resources/static/uploads");

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("文件不能为空");
        }

        // 确保目标目录存在
        Files.createDirectories(rootLocation);

        // 获取文件名
        String filename = file.getOriginalFilename();

        // 保存文件到目标目录
        Path destinationFile = rootLocation.resolve(filename).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile);

        // 返回相对路径（可以用于生成访问URL）
        return "/uploads/" + filename; // 返回相对路径
    }
}
