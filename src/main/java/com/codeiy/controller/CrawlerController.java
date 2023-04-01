package com.codeiy.controller;


import com.codeiy.dto.CrawlProperties;
import com.codeiy.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 爬虫控制器
 */
@Slf4j
@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
public class CrawlerController {
    private final CrawlerService crawlerService;

    @PostMapping("/start")
    public ResponseEntity<?> startCrawler(@RequestBody CrawlProperties crawlProperties) {
        crawlerService.startCrawler(crawlProperties);
        return ResponseEntity.ok().build();
    }

    /**
     * 停止爬虫任务
     *
     * @return ResponseEntity
     */
    @PostMapping("/stop")
    public ResponseEntity<?> stopCrawlerTask() {
        return ResponseEntity.ok().build();
    }

    /**
     * 查询爬虫任务状态
     *
     * @return ResponseEntity
     */
    @GetMapping("/status")
    public ResponseEntity<?> getCrawlerTaskStatus() {
        return ResponseEntity.ok().build();
    }

    /**
     * 查询爬虫任务结果
     *
     * @return ResponseEntity
     */
    @GetMapping("/result")
    public ResponseEntity<?> getCrawlerTaskResult() {
        return ResponseEntity.ok().build();
    }

}
