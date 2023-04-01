package com.codeiy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page implements Serializable {
    private String url;
    private String title;
    private String content;
    private String category;
    private Date timestamp;
}
