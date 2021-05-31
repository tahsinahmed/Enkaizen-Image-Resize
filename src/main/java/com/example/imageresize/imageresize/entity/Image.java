package com.example.imageresize.imageresize.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "image_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "thumbnail")
    private String thumbnail;

    public Image(String fileName, String thumbnail) {
        this.fileName = fileName;
        this.thumbnail = thumbnail;
    }
}
