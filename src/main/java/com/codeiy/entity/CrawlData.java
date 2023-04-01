package com.codeiy.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("crawl_data")
public class CrawlData {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long siteId;

    private String url;

    private String title;

    private String content;

    private String category;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public CrawlData() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public CrawlData(Long siteId, String url, String title, String content, String category) {
        this.siteId = siteId;
        this.url = url;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public void update(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.updateTime = LocalDateTime.now();
    }
}
