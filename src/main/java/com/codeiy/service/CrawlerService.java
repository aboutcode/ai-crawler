package com.codeiy.service;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.codeiy.dto.CrawlProperties;
import com.codeiy.dto.CrawlerMetrics;
import com.codeiy.entity.CrawlData;
import com.codeiy.entity.WebSite;
import com.codeiy.event.CrawlerErrorEvent;
import com.codeiy.event.CrawlerParsedEvent;
import com.codeiy.event.CrawlerRequestEvent;
import com.codeiy.event.SaveDataEvent;
import com.codeiy.mapper.CrawlDataMapper;
import com.codeiy.mapper.WebSiteMapper;
import com.codeiy.util.HttpUtils;
import com.codeiy.util.UrlUtils;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 爬虫服务
 *
 * @author ChatGPT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerService implements ApplicationListener<CrawlerRequestEvent> {
    private final HttpUtils httpUtils;
    private final WebSiteMapper webSiteMapper;
    private final CrawlDataMapper crawlDataMapper;
    private final MeterRegistry meterRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;
    /**
     * 待采集的队列链接
     */
    private final Queue<String> queue = new ConcurrentLinkedQueue<>();
    /**
     * 已采集的链接集合
     */
    private final Set<String> visitedUrls = new ConcurrentHashSet<>();

    public void startCrawler(CrawlProperties crawlProperties) {
        CrawlerMetrics crawlerMetrics = CrawlerMetrics.getInstance();
        crawlerMetrics.start();
        WebSite crawlSite = webSiteMapper.selectById(crawlProperties.getSiteId());
        String seedUrl = crawlSite.getSiteEntry();
        visitedUrls.add(seedUrl);
        CrawlData source = new CrawlData();
        source.setSiteId(crawlProperties.getSiteId());
        source.setUrl(seedUrl);
        CrawlerRequestEvent event = new CrawlerRequestEvent(source, seedUrl);
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void onApplicationEvent(CrawlerRequestEvent event) {
        if (!(event.getSource() instanceof CrawlData)) {
            sendExceptionEvent("数据源类型错误：" + event);
            return;
        }
        CrawlData source = (CrawlData) event.getSource();
        httpUtils.newCall(event.getUrl()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendExceptionEvent(String.format("Request failed: %s", e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    sendExceptionEvent(String.format("Request failed: %s", response));
                    return;
                }
                ResponseBody body = response.body();
                if (body == null) {
                    sendExceptionEvent("Response body is null");
                    return;
                }
                String html = body.string();
                Document doc = Jsoup.parse(html);

                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String url = link.absUrl("href");
                    if (UrlUtils.isValid(url) && !visitedUrls.contains(url)) {
                        queue.add(url);
                        visitedUrls.add(url);
                    }
                }
                String title = doc.title();
                String content = doc.body().text();
                source.setTitle(title);
                source.setContent(content);
                meterRegistry.counter("crawler.content_parse.success").increment();
                // 发布抓取成功事件
                applicationEventPublisher.publishEvent(new CrawlerParsedEvent(source, title, content));

                // TODO: 抓取下一个链接优化，要控制链接的请求数
                String nextUrl = queue.poll();
                if (StrUtil.isNotBlank(nextUrl)) {
                    CrawlData nextSource = new CrawlData();
                    nextSource.setSiteId(source.getSiteId());
                    nextSource.setUrl(nextUrl);
                    applicationEventPublisher.publishEvent(new CrawlerRequestEvent(nextSource, nextUrl));
                }
            }
        });
    }

    private void sendExceptionEvent(String message) {
        log.error(message);
        CrawlerErrorEvent errorEvent = new CrawlerErrorEvent(this, new RuntimeException(message));
        applicationEventPublisher.publishEvent(errorEvent);
    }

    @EventListener
    public void handleParsedEvent(CrawlerParsedEvent event) {
        if (!(event.getSource() instanceof CrawlData)) {
            sendExceptionEvent("数据源类型错误：" + event);
            return;
        }

        CrawlData source = (CrawlData) event.getSource();
        String title = event.getTitle();
        String content = event.getContent();

        // TODO: 使用hanlp进行分类
        String category = "Java";
        source.setCategory(category);
        meterRegistry.counter("crawler.content_classification.success").increment();

        // 发布存储事件
        SaveDataEvent saveDataEvent = new SaveDataEvent(source, category);
        applicationEventPublisher.publishEvent(saveDataEvent);
    }

    /**
     * 获取到爬虫请求的相关信息，例如URL、请求头、代理等等，并进行相关的处理。
     *
     * @param event
     */
    @EventListener
    public void onCrawlerRequestEvent(SaveDataEvent event) {
        if (!(event.getSource() instanceof CrawlData)) {
            sendExceptionEvent("数据源类型错误：" + event);
            return;
        }

        CrawlData crawlData = (CrawlData) event.getSource();
        QueryWrapper<CrawlData> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(CrawlData::getUrl, crawlData.getUrl());
        if (crawlDataMapper.selectCount(queryWrapper) > 1) {
            sendExceptionEvent("链接已存在多个：" + crawlData.getUrl());
            return;
        }
        CrawlData old = crawlDataMapper.selectOne(queryWrapper);
        if (old == null) {
            crawlDataMapper.insert(crawlData);
        } else {
            old.update(crawlData.getTitle(), crawlData.getContent(), crawlData.getCategory());
            crawlDataMapper.updateById(old);
        }
        meterRegistry.counter("crawler.content_store.success").increment();
    }
}
