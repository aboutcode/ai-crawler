# 让ChatGPT来帮你写代码吧

## 背景说明
由ChatGPT4辅助编写的爬虫程序，能够根据当前互联网的科技博客内容，分析当前技术的流行趋势。

目前已经实现的逻辑：
1. 抓取科技博客网站的首页信息，只要提取首页的链接和链接的描述，把这些数据存入数据库即可。
2. 使用OkHttp3框架来发起请求获取首页数据，并用Jsoup框架分析首页数据里的链接信息。
3. 使用mybatisplus框架将这些数据存储到数据库。


## 创建项目
>> 创建Maven工程，工程名称为ai-crawler,group-id为com.codeiy，版本号为1.0.0。父工程为spring-boot-starter-parent，版本2.7.10。依赖spring-boot-starter-actuator，spring-boot-starter-webflux，spring-boot-devtools，mysql-connector-j，lombok


