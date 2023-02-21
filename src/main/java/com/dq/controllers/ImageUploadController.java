package com.dq.controllers;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageUploadController {
    @GetMapping("getImage/{photo}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("photo") String photo){
        if(!photo.equals("") || photo != null){
            try {
                Path fileName = Paths.get("uploads/",photo);
                byte[] buffer = Files.readAllBytes(fileName);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);

                return ResponseEntity.ok().contentLength(buffer.length).contentType(MediaType.parseMediaType("image/png")).body(byteArrayResource);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }











}
