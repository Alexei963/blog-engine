package com.example.blog.service;

import com.example.blog.api.response.ResultAndErrorsResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FilenameUtils;
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
    String separator = File.separator;
    if (image != null) {
      newPath
          .append(path)
          .append(separator)
          .append(RandomString.make(2))
          .append(separator)
          .append(RandomString.make(2))
          .append(separator)
          .append(RandomString.make(2));
      File uploadDir = new File(newPath.toString());
      if (!uploadDir.exists() && uploadDir.mkdirs()) {
        String extension = FilenameUtils.getExtension(image.getOriginalFilename());
        assert extension != null;
        String imageName = RandomString.make(5)
            .concat(".")
            .concat(extension);
        uploadedImage = new File(newPath + separator + imageName);
        BufferedImage oldImage = ImageIO.read(image.getInputStream());
        BufferedImage newImage = Scalr.resize(oldImage,
            Method.AUTOMATIC, Mode.AUTOMATIC, imageWidth);
        ImageIO.write(newImage, extension, uploadedImage);
      }
    }
    assert uploadedImage != null;
    return separator.concat(uploadedImage.getPath());
  }

  public ResultAndErrorsResponse imageUploadErrors(MultipartFile image) {
    Map<String, String> errors = new HashMap<>();
    String extension = FilenameUtils.getExtension(image.getOriginalFilename());
    if (image.getSize() > MAX_IMAGE_SIZE) {
      errors.put("image size", "Размер файла превышает допустимый размер");
    }
    assert extension != null;
    if (!extension.equals("jpg") && !extension.equals("png")) {
      errors.put("image format", "Неверный формат изображения");
    }
    ResultAndErrorsResponse resultAndErrorsResponse = new ResultAndErrorsResponse();
    resultAndErrorsResponse.setErrors(errors);
    return resultAndErrorsResponse;
  }
}
