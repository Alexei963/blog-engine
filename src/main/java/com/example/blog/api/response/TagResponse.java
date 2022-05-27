package com.example.blog.api.response;

import com.example.blog.dto.TagDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Tag Response.
 */

@Getter
@Setter
public class TagResponse {

  private List<TagDto> tags = new ArrayList<>();
}
