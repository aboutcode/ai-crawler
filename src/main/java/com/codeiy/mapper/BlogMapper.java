package com.codeiy.mapper;

import com.codeiy.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}

