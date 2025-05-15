package com.example.pal.dto.content;

import lombok.Data;


@Data
public class ContentDTO {
    private Long id;
    private String type;
    private String url;
    private Long courseId;
}
