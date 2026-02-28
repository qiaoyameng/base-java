package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PhotoWallDTO {
    private Long productId;

    @NotBlank(message = "图片URL不能为空")
    private String imageUrl;

    @Size(max = 200, message = "描述不能超过200字")
    private String description;
}
