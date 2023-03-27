## 一个完全由ChatGPT写的代码工程
由ChatGPT辅助编写的爬虫程序，能够根据当前互联网的科技博客内容，分析当前技术的流行趋势。


主要功能：
1. 定义爬虫程序的目标科技博客网站，确定需要抓取的页面以及需要提取的信息。这些信息可以包括文章标题、作者、发布日期、摘要、内容等。

2. 使用OkHttp3框架发起HTTP请求获取网页的HTML代码。为了避免被网站屏蔽，需要设置合适的User-Agent和延迟时间等参数。对于大量请求的情况，可以考虑使用代理IP等策略来防止被封。

3. 使用Jsoup框架解析HTML代码，提取需要的信息。可以使用选择器来定位需要的元素，使用API来获取元素的属性和文本内容。需要注意的是，有些网站可能会使用反爬虫技术，比如设置验证码、动态生成页面等，需要根据具体情况进行处理。

4. 使用Mybatis Plus框架将数据存储到数据库中。需要先定义数据表的结构和实体类，然后使用框架提供的API进行数据的增删改查等操作。为了避免重复存储，可以考虑使用主键或唯一索引等机制进行数据的去重。

5. 使用定时任务来周期性地执行爬虫程序，保证数据的及时更新。需要设置合适的执行频率和时间，以避免过度频繁的访问目标网站，导致网站出现异常。

## 详细步骤
### 创建工程
![](https://files.mdnice.com/user/20569/65a85c3b-9828-44f8-bb43-ccc2a636eeae.png)

![](https://files.mdnice.com/user/20569/d22e5b22-c5d3-4429-a1dc-64a4dccb0307.png)

![](https://files.mdnice.com/user/20569/b5986d3b-d068-45fe-bb04-2bf4a667021e.png)

![](https://files.mdnice.com/user/20569/6a9f8bd8-baf7-48af-8cb3-0578ce23608d.png)

![](https://files.mdnice.com/user/20569/77886351-6c55-4e00-a10e-877c8542a17e.png)

### 基础功能（CRUD）
**激动人心的时刻来了**，让ChatGPT生成源码。我们给到AI的信息包括：工程名称ai-crawler，Java版本1.8，依赖：mybatis-plus-boot-starter、okhttp、hutool-all、jsoup，**AI会生成什么样的代码呢**？


![](https://files.mdnice.com/user/20569/5b247663-2cfc-49af-91c6-cf645472ed1c.png)

嗯，中规中矩，知道根据okhttp和jsoup创建两个工具类。

接下来，再给它一点提示，告诉AI具体的数据模型，目前暂定为Blog，由AI来生成具体的增删改查代码。

来看看他的表现：

![](https://files.mdnice.com/user/20569/d80f92c9-c8e0-4d62-9e24-96362126d3e3.png)

![](https://files.mdnice.com/user/20569/abc832f0-336a-4864-9803-10b08b38f836.png)

![](https://files.mdnice.com/user/20569/ad9e50d0-97e4-4eed-b93b-7d8dfb8bf4e7.png)

还不错，生成的Blog实体类，利用了MybatisPlus的注解@TableName；BlogService继承自MybatisPlus的IService。Spring的@Service注解也自觉用上了。

这**代码不比Java初级工程师差**了吧？

### 配置代码及工具类
除了上面的基础逻辑代码，再来看看配置类和配置文件。

#### AICrawlerApplication.java

![](https://files.mdnice.com/user/20569/5484d6fb-de16-4c87-be38-6042db8710a0.png)


#### MybatisPlusConfig.java


![](https://files.mdnice.com/user/20569/7203c86a-2368-41a4-8867-15fd215a150f.png)

#### HomeController.java

![](https://files.mdnice.com/user/20569/7f3ef219-cf3f-4133-b3e0-7ae832b22f07.png)

#### HttpUtil.java

![](https://files.mdnice.com/user/20569/2f3c19f5-02aa-435d-bb50-771852bd7ee3.png)

#### JsoupUtil.java

![](https://files.mdnice.com/user/20569/e0f11cae-83f0-4d6c-907b-b87cc07fa016.png)

#### application.yml

![](https://files.mdnice.com/user/20569/9007c3e9-627b-4bf7-b320-f2aa4ffe332e.png)

#### logback-spring.xml

![](https://files.mdnice.com/user/20569/2f776539-c9d8-49cd-8ac4-db8a28712661.png)

### 代码Review

![](https://files.mdnice.com/user/20569/44704bcf-4197-481e-821e-c6b1182137ec.png)

![](https://files.mdnice.com/user/20569/0301c3d8-dd5b-4f08-93ea-e6a398498254.png)

## 总结

AI会取代程序员吗？也许以后可以，现在应该还不行。我觉得AI更像一个趁手的工具。

![](https://files.mdnice.com/user/20569/16732db8-98a5-4a10-88cd-8bb9d636ec3c.png)
