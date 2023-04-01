package com.codeiy.dto;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 记录爬虫任务状态、爬取数据量、爬虫运行时间等指标信息。
 */
public class CrawlerMetrics {

    private static CrawlerMetrics instance = new CrawlerMetrics();

    private Date startTime;
    private Date endTime;
    private AtomicInteger totalRequests = new AtomicInteger(0);
    private AtomicInteger totalResponses = new AtomicInteger(0);
    private AtomicInteger totalErrors = new AtomicInteger(0);

    private CrawlerMetrics() {}

    public static CrawlerMetrics getInstance() {
        return instance;
    }

    public synchronized void start() {
        startTime = new Date();
    }

    public synchronized void end(boolean success) {
        endTime = new Date();
        if (!success) {
            totalErrors.incrementAndGet();
        }
    }

    public void incrementRequests() {
        totalRequests.incrementAndGet();
    }

    public void incrementResponses() {
        totalResponses.incrementAndGet();
    }

    public void incrementErrors() {
        totalErrors.incrementAndGet();
    }

    public int getTotalRequests() {
        return totalRequests.get();
    }

    public int getTotalResponses() {
        return totalResponses.get();
    }

    public int getTotalErrors() {
        return totalErrors.get();
    }

    public long getDurationMillis() {
        if (startTime == null || endTime == null) {
            return 0L;
        }
        return endTime.getTime() - startTime.getTime();
    }
}
