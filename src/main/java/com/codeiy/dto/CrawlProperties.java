package com.codeiy.dto;

import lombok.Data;

@Data
public class CrawlProperties {
    private Long siteId;
    private int maxDepth;
    private int maxPages;
    private int maxConcurrency;
}