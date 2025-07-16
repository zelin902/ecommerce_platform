package com.example.ecommerce.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class UploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        String original = file.getOriginalFilename();
        String ext = original.substring(original.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + ext;

        file.transferTo(new File(dir.getAbsolutePath(), fileName));

        String url = "http://localhost:8081/uploads/" + fileName;
        return Collections.singletonMap("url", url);
    }
}