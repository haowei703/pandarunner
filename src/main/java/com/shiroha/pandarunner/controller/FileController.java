package com.shiroha.pandarunner.controller;

import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.domain.vo.FileResponse;
import com.shiroha.pandarunner.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Tag(name = "通用文件上传&下载模块")
@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public R<FileResponse> upload(@RequestParam("file") MultipartFile file) {
        // 直接调用服务层方法，不捕获异常
        FileResponse response = fileService.uploadImage(file);
        return R.ok(response);
    }

    @Operation(summary = "下载文件")
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileName) throws IOException {
        // 获取文件路径
        String filePath = fileService.getImagePath(fileName);
        Path path = Paths.get(filePath);

        // 加载文件资源
        Resource resource = new UrlResource(path.toUri());

        // 设置响应头（根据文件类型动态设置 Content-Type）
        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
