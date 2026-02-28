package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ExperienceShareDTO {
    private Long productId;

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题不能超过100字")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(max = 2000, message = "内容不能超过2000字")
    private String content;

    private List<String> images;
}
