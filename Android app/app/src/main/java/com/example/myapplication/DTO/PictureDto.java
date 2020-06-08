package com.example.myapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PictureDto {

    String pictureName;
    byte[] picture;
}
