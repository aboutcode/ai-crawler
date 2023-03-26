package com.codeiy.crawler;

import com.codeiy.entity.Blog;
import com.codeiy.util.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CrawlerEngine {
    
    public static List<Blog> getBlogs(String blogUrl) {
        List<Blog> blogs = new ArrayList<>();
        try {
            String html = HttpUtils.get(blogUrl);
            Document doc = Jsoup.parse(html);
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String url = link.attr("abs:href");
                String title = link.text();
                Blog blog = new Blog();
                blog.setUrl(url);
                blog.setTitle(title);
                blogs.add(blog);
            }
        } catch (IOException e) {
            log.error("Error occurred while getting blogs from URL: {}", blogUrl, e);
        }
        return blogs;
    }
}
