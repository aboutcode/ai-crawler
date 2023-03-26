package com.codeiy.controller;

import com.codeiy.crawler.CrawlerEngine;
import com.codeiy.entity.Blog;
import com.codeiy.entity.WebSite;
import com.codeiy.service.BlogService;
import com.codeiy.service.WebSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private WebSiteService webSiteService;

    /**
     * 获取所有博客
     *
     * @return 博客列表
     */
    @PostMapping("/crawle")
    public boolean crawle() {
        List<WebSite> webSites = webSiteService.list();
        List<Blog> allBlogs = new ArrayList<>();
        for (WebSite webSite : webSites) {
            List<Blog> blogs = CrawlerEngine.getBlogs(webSite.getSiteEntry());
            allBlogs.addAll(blogs);
        }
        blogService.saveBatch(allBlogs);
        return true;
    }
}
