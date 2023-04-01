package com.codeiy.monitor;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.codeiy.dto.CrawlerMetrics;
import com.codeiy.mapper.CrawlDataMapper;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * 实现了Spring Boot Actuator的健康指标接口的类
 */
@Component
@RequiredArgsConstructor
public class CrawlerHealthIndicator extends AbstractHealthIndicator {
    private final MeterRegistry meterRegistry;
    private final CrawlDataMapper crawlDataMapper;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        CrawlerMetrics metrics = CrawlerMetrics.getInstance();

        builder.up()
                .withDetail("total_requests", metrics.getTotalRequests())
                .withDetail("visited_url_size", metrics.getTotalErrors())
                .withDetail("crawl_data_count", crawlDataMapper.selectCount(Wrappers.emptyWrapper()))
                .withDetail("content_parse_success_count", meterRegistry.counter("crawler.content_parse.success").count())
                .withDetail("content_classification_success_count", meterRegistry.counter("crawler.content_classification.success").count())
                .withDetail("data_save_success_count", meterRegistry.counter("crawler.data_save.success").count())
                .build();
    }
}
