package com.example.blog.api.response;

import com.example.blog.dto.TagDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Tag Response.
 */

@Data
public class TagResponse {

  private List<TagDto> tags = new ArrayList<>();
}
