package com.shiroha.pandarunner.service;

import com.shiroha.pandarunner.domain.vo.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传图片
     * @param file 图片文件
     * @return 包含图片URL和其他信息的Map
     */
    FileResponse uploadImage(MultipartFile file);

    /**
     * 根据文件名获取图片路径
     * @param fileName 文件名
     * @return 图片的绝对路径
     */
    String getImagePath(String fileName);

}
