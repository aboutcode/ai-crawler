package com.codeiy.service;
    
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeiy.entity.Blog;
import com.codeiy.mapper.BlogMapper;
import org.springframework.stereotype.Service;


@Service
public class BlogService extends ServiceImpl<BlogMapper, Blog> {

}
