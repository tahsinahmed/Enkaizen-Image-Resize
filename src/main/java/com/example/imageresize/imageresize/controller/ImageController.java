package com.example.imageresize.imageresize.controller;

import com.example.imageresize.imageresize.entity.Image;
import com.example.imageresize.imageresize.repository.ImageRepository;
import com.example.imageresize.imageresize.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class ImageController {

    @Autowired
    ImageService imageService;

    @PostMapping("/add-image")
    public ResponseEntity<?> createImage(@RequestParam("image") MultipartFile multipartFile) {
        return ResponseEntity.ok().body(imageService.saveImage(multipartFile));
    }

    @GetMapping(value = "/all-images")
    public ResponseEntity<?> getImages() throws IOException {
        return ResponseEntity.ok().body(imageService.getAllImages());
    }

    @GetMapping("/get-image")
    public ResponseEntity<?> imagesArray(@RequestParam(name = "thumbnail") String thumbnail) throws IOException {
        return ResponseEntity.ok().body(imageService.getImageByName(thumbnail));
    }
}
