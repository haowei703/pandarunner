package com.shiroha.pandarunner.service.impl;

import com.shiroha.pandarunner.domain.vo.FileResponse;
import com.shiroha.pandarunner.exception.InternalServiceException;
import com.shiroha.pandarunner.exception.NotSupportException;
import com.shiroha.pandarunner.service.FileService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {

    /**
     * 上传路径
     */
    @Value("${file.upload.dir:./src/main/resources/upload}")
    private String uploadDir;

    // 初始化时获取 upload 目录的绝对路径，防止相对路径问题
    private Path uploadRootPath;

    // 合法文件名正则表达式
    private static final Pattern VALID_FILENAME =
            Pattern.compile("^[a-zA-Z0-9_-]+\\.[a-zA-Z0-9]{1,5}$");

    @PostConstruct
    public void init() throws IOException {
        // 解析为绝对路径，并标准化（消除 ../ 等相对路径）
        uploadRootPath = Paths.get(uploadDir).toRealPath();

        // 确保上传目录存在
        Files.createDirectories(uploadRootPath);
    }

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return 包含图片URL和其他信息的FileResponse
     */
    @Override
    public FileResponse uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new NotSupportException("上传的文件不能为空");
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new NotSupportException("只允许上传图片文件");
        }

        // 获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成安全的唯一文件名（替换UUID中的连字符）
        String safeFileName = UUID.randomUUID().toString().replace("-", "_") + extension;

        // 构建并验证文件路径
        Path filePath = uploadRootPath.resolve(safeFileName).normalize();

        // 保存文件
        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new InternalServiceException("文件写入失败", e);
        }

        // 返回文件响应（使用相对URL，实际部署时可能需要调整为完整URL）
        return FileResponse.builder()
                .fileName(safeFileName)
                .url("/file/" + safeFileName)
                .size(file.getSize())
                .contentType(contentType)
                .build();
    }

    /**
     * 根据文件名获取图片路径
     *
     * @param fileName 文件名
     * @return 图片的绝对路径
     */
    @Override
    public String getImagePath(String fileName) {
        // 验证文件名格式
        if (!isValidFileName(fileName)) {
            throw new NotSupportException("非法的文件名: " + fileName);
        }

        // 构建并验证文件路径
        Path filePath = uploadRootPath.resolve(fileName).normalize();
        if (!isPathInAllowedDirectory(filePath)) {
            throw new NotSupportException("非法的文件路径: " + fileName);
        }

        return filePath.toString();
    }

    /**
     * 验证文件名是否合法
     */
    private boolean isValidFileName(String fileName) {
        return VALID_FILENAME.matcher(fileName).matches();
    }

    /**
     * 验证路径是否在允许的目录内
     */
    private boolean isPathInAllowedDirectory(Path path) {
        try {
            // 获取绝对路径并验证是否以uploadRootPath开头
            Path absolutePath = path.toRealPath();
            return absolutePath.startsWith(uploadRootPath);
        } catch (IOException e) {
            // 如果路径不存在或无法解析，视为非法路径
            return false;
        }
    }
}