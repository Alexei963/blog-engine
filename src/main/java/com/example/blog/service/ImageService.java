package com.example.blog.service;

import com.example.blog.api.response.ResultAndErrorsResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import net.bytebuddy.utility.RandomString;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Image Service.
 */

@Service
public class ImageService {

  private static final int MAX_IMAGE_SIZE = 5242880; // 5 мегабайт

  public String getImagePath(MultipartFile image, String path, int imageWidth) throws IOException {
    File uploadedImage = null;
    StringBuilder newPath = new StringBuilder();
    if (image != null) {
      newPath
          .append(path)
          .append("/")
          .append(RandomString.make(2))
          .append("/")
          .append(RandomString.make(2))
          .append("/")
          .append(RandomString.make(2));
      File uploadDir = new File(newPath.toString());
      if (!uploadDir.exists()) {
        if (uploadDir.mkdirs()) {
          String imageType = Objects.requireNonNull(
              image.getOriginalFilename()).split("\\.")[1];
          String imageName = RandomString.make(5)
              + "."
              + imageType;
          uploadedImage = new File(newPath + "/" + imageName);
          BufferedImage oldImage = ImageIO.read(image.getInputStream());
          BufferedImage newImage = Scalr.resize(oldImage,
              Method.ULTRA_QUALITY, Mode.FIT_TO_WIDTH, imageWidth);
          ImageIO.write(newImage, imageType, uploadedImage);
        }
      }
    }
    assert uploadedImage != null;
    return uploadedImage.getPath();
  }

  public ResultAndErrorsResponse imageUploadErrors(MultipartFile image) {
    Map<String, String> errors = new HashMap<>();
    ResultAndErrorsResponse postResponse = new ResultAndErrorsResponse();
    if (image != null) {
      if (image.getSize() > MAX_IMAGE_SIZE) {
        errors.put("image size", "Размер файла превышает допустимый размер");
      }
      if (!(Objects.requireNonNull(image.getOriginalFilename()).endsWith(".jpg"))
          && !(image.getOriginalFilename().endsWith(".png"))) {
        errors.put("image format", "Неверный формат изображения");
      }
    }
    postResponse.setErrors(errors);
    return postResponse;
  }
}
