package com.example.imageresize.imageresize.service;

import com.example.imageresize.imageresize.entity.Image;
import com.example.imageresize.imageresize.repository.ImageRepository;
import com.example.imageresize.imageresize.utils.BASE64DecodedMultipartFile;
import com.example.imageresize.imageresize.utils.FileUploadUtil;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.util.*;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    public String saveImage(MultipartFile multipartFile) {
        try {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String thumbnailFileName = "thumbnail-" + fileName;
            Image newImage = new Image(fileName, "thumbnail-" + fileName);
            imageRepository.save(newImage);
            String originalUploadDir = "images/original";
            FileUploadUtil.saveFile(originalUploadDir, fileName, multipartFile);
            BufferedImage bi = convertImageToBuffer(multipartFile);
            BufferedImage thumbnailBi = resizeImage(bi, 200, 200);
            MultipartFile thumbnailMultipart = new BASE64DecodedMultipartFile(toByteArray(thumbnailBi, "JPEG"));
            String thumbnailUploadDir = "images/thumbnail";
            FileUploadUtil.saveFile(thumbnailUploadDir, thumbnailFileName, thumbnailMultipart);
            return "File Upload Successful";
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }

    private BufferedImage convertImageToBuffer(MultipartFile multipartFile) throws IOException {
        byte[] imageBytes = multipartFile.getBytes();
        InputStream is = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(is);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .outputFormat("JPEG")
                .outputQuality(1)
                .toOutputStream(outputStream);
        byte[] data = outputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        return ImageIO.read(byteArrayInputStream);
    }

    private byte[] toByteArray(BufferedImage bi, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        return baos.toByteArray();
    }

    public Map getAllImages() throws IOException {
        List<Image> image = imageRepository.findAll();
        Map<String, Object> result = new HashMap<>();
        result.put("images", image);
        return result;
    }

    public Map getImageByName(String thumbnail) throws IOException {
        BufferedImage bImage = ImageIO.read(new File("images/thumbnail/" + thumbnail));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos );
        byte [] data = bos.toByteArray();
        String encodedString = Base64.getEncoder().encodeToString(data);
        Map<String, String> res = new HashMap<>();
        res.put("encodedString", encodedString);
        return res;
    }
}
