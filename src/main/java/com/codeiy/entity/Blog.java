package com.codeiy.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;


@TableName("blog")
@Data
public class Blog {
    /**
     * 博客id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 博客标题
     */
    @TableField("title")
    private String title;
    /**
     * 博客链接
     */
    @TableField("url")
    private String url;
    /**
     * 发布时间
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;
    /**
     * 阅读数
     */
    @TableField("read_count")
    private Integer readCount;
    /**
     * 点赞数
     */
    @TableField("like_count")
    private Integer likeCount;
}

