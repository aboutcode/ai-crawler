package com.codeiy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("website")
public class WebSite {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 网站名称
     */
    private String siteName;

    /**
     * 入口链接
     */
    private String siteEntry;

    /**
     * 是否已爬取
     */
    private Boolean crawleFlag;

    /**
     * 上次爬取时间
     */
    private Date lastCrawledTime;
}
