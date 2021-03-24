# Flash Sale

## 技术架构

![1560083013427](http://ruiimg.hifool.cn/img1560083013427.png)

## 系统架构图

![1560090475333](http://ruiimg.hifool.cn/img1560090475333.png)

## FastDFS

- FastDFS是一个开源的轻量级**分布式文件系统**
- 解决了**大容量存储**和**负载均衡的问题**
- FastDFS为互联网量身定制，充分考虑了冗余备份、负载均衡、线性扩容等机制，并注重高可用、高性能等指标，使用FastDFS很容易搭建一套高性能的文件服务器集群提供文件上传、下载等服务。
- FastDFS 架构包括 Tracker server 和 Storage server。客户端请求 Tracker server 进行文件上传、下载，通过Tracker server 调度最终由 Storage server 完成文件上传和下载。
- Tracker server 作用是负载均衡和调度，通过 Tracker server 在文件上传时可以根据一些策略找到Storage server 提供文件上传服务。可以将 tracker 称为追踪服务器或调度服务器。Storage server 作用是文件存储，客户端上传的文件最终存储在 Storage 服务器上，Storageserver 没有实现自己的文件系统而是利用操作系统的文件系统来管理文件。可以将storage称为存储服务器。

![1559117928459](http://ruiimg.hifool.cn/img1559117928459.png)

### 上传流程

![1559117994668](http://ruiimg.hifool.cn/img1559117994668.png)

- 客户端上传文件后存储服务器将文件 ID 返回给客户端，此文件 ID 用于以后访问该文件的索引信息。文件索引信息包括：组名，虚拟磁盘路径，数据两级目录，文件名。

## 商品发布

### SPU与SKU

- SPU = Standard Product Unit  （标准产品单位）
  - 概念 : SPU 是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。
  - 通俗点讲，属性值、特性相同的货品就可以称为一个 SPU
  - ==同款商品的公共属性抽取==
  - 例如：**华为P30 就是一个 SPU**
- SKU=stock keeping unit( 库存量单位)
  - SKU 即库存进出计量的单位， 可以是以件、盒、托盘等为单位。
  - SKU 是物理上不可分割的最小存货单元。在使用时要根据不同业态，不同管理模式来处理。
  - 在服装、鞋类商品中使用最多最普遍。
  - ==某个库存单位的商品独有属性(某个商品的独有属性)==
  - 例如：**华为P30 红色 64G 就是一个 SKU**

## 缓存

- 变更频率低的数据，如何提升访问速度？
  - 数据做成静态页（商品详情页）
  - 做缓存（Redis）

### 缓存方案

1. 首先访问nginx，我们可以采用缓存的方式，先从nginx本地缓存中获取，获取到直接响应
2. 如果没有获取到，再次访问redis，我们可以从redis中获取数据，如果有 则返回，并缓存到nginx中
3. 如果没有获取到，再次访问mysql，我们从mysql中获取数据，再将数据存储到redis中，返回。
- 而这里面，我们都可以使用LUA脚本嵌入到程序中执行这些查询相关的业务。
- 像多级缓存

![1560738068753](http://ruiimg.hifool.cn/img1560738068753.png)

### OpenResty

- OpenResty(又称：ngx_openresty) 是一个基于 nginx的可伸缩的 Web 平台，由中国人章亦春发起，提供了很多高质量的第三方模块。
- 封装了Nginx，并为Nginx提供了高性能的可扩展程序，使Nginx抗压能力得到了大大的提升，并且提供了Lua脚本的扩展支持
- nginx.conf
  ```nginx
    user  root root;
    worker_processes  1;

    events {
        worker_connections  1024;
    }

    http {
        include       mime.types;
        default_type  application/octet-stream;

        #定义Nginx缓存模块，模块名字叫dis_cache,容量大小128M
        lua_shared_dict dis_cache 128m;

        #限流设置   $binary_remote_addr：根据请求IP进行限流 contentRateLimit:缓存空间名称：10M  每秒允许2个请求
        limit_req_zone $binary_remote_addr zone=contentRateLimit:10m rate=2r/s;

        #根据IP地址来限制，存储内存大小10M
        limit_conn_zone $binary_remote_addr zone=addr:10m;

        #个人IP显示
        limit_conn_zone $binary_remote_addr zone=perip:10m;
        #针对整个服务所有的并发量控制
        limit_conn_zone $server_name zone=perserver:10m; 

        sendfile        on;
        #tcp_nopush     on;

        #keepalive_timeout  0;
        keepalive_timeout  65;

        #gzip  on;

        server {
            listen       80;
        #监听的域名
            server_name  localhost;

        #本地开发地址10.162.133.3
        location /brand {
            limit_conn perip 3;      #单个客户端ip与服务器的连接数．
            limit_conn perserver 5;  #限制与服务器的总连接数
            #同一个IP只允许有2个并发连接
            #limit_conn addr 2;
            #所有以/brand的请求，都将交给  10.162.133.3服务器的18081程序处理.
            proxy_pass http://10.162.133.3:58081;
        }

        #表示所有以 localhost/read_content的请求都由该配置处理
        location /read_content {
            #使用指定限流配置,burst=4表示允许同时有4个并发连接,如果不能同时处理，则会放入队列，等请求处理完成后，再从队列中拿请求
            #nodelay 并行处理所有请求
            limit_req zone=contentRateLimit burst=4 nodelay;
            #content_by_lua_file:所有请求都交给指定的lua脚本处理(/root/lua/read_content.lua)
            content_by_lua_file /usr/local/server/lua65/read_content.lua;
        }

        #表示所有以 localhost/update_content的请求都由该配置处理
        location /update_content {
            #content_by_lua_file:所有请求都交给指定的lua脚本处理(/root/lua/update_content.lua)
            content_by_lua_file /usr/local/server/lua65/update_content.lua;
        }
        }
    }

  ```
- lua脚本
  ```lua
    ngx.header.content_type="application/json;charset=utf8"
    local uri_args = ngx.req.get_uri_args();
    local id = uri_args["id"];  --获取请求路径中的id参数
    --获取本地缓存
    local cache_ngx=ngx.shared.dis_cache;   --加载Nginx缓存模块（需要定义）
    --根据ID 获取本地缓存数据
    local contentCache=cache_ngx:get('content_cache_'..id);

    if contentCache == "" or contentCache == nil then   --nginx缓存中没有，去redis中找
        local redis = require("resty.redis");   -- 加载redis
        local red = redis:new()
        red:set_timeout(2000)
        red:connect("10.177.73.40", 6379)
        local rescontent=red:get("content_"..id);   -- key=content_{id}


        if ngx.null == rescontent then  --redis缓存中没有去数据库中找
            local cjson = require("cjson");
            local mysql = require("resty.mysql");
            local db = mysql:new();
            db:set_timeout(2000)
            local props = {
                host = "10.177.73.40",
                port = 3306,
                database = "changgou_content",
                user = "root",
                password = "123456"
            }
            local res = db:connect(props);
            local select_sql = "select url,pic from tb_content where status ='1' and category_id="..id.." order by sort_order";
            res = db:query(select_sql);
            local responsejson = cjson.encode(res);
            red:set("content_"..id,responsejson);   -- 数据存入redis
            ngx.say(responsejson);
            db:close()
        else
            cache_ngx:set('content_cache_'..id, rescontent, 10*60); --将redis查询的缓存存入到Nginx缓存，缓存10分钟
            ngx.say(rescontent)
        end
        red:close()
    else
        ngx.say(contentCache)

    end

  ```

### Nginx限流

- 一般情况下，首页的并发量是比较大的，即使 有了多级缓存，当用户不停的刷新页面的时候，也是没有必要的，另外如果有恶意的请求 大量达到，也会对系统造成影响。
- 而限流就是保护措施之一。
- 两种限流方式
  - 控制速率
  - 控制并发的连接数

#### 控制速率

- 使用漏桶算法。
  - 漏桶(Leaky Bucket)算法思路很简单,水(请求)先进入到漏桶里,漏桶以一定的速度出水(接口有响应速率),当水流入速度过大会直接溢出(访问频率超过接口响应速率),然后就拒绝请求,可以看出漏桶算法能强行限制数据的传输速率.示意图如下:![1560774438337](http://ruiimg.hifool.cn/img1560774438337.png)
  - 解决雪崩的有效手段之一
- Nginx配置说明
  - binary_remote_addr 是一种key，表示基于 remote_addr(客户端IP) 来做限流，binary_ 的目的是压缩内存占用量。
  - zone：定义共享内存区来存储访问信息， contentRateLimit:10m 表示一个大小为10M，名字为contentRateLimit的内存区域。1M能存储16000 IP地址的访问信息，10M可以存储16W IP地址访问信息。
  - rate 用于设置最大访问速率，rate=10r/s 表示每秒最多处理10个请求。Nginx 实际上以毫秒为粒度来跟踪请求信息，因此 10r/s 实际上是限制：每100毫秒处理一个请求。这意味着，自上一个请求处理完后，若后续100毫秒内又有请求到达，将拒绝处理该请求.我们这里设置成2 方便测试。

#### 并发数量限流

  - 上面例子限制 2r/s，如果有时正常流量突然增大，超出的请求将被拒绝，无法处理突发流量，可以结合 **burst** 参数使用来解决该问题。
    - burst 译为突发、爆发，表示在超过设定的处理速率后能额外处理的请求数,当 rate=10r/s 时，将1s拆成10份，即每100ms可处理1个请求。
    - 此处，**burst=4 **，若同时有4个请求到达，Nginx 会处理第一个请求，剩余3个请求将放入队列，然后每隔500ms从队列中获取一个请求进行处理。若请求数大于4，将拒绝处理多余的请求，直接返回503.
    - 不过，单独使用 burst 参数并不实用。假设 burst=50 ，rate依然为10r/s，排队中的50个请求虽然每100ms会处理一个，但第50个请求却需要等待 50 * 100ms即 5s，这么长的处理时间自然难以接受。
    - 因此，burst 往往结合 nodelay 一起使用。

### canal

- canal可以用来监控mysql数据库数据的变化，从而获得新增数据，或者修改的数据。
- canal是应阿里巴巴存在杭州和美国的双机房部署，存在跨机房同步的业务需求而提出的。
- 阿里系公司开始逐步的尝试基于数据库的日志解析，获取增量变更进行同步，由此衍生出了增量订阅&消费的业务。

![20210222123931](http://ruiimg.hifool.cn/img20210222123931.png)

#### 开启MySql binlog模式（二进制日志）

- 进入mysql容器
- 修改/etc/mysql/mysql.conf.d/mysqld.cnf ![20210222124620](http://ruiimg.hifool.cn/img20210222124620.png)
- 创建账号给canal赋予访问权限
  ```properties
    create user canal@'%' IDENTIFIED by 'canal';
    GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT,SUPER ON *.* TO 'canal'@'%';
    FLUSH PRIVILEGES;
  ```

## Elasticsearch

### IK分词器

<https://github.com/medcl/elasticsearch-analysis-ik/releases>

### Kibana

- Kibana 是一款开源的数据分析和可视化平台，它是 Elastic Stack 成员之一，设计用于和 Elasticsearch 协作。您可以使用 Kibana 对 Elasticsearch 索引中的数据进行搜索、查看、交互操作。您可以很方便的利用图表、表格及地图对数据进行多元化的分析和呈现。
- Kibana 可以使大数据通俗易懂。它很简单，基于浏览器的界面便于您快速创建和分享动态数据仪表板来追踪 Elasticsearch 的实时数据变化。

#### DSL语句

- Query DSL是一个Java开源框架用于构建类型安全的SQL查询语句。采用API代替传统的拼接字符串来构造查询语句。目前Querydsl支持的平台包括JPA,JDO，SQL，Java Collections，RDF，Lucene，Hibernate Search。elasticsearch提供了一整套基于JSON的查询DSL语言来定义查询。
- Query DSL当作是一系列的抽象的查询表达式树(AST)特定查询能够包含其它的查询，(如 bool ), 有些查询能够包含过滤器(如 constant_score), 还有的可以同时包含查询和过滤器 (如 filtered). 都能够从ES支持查询集合里面选择任意一个查询或者是从过滤器集合里面挑选出任意一个过滤器, 这样的话，我们就可以构造出任意复杂（maybe 非常有趣）的查询了。
  - 查询所有索引 `GET /_cat/indices?v`
  - 删除索引库 `DELETE /skuinfo`
  - 新增索引库 `PUT /user`
  - 添加映射 `PUT /user/userinfo/_mappin`
  - 新增文档数据 `PUT /user/userinfo/1`
  - 修改数据
    - 替换 `PUT /user/userinfo/4`
    - 更新 `POST /user/userinfo/4/_update`
  - 删除数据 `DELETE user/userinfo/7`
  - 查询所有数据 `GET /user/_search`
  - 根据id查询 `GET /user/userinfo/4`

### SpringData

- Spring Data是一个用于简化数据库访问，并支持云服务的开源框架。其主要目标是使得对数据的访问变得方便快捷，并支持map-reduce框架和云计算数据服务。 Spring Data可以极大的简化JPA的写法，可以在几乎不用写实现的情况下，实现对数据的访问和操作。除了CRUD外，还包括如分页、排序等一些常用的功能。
- Spring Data的官网：<http://projects.spring.io/spring-data/>

#### SpringData ES

- Spring Data ElasticSearch 基于 spring data API 简化 elasticSearch操作，将原始操作elasticSearch的客户端API 进行封装 。Spring Data为Elasticsearch项目提供集成搜索引擎。Spring Data Elasticsearch POJO的关键功能区域为中心的模型与Elastichsearch交互文档和轻松地编写一个存储库数据访问层。
- 官方网站：http://projects.spring.io/spring-data-elasticsearch/ 

### 数据导入

![1557563491839](http://ruiimg.hifool.cn/img1557563491839.png)

1. 请求search服务,调用数据导入地址
2. 根据注册中心中的注册的goods服务的地址，使用Feign方式查询所有已经审核的Sku
3. 使用SpringData Es将查询到的Sku集合导入到ES中

## Thymeleaf

- hymeleaf提供了一个用于整合Spring MVC的可选模块，在应用开发中，你可以使用Thymeleaf来完全代替JSP或其他模板引擎，如Velocity、FreeMarker等。Thymeleaf的主要目标在于提供一种可被浏览器正确显示的、格式良好的模板创建方式，因此也可以用作静态建模。你可以使用它创建经过验证的XML与HTML模板。相对于编写逻辑或代码，开发者只需将标签属性添加到模板中即可。接下来，这些标签属性就会在DOM（文档对象模型）上执行预先制定好的逻辑。
- 它的特点便是：开箱即用，Thymeleaf允许您处理六种模板，每种模板称为模板模式：
  - XML
  - 有效的XML
  - XHTML
  - 有效的XHTML
  - HTML5
  - 旧版HTML5
- 模板引擎技术：可以基于写好的模板，动态给写好的模板加载数据

## 微服务网关

- 主要功能
  1. 整合各个为服务功能，形成一套系统
  2. 在微服务网关中实现日志统一记录
  3. 实现用户的操作跟踪
  4. 实现限流操作
  5. 用户权限认证操作
- 优点
  - 安全 ，只有网关系统对外进行暴露，微服务可以隐藏在内网，通过防火墙保护。
  - 易于监控。可以在网关收集监控数据并将其推送到外部系统进行分析。
  - 易于认证。可以在网关上进行认证，然后再将请求转发到后端的微服务，而无须在每个微服务中进行认证。
  - 减少了客户端与各个微服务之间的交互次数
  - 易于统一授权。
  - 提供微服务限流功能，可以保护微服务，防止雪崩效应发生
- 实现微服务网关的技术
  - Nginx：Nginx (tengine x) 是一个高性能的[HTTP](https://baike.baidu.com/item/HTTP)和[反向代理](https://baike.baidu.com/item/)
  - Zuul：Zuul 是 Netflix 出品的一个基于 JVM 路由和服务端的负载均衡器。
  - Spring-cloud-gateway：是spring 出品的 基于spring 的网关项目，集成断路器，路径重写，性能比Zuul好。
![20210224111127](http://ruiimg.hifool.cn/img20210224111127.png)

### 网关过滤

- 路由过滤器允许以某种方式修改传入的HTTP请求或传出的HTTP响应。 路径过滤器的范围限定为特定路径。 Spring Cloud Gateway包含许多内置的GatewayFilter工厂。如上图，根据请求路径路由到不同微服务去，这块可以使用Gateway的路由过滤功能实现。
- 过滤器 有 20 多个 实现 类， 包括 头部 过滤器、 路径 类 过滤器、 Hystrix 过滤器 和 变更 请求 URL 的 过滤器， 还有 参数 和 状态 码 等 其他 类型 的 过滤器。
- 内置的过滤器工厂有22个实现类，包括 头部过滤器、路径过滤器、Hystrix 过滤器 、请求URL 变更过滤器，还有参数和状态码等其他类型的过滤器。根据过滤器工厂的用途来划分，可以分为以下几种：Header、Parameter、Path、Body、Status、Session、Redirect、Retry、RateLimiter和Hystrix。

#### 网关过滤配置

- Host路由
    ```properties
        routes:
            - id: changgou_goods_route
                uri: http://localhost:18081
                predicates:
                - Host=cloud.itheima.com**
    ```
- 路径匹配过滤配置
    ```properties
        routes:
                - id: changgou_goods_route
                uri: http://localhost:18081
                predicates:
                - Path=/brand**
    ```
- StripPrefix 过滤配置  所有以/api/brand开头的请求都去掉api给上面的微服务
    ```properties
        routes:
                - id: changgou_goods_route
                uri: http://localhost:18081
                predicates:
                - Path=/api/brand/**
                filters:
                - StripPrefix=1
    ```
- PrefixPath 过滤配置   所有路径添加/brand前缀
    ```properties
        routes:
                - id: changgou_goods_route
                uri: http://localhost:18081
                predicates:
                #- Host=cloud.itheima.com**
                - Path=/**
                filters:
                - PrefixPath=/brand
    ```

#### LoadBalancerClient 路由过滤器(客户端负载均衡) 

- 上面的路由配置每次都会将请求给指定的`URL`处理，但如果在以后生产环境，并发量较大的时候，我们需要根据服务的名称判断来做负载均衡操作，可以使用`LoadBalancerClientFilter`来实现负载均衡调用。`LoadBalancerClientFilter`会作用在url以lb开头的路由，然后利用`loadBalancer`来获取服务实例，构造目标`requestUrl`，设置到`GATEWAY_REQUEST_URL_ATTR`属性中，供`NettyRoutingFilter`使用。
    ```properties
        routes:
                - id: changgou_goods_route
                #uri: http://localhost:18081
                uri: lb://goods
                predicates:
                #- Host=cloud.itheima.com**
                - Path=/**
                filters:
                #- PrefixPath=/brand
                - StripPrefix=1
    ```

### 网关限流

- Nginx抵御第一波并发流量之后，流量依然比较大
- 网关限流保护微服务，防止雪崩
![1557909861570](http://ruiimg.hifool.cn/img1557909861570.png)

#### 令牌桶算法

令牌桶算法是比较常见的限流算法之一，大概描述如下：
1）所有的请求在处理之前都需要拿到一个可用的令牌才会被处理；
2）根据限流大小，设置按照一定的速率往桶里添加令牌；
3）桶设置最大的放置令牌限制，当桶满时、新添加的令牌就被丢弃或者拒绝；
4）请求达到后首先要获取令牌桶中的令牌，拿着令牌才可以进行其他的业务逻辑，处理完业务逻辑之后，将令牌直接删除；
5）令牌桶有最低限额，当桶中的令牌达到最低限额的时候，请求处理完之后将不会删除令牌，以此保证足够的限流

![20210224114651](http://ruiimg.hifool.cn/img20210224114651.png)

(1)引入redis依赖

在changgou-gateway的pom.xml中引入redis的依赖

```xml
<!--redis-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    <version>2.1.3.RELEASE</version>
</dependency>
```

(2)定义KeyResolver

在Applicatioin引导类中添加如下代码，KeyResolver用于计算某一个类型的限流的KEY也就是说，可以通过KeyResolver来指定限流的Key。

我们可以根据IP来限流，比如每个IP每秒钟只能请求一次，在GatewayWebApplication定义key的获取，获取客户端IP，将IP作为key，如下代码：

```java
/***
 * IP限流
 * @return
 */
@Bean(name="ipKeyResolver")
public KeyResolver userKeyResolver() {
    return new KeyResolver() {
        @Override
        public Mono<String> resolve(ServerWebExchange exchange) {
            //获取远程客户端IP
            String hostName = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            System.out.println("hostName:"+hostName);
            return Mono.just(hostName);
        }
    };
}
```

(3)修改application.yml中配置项，指定限制流量的配置以及REDIS的配置，

配置代码如下：

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]': # 匹配所有请求
              allowedOrigins: "*" #跨域处理 允许所有的域
              allowedMethods: # 支持的方法
                - GET
                - POST
                - PUT
                - DELETE
      routes:
            - id: changgou_goods_route
              uri: lb://goods
              predicates:
              - Path=/api/brand**
              filters:
              - StripPrefix=1
              - name: RequestRateLimiter #请求数限流 名字不能随便写 ，使用默认的facatory
                args:
                  key-resolver: "#{@ipKeyResolver}"
                  redis-rate-limiter.replenishRate: 1
                  redis-rate-limiter.burstCapacity: 1

  application:
    name: gateway-web
  #Redis配置
  redis:
    host: 192.168.211.132
    port: 6379

server:
  port: 8001
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:7001/eureka
  instance:
    prefer-ip-address: true
management:
  endpoint:
    gateway:
      enabled: true
    web:
      exposure:
        include: true
```

- 解释：
  - `redis-rate-limiter.replenishRate`是您希望允许用户每秒执行多少请求，而不会丢弃任何请求。这是令牌桶填充的速率
  - `redis-rate-limiter.burstCapacity`是指令牌桶的容量，允许在一秒钟内完成的最大请求数,将此值设置为零将阻止所有请求。
  - key-resolver: "#{@ipKeyResolver}" 用于通过SPEL表达式来指定使用哪一个KeyResolver.
- 如上配置：
  - 表示 一秒内，允许 一个请求通过，令牌桶的填充速率也是一秒钟添加一个令牌。
  - 最大突发状况 也只允许 一秒内有一次请求，可以根据业务来调整。


### JWT令牌

![20210224131523](http://ruiimg.hifool.cn/img20210224131523.png)

- JSON Web Token（JWT）是一个非常轻巧的规范。这个规范允许我们使用JWT在用户和服务器之间传递安全可靠的信息。
- 一个JWT实际上就是一个字符串，它由三部分组成，头部、载荷与签名。
  - 头部（Header）：头部用于描述关于该JWT的最基本的信息，例如其类型以及签名所用的算法等。这也可以被表示成一个JSON对象。使用base64进行编码加密
    ```json
    {"typ":"JWT","alg":"HS256"}
    ```
  - 载荷（playload）：载荷就是存放有效信息的地方。使用base64进行编码加密
    1. 标准中注册的声明（建议但不强制使用）
    2. 公共的声明（不参与校验）
    3. 私有的声明（不参与校验）
  - 签证（signature）校验数据是否被篡改，由三部分组成
    > header (base64后的)
    >
    > payload (base64后的)
    >
    > secret（密钥->盐）
    - 这个部分需要base64加密后的header和base64加密后的payload使用.连接组成的字符串，然后通过header中声明的加密方式进行加盐secret组合加密，然后就构成了jwt的第三部分。
    - 签名=Base64(头)+Base64(载荷)+密钥(盐)

#### JJWT

- JJWT是一个提供端到端的JWT创建和验证的Java库。永远免费和开源(Apache License，版本2.0)，JJWT很容易使用和理解。它被设计成一个以建筑为中心的流畅界面，隐藏了它的大部分复杂性。
- 官方文档：<https://github.com/jwtk/jjwt>

![1562069596308](http://ruiimg.hifool.cn/img1562069596308.png)

## 用户认证

### 认证与授权

- 身份认证
  - 用户身份认证即用户去访问系统资源时系统要求验证用户的身份信息，身份合法方可继续访问。常见的用户身份认证表现形式有：用户名密码登录，指纹打卡等方式。说通俗点，就相当于校验用户账号密码是否正确。
- 用户授权
  - 用户认证通过后去访问系统的资源，系统会判断用户是否拥有访问资源的权限，只允许访问有权限的系统资源，没有权限的资源将无法访问，这个过程叫用户授权。

### 单点登录

- 用户访问的项目中，至少有3个微服务需要识别用户身份，如果用户访问每个微服务都登录一次就太麻烦了，为了提高用户的体验，我们需要实现让用户在一个系统中登录，其他任意受信任的系统都可以访问，这个功能就叫单点登录。
- 单点登录（Single Sign On），简称为 SSO，是目前比较流行的企业业务整合的解决方案之一。 SSO的定义是在多个应用系统中，用户只需要登录一次就可以访问所有相互信任的应用系统  
- 分布式系统要实现单点登录，通常将认证系统独立抽取出来，并且将用户身份信息存储在单独的存储介质，比如： MySQL、Redis，考虑性能要求，通常存储在Redis中，如下图：![1558175040643](http://ruiimg.hifool.cn/img1558175040643.png)
  - 单点登录的特点是： 
    1. 认证系统为独立的系统。 
    2. 各子系统通过Http或其它协议与认证系统通信，完成用户认证。 
    3. 用户身份信息存储在Redis集群。
  - Java中有很多用户认证的框架都可以实现单点登录：
     1. Apache Shiro. 
     2. CAS 
     3. Spring security CAS    

#### Oauth2认证

- OAuth（开放授权）是一个开放标准，允许用户授权第三方移动应用访问他们存储在另外的服务提供者上的信息，而不需要将用户名和密码提供给第三方移动应用或分享他们数据的所有内容，OAuth2.0是OAuth协议的延续版本。
- 认证流程
  - 第三方认证技术方案最主要是解决认证协议的通用标准 问题，因为要实现 跨系统认证，各系统之间要遵循一定的接口协议。
  - OAUTH协议为用户资源的授权提供了一个安全的、开放而又简易的标准。同时，任何第三方都可以使用OAUTH认证服务，任何服务提供商都可以实现自身的OAUTH认证服务，因而OAUTH是开放的。业界提供了OAUTH的多种实现如PHP、JavaScript，Java，Ruby等各种语言开发包，大大节约了程序员的时间，因而OAUTH是简易的。互联网很多服务如Open API，很多大公司如Google，Yahoo，Microsoft等都提供了OAUTH认证服务，这些都足以说明OAUTH标准逐渐成为开放资源授权的标准。
  - Oauth协议目前发展到2.0版本，1.0版本过于复杂，2.0版本已得到广泛应用。
  - 参考：https://baike.baidu.com/item/oAuth/7153134?fr=aladdin
  - Oauth协议：https://tools.ietf.org/html/rfc6749
  - 下边分析一个Oauth2认证的例子，黑马程序员网站使用微信认证的过程：![1562394786067](http://ruiimg.hifool.cn/img1562394786067.png)
- 项目中：![20210224153620](http://ruiimg.hifool.cn/img20210224153620.png)

#### Oauth2授权

- 授权模式
  1. 授权码模式（Authorization Code）[常用]
  2. 隐式授权模式（Implicit） [不常用]
  3. 密码模式（Resource Owner Password Credentials） [常用]
  4. 客户端模式（Client Credentials） [不常用]

##### 传统授权模式

![1562479275302](http://ruiimg.hifool.cn/img1562479275302.png)

- 资源服务器授权流程如上图，客户端先去授权服务器申请令牌，申请令牌后，携带令牌访问资源服务器，资源服务器访问授权服务校验令牌的合法性，授权服务会返回校验结果，如果校验成功会返回用户信息给资源服务器，资源服务器如果接收到的校验结果通过了，则返回资源给客户端。
- 传统授权方法的问题是用户每次请求资源服务，资源服务都需要携带令牌访问认证服务去校验令牌的合法性，并根 据令牌获取用户的相关信息，性能低下。 

##### 公钥私钥授权流程

![20210224161636](http://ruiimg.hifool.cn/img20210224161636.png)

- 传统的授权模式性能低下，每次都需要请求授权服务校验令牌合法性，我们可以利用公钥私钥完成对令牌的加密，如果加密解密成功，则表示令牌合法，如果加密解密失败，则表示令牌无效不合法，合法则允许访问资源服务器的资源，解密失败，则不允许访问资源服务器资源。
  1. 客户端请求认证服务申请令牌
  2. 认证服务生成令牌认证服务采用非对称加密算法，使用私钥生成令牌。
  3. 客户端携带令牌访问资源服务客户端在Http header 中添加： Authorization：Bearer 令牌。
  4. 资源服务请求认证服务校验令牌的有效性资源服务接收到令牌，使用公钥校验令牌的合法性。
  5. 令牌有效，资源服务向客户端响应资源信息

##### 生成私钥和公钥

- Spring Security 提供对JWT的支持，本节我们使用Spring Security 提供的JwtHelper来创建JWT令牌，校验JWT令牌 等操作。 这里JWT令牌我们采用非对称算法进行加密，所以我们要先生成公钥和私钥。

##### 认证开发

![1562481462105](http://ruiimg.hifool.cn/img1562481462105.png)

1. 用户登录，请求认证服务 
2. 认证服务认证通过，生成jwt令牌，将jwt令牌及相关信息写入cookie 
3. 用户访问资源页面，带着cookie到网关 
4. 网关从cookie获取token，如果存在token，则校验token合法性，如果不合法则拒绝访问，否则放行 
5. 用户退出，请求认证服务，删除cookie中的token 

![20210224171924](http://ruiimg.hifool.cn/img20210224171924.png)

- 认证流程![1562486044874](http://ruiimg.hifool.cn/img1562486044874.png)
  - 参数传递
    1. 账号         username=szitheima
    2. 密码         password=szitheima
    3. 授权方式     grant_type=password
  - 请求头传递
    - Basic Base64(客户端ID:客户端密钥) Authorization=Basic Y2hhbmdnb3U6Y2hhbmdnb3U=

### 资源服务授权配置

![1562406193809](http://ruiimg.hifool.cn/img1562406193809.png)

#### 授权配置类

```java
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)//激活方法上的PreAuthorize注解
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //公钥
    private static final String PUBLIC_KEY = "public.key";

    /***
     * 定义JwtTokenStore
     * @param jwtAccessTokenConverter
     * @return
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /***
     * 定义JJwtAccessTokenConverter
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(getPubKey());
        return converter;
    }
    /**
     * 获取非对称加密公钥 Key
     * @return 公钥 Key
     */
    private String getPubKey() {
        Resource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            return null;
        }
    }

    /***
     * Http安全配置，对每个到达系统的http请求链接进行校验
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //所有请求必须认证通过
        http.authorizeRequests()
                //下边的路径放行
                .antMatchers(
                        "/user/add"). //配置地址放行
                permitAll()
                .anyRequest().
                authenticated();    //其他地址需要认证授权
    }
}
```

#### 角色权限控制

- 在每个微服务中，需要获取用户的角色，然后根据角色识别是否允许操作指定的方法，Spring Security中定义了四个支持权限控制的表达式注解，分别是`@PreAuthorize`、`@PostAuthorize`、`@PreFilter`和`@PostFilter`。其中前两者可以用来在方法调用前或者调用后进行权限检查，后两者可以用来对集合类型的参数或者返回值进行过滤。在需要控制权限的方法上，我们可以添加`@PreAuthorize`注解，用于方法执行前进行权限检查，校验用户当前角色是否能访问该方法。

#### 微服务之间认证

- 因为微服务之间并没有传递头文件，所以我们可以定义一个拦截器，每次微服务调用之前都先检查下头文件，将请求的头文件中的令牌数据再放入到header中，再调用其他微服务即可。
- ==各大为服务之间的认证其实就是令牌的传递==
- 用户当前请求的时候对应线程的数据，如果开启了熔断，默认时线程池隔离，会开启新的线程，需要将熔断策略换成信号量隔离，此时不会开启新的线程![20210225153625](http://ruiimg.hifool.cn/img20210225153625.png)

## 订单结算

### 超卖现象

```java
/**
  * 递减库存
  * @param orderItem
  * @return
  */
@Update("UPDATE tb_sku SET num=num-#{num},sale_num=sale_num+#{num} WHERE id=#{skuId} AND num>=#{num}")
int decrCount(OrderItem orderItem);
```

> 使用行锁控制超卖

- 数据库中每条记录都拥有行级锁，此时只能允许一个事务修改该记录，只有等该事务结束后，其他事务才能操作该记录

### 延时队列

![20210317153057](http://ruiimg.hifool.cn/img20210317153057.png)

- 使用延时队列，延时30分钟控制超时未支付订单

## 秒杀

> 秒杀技术实现核心思想是运用**缓存减少数据库瞬间的访问压力**

![20210319124952](http://ruiimg.hifool.cn/img20210319124952.png)

- 把秒杀商品定时存入Redis中，Hash ![20210319162750](http://ruiimg.hifool.cn/img20210319162750.png)
- 抢单时直接去Redis中查询，修改数量，直到抢购完毕再写回数据库中

### 多线程抢单

![1557038616667](http://ruiimg.hifool.cn/img1557038616667.png)

- 在审视秒杀中，操作一般都是比较复杂的，而且并发量特别高，比如，检查当前账号操作是否已经秒杀过该商品，检查该账号是否存在存在刷单行为，记录用户操作日志等。
- 下订单这里，我们一般采用多线程下单，但多线程中我们又需要保证用户抢单的公平性，也就是先抢先下单。我们可以这样实现，用户进入秒杀抢单，如果用户复合抢单资格，只需要记录用户抢单数据，存入队列，多线程从队列中进行消费即可，存入队列采用左压，多线程下单采用右取的方式。


> Spring异步实现

- 要想使用Spring的异步操作，需要先开启异步操作，用`@EnableAsync`注解开启，然后在对应的异步方法上添加注解`@Async`即可。

### 防止秒杀重复排队

- 用户每次抢单的时候，一旦排队，我们设置一个自增值，让该值的初始值为1，每次进入抢单的时候，对它进行递增，如果值>1，则表明已经排队,不允许重复排队,如果重复排队，则对外抛出异常，并抛出异常信息100表示已经正在排队。
- Redis中的`incr`操作
- **Redis是单线程**的，每次操作并发请求只允许有一个请求去操作redis

### 并发超卖问题

- 超卖问题，这里是指多人抢购同一商品的时候，多人同时判断是否有库存，如果只剩一个，则都会判断有库存，此时会导致超卖现象产生，也就是一个商品下了多个订单的现象。![20210322112135](http://ruiimg.hifool.cn/img20210322112135.png)

> 解决：两种方法：Redis队列，自增键

- 解决超卖问题，可以利用Redis队列实现，给每件商品创建一个独立的商品个数队列，例如：A商品有2个，A商品的ID为1001，则可以创建一个队列,key=SeckillGoodsCountList_1001,往该队列中塞2次该商品ID。
- 每次给用户下单的时候，先从队列中取数据，如果能取到数据，则表明有库存，如果取不到，则表明没有库存，这样就可以防止超卖问题产生了。
- 在我们队Redis进行操作的时候，很多时候，都是先将数据查询出来，在内存中修改，然后存入到Redis，在并发场景，会出现数据错乱问题，为了控制数量准确，我们单独将商品数量整一个自增键，自增键是线程安全的，所以不担心并发场景的问题。
- 当最后一个商品被消耗应当将商品数量同步回数据库，因此需要使用这个自增键来判断
- 也可以在判断之前的队列的时候，如果一旦为空就写回数据库

### 订单支付问题

- 两个队列，一个秒杀队列，一个普通支付队列
- 利用微信支付的attach字段，如果成功直接返回队列名

![20210322135636](http://ruiimg.hifool.cn/img20210322135636.png)

## RabbitMQ延时消息队列

### 延时队列介绍

- 延时队列即放置在该队列里面的消息是不需要立即消费的，而是等待一段时间之后取出消费。
- Rabbitmq实现延时队列一般而言有两种形式：
  - 第一种方式：利用两个特性： Time To Live(TTL)、Dead Letter Exchanges（DLX）[A队列过期->转发给B队列]
  - 第二种方式：利用rabbitmq中的插件x-delay-message

### TTL DLX实现延时队列

#### TTL DLX介绍

**TTL**
- RabbitMQ可以针对队列设置x-expires(则队列中所有的消息都有相同的过期时间)或者针对Message设置x-message-ttl(对消息进行单独设置，每条消息TTL可以不同)，来控制消息的生存时间，如果超时(两者同时设置以最先到期的时间为准)，则消息变为dead letter(死信)

**Dead Letter Exchanges（DLX）**
- RabbitMQ的Queue可以配置x-dead-letter-exchange和x-dead-letter-routing-key（可选）两个参数，如果队列内出现了dead letter，则按照这两个参数重新路由转发到指定的队列。
- x-dead-letter-exchange：出现dead letter之后将dead letter重新发送到指定exchange
- x-dead-letter-routing-key：出现dead letter之后将dead letter重新按照指定的routing-key发送

![20210322141701](http://ruiimg.hifool.cn/img20210322141701.png)

## 分布式事务

- 分布式事务指事务的参与者、支持事务的服务器、资源服务器以及事务管理器分别位于不同的分布式系统的不同节点之上,且属于不同的应用，分布式事务需要保证这些操作要么全部成功，要么全部失败。本质上来说，分布式事务就是为了保证不同数据库的数据一致性。
- 事务的作用：保证每个事务的数据一致性。

### 基于XA协议的两阶段提交(2PC)

两阶段提交协议(Two Phase Commitment Protocol)中，涉及到两种角色

==一个事务协调者==（coordinator）：负责协调多个参与者进行事务投票及提交(回滚)
多个==事务参与者==（participants）：即本地事务执行者

总共处理步骤有两个
（1）投票阶段（voting phase）：协调者将通知事务参与者准备提交或取消事务，然后进入表决过程。参与者将告知协调者自己的决策：同意（事务参与者本地事务执行成功，但未提交）或取消（本地事务执行故障）；
（2）提交阶段（commit phase）：收到参与者的通知后，协调者再向参与者发出通知，根据反馈情况决定各参与者是否要提交还是回滚；

如图所示 1-2为第一阶段，2-3为第二阶段
![1565819561657](http://ruiimg.hifool.cn/img1565819561657.png)
![1565819640025](http://ruiimg.hifool.cn/img1565819640025.png)

如果任一资源管理器在第一阶段返回准备失败，那么事务管理器会要求所有资源管理器在第二阶段执行回滚操作。通过事务管理器的两阶段协调，最终所有资源管理器要么全部提交，要么全部回滚，最终状态都是一致的

> **优点：** 尽量保证了数据的强一致，适合对数据强一致要求很高的关键领域。
> **缺点：** 牺牲了可用性，对性能影响较大，不适合高并发高性能场景，如果分布式系统跨接口调用，目前 .NET 界还没有实现方案。

### 补偿事务（TCC）-（3PC）

TCC 将事务提交分为 Try(method1) - Confirm(method2) - Cancel(method3) 3个操作。其和两阶段提交有点类似，Try为第一阶段，Confirm - Cancel为第二阶段，是一种应用层面侵入业务的两阶段提交。

| 操作方法 | 含义 |
| --- | --- |
| Try | 预留业务资源/数据效验-尝试检查当前操作是否可执行 |
| Confirm  | 确认执行业务操作，实际提交数据，不做任何业务检查，try成功，confirm必定成功，需保证幂等 |
| Cancel | 取消执行业务操作，实际回滚数据，需保证幂等 |

其核心在于将业务分为两个操作步骤完成。不依赖 RM 对分布式事务的支持，而是通过对业务逻辑的分解来实现分布式事务。
![1565819838546](http://ruiimg.hifool.cn/img1565819838546.png)

例如： A要向 B 转账，思路大概是： 

> 我们有一个本地方法，里面依次调用 
1、首先在 Try 阶段，要先调用远程接口把 B和 A的钱给冻结起来。 
2、在 Confirm 阶段，执行远程调用的转账的操作，转账成功进行解冻。 
3、如果第2步执行成功，那么转账成功，如果第二步执行失败，则调用远程冻结接口对应的解冻方法 (Cancel)。 

> 假设用户user表中有两个字段：可用余额(available_money)、冻结余额(frozen_money)
A扣钱对应服务A(ServiceA)
B加钱对应服务B(ServiceB)
转账订单服务(OrderService)
业务转账方法服务(BusinessService)

ServiceA，ServiceB，OrderService都需分别实现try()，confirm()，cancle()方法，方法对应业务逻辑如下

| 操作方法  | **ServiceA** | **ServiceB**  | **OrderService** |
| --- | --- | --- | --- |
| try() | 校验余额(并发控制)<br/>冻结余额+1000<br/>余额-1000 | 冻结余额+1000 | 创建转账订单，状态待转账 |
| confirm() | 冻结余额-1000 |      | 状态变为转账成功 |
| cancle()  | 冻结余额-1000<br/>余额+1000 |    | 状态变为转账失败 |

其中业务调用方BusinessService中就需要调用
ServiceA.try()
ServiceB.try()
OrderService.try()

1、当所有try()方法均执行成功时，对全局事物进行提交，即由事物管理器调用每个微服务的confirm()方法

2、 当任意一个方法try()失败(预留资源不足，抑或网络异常，代码异常等任何异常)，由事物管理器调用每个微服务的cancle()方法对全局事务进行回滚

**优点：** 跟2PC比起来，实现以及流程相对简单了一些，但数据的一致性比2PC也要差一些

**缺点：** 缺点还是比较明显的，在2,3步中都有可能失败。TCC属于应用层的一种补偿方式，所以需要程序员在实现的时候多写很多补偿的代码，在一些场景中，一些业务流程可能用TCC不太好定义及处理。存在非幂等问题

### 本地消息表（异步确保）- 事务最终一致性

本地消息表这种实现方式应该是业界使用最多的，其核心思想是将分布式事务拆分成本地事务进行处理，这种思路是来源于ebay。我们可以从下面的流程图中看出其中的一些细节： 

![1553321000110](http://ruiimg.hifool.cn/img1553321000110.png)

基本思路就是：

消息生产方，需要额外建一个消息表，并记录消息发送状态。消息表和业务数据要在一个事务里提交，也就是说他们要在一个数据库里面。然后消息会经过MQ发送到消息的消费方。如果消息发送失败，会进行重试发送。

消息消费方，需要处理这个消息，并完成自己的业务逻辑。此时如果本地事务处理成功，表明已经处理成功了，如果处理失败，那么就会重试执行。如果是业务上面的失败，可以给生产方发送一个业务补偿消息，通知生产方进行回滚等操作。

生产方和消费方定时扫描本地消息表，把还没处理完成的消息或者失败的消息再发送一遍。如果有靠谱的自动对账补账逻辑，这种方案还是非常实用的。

这种方案遵循BASE理论，采用的是最终一致性，笔者认为是这几种方案里面比较适合实际业务场景的，即不会出现像2PC那样复杂的实现(当调用链很长的时候，2PC的可用性是非常低的)，也不会像TCC那样可能出现确认或者回滚不了的情况。

**优点：** 一种非常经典的实现，避免了分布式事务，实现了最终一致性。在 .NET中 有现成的解决方案。

**缺点：** 消息表会耦合到业务系统中，如果没有封装好的解决方案，会有很多杂活需要处理。

### MQ 事务消息 - 事务最终一致

有一些第三方的MQ是支持事务消息的，比如RocketMQ，他们支持事务消息的方式也是类似于采用的二阶段提交，但是市面上一些主流的MQ都是不支持事务消息的，比如 RabbitMQ 和 Kafka 都不支持。

以阿里的 RocketMQ 中间件为例，其思路大致为：

> Half Message，半消息
- 暂时不能被 Consumer消费的消息。Producer已经把消息发送到 Broker端，但是此消息的状态被标记为不能投递，处于这种状态下的消息称为半消息。事实上，该状态下的消息会被放在一个叫做RMQ_SYS_TRANS_HALF_TOPIC的主题下。
- 当 Producer端对它二次确认后，也就是 Commit之后，Consumer端才可以消费到；那么如果是Rollback，该消息则会被删除，永远不会被消费到。


> 事务状态回查
- 我们想，可能会因为网络原因、应用问题等，导致Producer端一直没有对这个半消息进行确认，那么这时候 Broker服务器会定时扫描这些半消息，主动找Producer端查询该消息的状态。
- 简而言之，RocketMQ事务消息的实现原理就是基于两阶段提交和事务状态回查，来决定消息最终是提交还是回滚的。

![1](http://ruiimg.hifool.cn/img1.jpg)

**优点：** 实现了最终一致性，不需要依赖本地数据库事务。

**缺点：** 目前主流MQ中只有RocketMQ支持事务消息。

## Seata 2pc改进

2019 年 1 月，阿里巴巴中间件团队发起了开源项目 [*Fescar*](https://www.oschina.net/p/fescar)*（Fast & EaSy Commit And Rollback）*，和社区一起共建开源分布式事务解决方案。Fescar 的愿景是让分布式事务的使用像本地事务的使用一样，简单和高效，并逐步解决开发者们遇到的分布式事务方面的所有难题。

**Fescar 开源后，蚂蚁金服加入 Fescar 社区参与共建，并在 Fescar 0.4.0 版本中贡献了 TCC 模式。**

为了打造更中立、更开放、生态更加丰富的分布式事务开源社区，经过社区核心成员的投票，大家决定对 Fescar 进行品牌升级，并更名为 **Seata**，意为：**Simple Extensible Autonomous Transaction Architecture**，是一套一站式分布式事务解决方案。

Seata 融合了阿里巴巴和蚂蚁金服在分布式事务技术上的积累，并沉淀了新零售、云计算和新金融等场景下丰富的实践经验。

### Seata介绍

解决分布式事务问题，有两个设计初衷

**对业务无侵入**：即减少技术架构上的微服务化所带来的分布式事务问题对业务的侵入
**高性能**：减少分布式事务解决方案所带来的性能消耗

seata中有两种分布式事务实现方案，AT及TCC

- AT模式主要关注多 DB 访问的数据一致性，当然也包括多服务下的多 DB 数据访问一致性问题
  - 2PC改进，效率比较高

- TCC 模式主要关注业务拆分，在按照业务横向扩展资源时，解决微服务间调用的一致性问题

### AT模式

Seata AT模式是基于XA事务演进而来的一个分布式事务中间件，XA是一个基于数据库实现的分布式事务协议，本质上和两阶段提交一样，需要数据库支持，Mysql5.6以上版本支持XA协议，其他数据库如Oracle，DB2也实现了XA接口

![1565820574338](http://ruiimg.hifool.cn/img1565820574338.png)

解释：

**Transaction Coordinator (TC)**： 事务协调器，维护全局事务的运行状态，负责协调并驱动全局事务的提交或回滚。
**Transaction Manager（TM）**： 控制全局事务的边界，负责开启一个全局事务，并最终发起全局提交或全局回滚的决议。
**Resource Manager (RM)**： 控制分支事务，负责分支注册、状态汇报，并接收事务协调器的指令，驱动分支（本地）事务的提交和回滚。

协调执行流程如下：

![1565820735168](http://ruiimg.hifool.cn/img1565820735168.png)

Branch就是指的分布式事务中每个独立的本地局部事务。

**第一阶段**

Seata 的 JDBC 数据源代理（ProxyDataSource）通过对业务 SQL 的解析，把业务数据在更新前后的数据镜像组织成回滚日志，利用 本地事务 的 ACID 特性，将业务数据的更新和回滚日志的写入在同一个 本地事务 中提交。（**把业务流程和日志记录两个操作通过代理数据源绑定到一起**）

这样，可以保证：**任何提交的业务数据的更新一定有相应的回滚日志存在**

![1565820909345](http://ruiimg.hifool.cn/img1565820909345.png)

基于这样的机制，分支的本地事务便可以在全局事务的第一阶段提交，并马上释放本地事务锁定的资源

这也是Seata和XA事务的不同之处，两阶段提交往往对资源的锁定需要持续到第二阶段实际的提交或者回滚操作，而有了回滚日志之后，可以在第一阶段释放对资源的锁定，降低了锁范围，提高效率，即使第二阶段发生异常需要回滚，只需找对undolog中对应数据并反解析成sql来达到回滚目的

同时Seata通过代理数据源将业务sql的执行解析成undolog来与业务数据的更新同时入库，达到了对业务无侵入的效果。

**第二阶段**

如果决议是全局提交，此时分支事务此时已经完成提交，不需要同步协调处理（只需要异步清理回滚日志），Phase2 可以非常快速地完成.

![1565821037492](http://ruiimg.hifool.cn/img1565821037492.png)

如果决议是全局回滚，RM 收到协调器发来的回滚请求，通过 XID 和 Branch ID 找到相应的回滚日志记录，**通过回滚记录生成反向的更新 SQL 并执行**，以完成分支的回滚

- Xid做为全局事务的唯一id

![1565821069728](http://ruiimg.hifool.cn/img1565821069728.png)

### TCC模式

seata也针对TCC做了适配兼容，支持TCC事务方案，原理前面已经介绍过，基本思路就是使用侵入业务上的补偿及事务管理器的协调来达到全局事务的一起提交及回滚。

![1565821173446](http://ruiimg.hifool.cn/img1565821173446.png)

### Seata案例

![20210323153403](http://ruiimg.hifool.cn/img20210323153403.png)

完成一个案例，用户下单的时候记录下单日志，完成订单添加，完成用户账户扣款，完成商品库存削减功能，一会在任何一个微服务中制造异常，测试分布式事务。

> 使用@GlobalTransactional注解开启事务

## Eureka集群

- erurak:集群，各个节点的数据一致，各个节点都属于同等级别的注册中心，不存在leader的概念。
- zookeeper：Zookeeper集群存在Leader节点，并且会进行Leader选举，Leader具有最高权限。

## Redis

### Redis集群

![20210323174355](http://ruiimg.hifool.cn/img20210323174355.png)

- redis 集群中内置了 16384 个哈希槽，当需要在 Redis 集群中放置一个 key-value 时，redis 先对 key 使用 crc16 算法算出一个结果，然后把结果对 16384 求余数，这样每个key 都会对应一个编号在 0-16383 之间的哈希槽，redis 会根据节点数量大致均等的将哈希槽映射到不同的节点。
- 进入redis源码目录中的src目录 执行下面的命令 redis-trib.rb ruby工具,可以实现Redis集群,create创建集群，--replicas创建主从关系 1：是否随机创建（是）。
```bash
./redis-trib.rb create --replicas 1 192.168.25.140:7001 192.168.25.140:7002 192.168.25.140:7003
192.168.25.140:7004 192.168.25.140:7005 192.168.25.140:7006
```

### Redis持久化

​Redis的数据都放在内存中。如果机器挂掉，内存的数据就不存在，数据不能恢复，严重影响使用。那么redis本身给我们提供了持久化机制。即时出现这样的问题，也能恢复数据。接下来我们来看下redis的两种持久化方

- RDB:  快照形式  （定期数据保存磁盘中）会产生一个dump.rdb文件,redis默认开启了RDB的持久化方式。
  - 特点：会存在数据丢失，性能较好，用于数据备份。![1558590788839](http://ruiimg.hifool.cn/img1558590788839.png)
    ```properties
      # 在 900 秒内最少有 1 个 key 被改动，或者 300 秒内最少有 10 个 key 被改动，又或者 60 秒内最少有 1000 个 key 被改动，以上三个条件随便满足一个，就触发一次保存操作。

      #    if(在60秒之内有10000个keys发生变化时){
      #      进行镜像备份
      #    }else if(在300秒之内有10个keys发生了变化){
      #      进行镜像备份
      #    }else if(在900秒之内有1个keys发生了变化){
      #      进行镜像备份
      #    }
    ```
- AOF : append only file . 所有对redis的操作命令记录在.aof文件中,如果想恢复数据，重新加载文件，执行文件中的命令即可。默认的情况下 redis没有开启，要想开启，必须配置。
  - 特点：每秒保存，数据完整性比较好，耗费性能。

### AOF和RDB对比

| 命令       | RDB    | AOF          |
| ---------- | ------ | ------------ |
| 启动优先级 | 低     | 高           |
| 体积       | 小     | 大           |
| 恢复速度   | 快     | 慢           |
| 数据安全性 | 丢数据 | 根据策略决定 |

RDB的最佳策略：

+ 关闭 
+ 集中管理（用于备份数据）![20210324121113](http://ruiimg.hifool.cn/img20210324121113.png)
+ 主从模式，从开。

AOF的最佳策略：

+ 建议 开  每秒刷盘->aof日志文件中
+ AOF重写集中管理

最佳的策略：

+ 小分片（max_memery 4G左右）
+ 监控机器的负载

### Redis哨兵

- Redis在使用过程中服务器毫无征兆的宕机，是一个麻烦的事情，如何保证备份的机器是原始服务器的完整备份呢？这时候就需要哨兵和复制。
- Sentinel（哨兵）可以管理多个Redis服务器，它**提供了监控，提醒以及自动的故障转移的功能**，
- Replication（复制）则是负责让一个Redis服务器可以配备多个备份的服务器。
- Redis也是利用这两个功能来保证Redis的高可用的

> 架构原理如图：

![1558593335136](http://ruiimg.hifool.cn/img1558593335136.png)

1. 多个sentinel 发现并确认master有问题。
2. sentinel内部选举领导
3. 选举出slave作为新的master
4. 通知其余的slave成为新master的slave
5. 通知客户端 主从变化
6. 如果老的master重新复活，那么成为新的master的slave

> 要实现上边的功能的主要细节主要有以下三个定时任务：

1. 每10秒，哨兵会向master和slave发送INFO命令(目的就是监控每一个节点信息)
2. 每2秒，哨兵会向master库和slave的频道(__sentinel__:hello)发送自己的信息 （sentinel节点通过__sentinel__:hello频道进行信息交换，比如加入哨兵集群，分享自己节点数据）
3. 每1秒，哨兵会向master和slave以及其他哨兵节点发送PING命令（目的就是 redis节点的状态监控，还有领导选举，主备切换选择等）

> 策略总结：

1. 尽量为 每一个节点部署一个哨兵
2. 哨兵也要搭建集群（防止哨兵单点故障）
3. 每一个节点都同时设置quorum的值超过半数（N/2）+1 <https://zh.wikipedia.org/wiki/Quorum_(%E5%88%86%E5%B8%83%E5%BC%8F%E7%B3%BB%E7%BB%9F)>

### Redis缓存击穿

![1558597517593](http://ruiimg.hifool.cn/img1558597517593.png)

如图：

1. 当用户根据key 查询数据时，先查询缓存，如果缓存有命中，返回，
2. 但是如果缓存没有命中直接穿过缓存层，访问数据层 如果有，则存储指缓存，
3. 但是同样如果没有命中，（也就是数据库中也没有数据）直接返回用户，但是不缓存

这就是缓存的穿透。如果某一个key 请求量很大，但是存储层也没有数据，大量的请求都会达到存储层就会造成数据库压力巨大，有可能宕机的情况。

> 解决方案

![20210324122851](http://ruiimg.hifool.cn/img20210324122851.png)

如图：

1. 当缓存中没有命中的时候，从数据库中获取
2. 当数据库中也没有数据的时候，我们直接将null 作为值设置redis中的key上边。
3. 此时如果没有数据，一般情况下都需要设置一个过期时间，**例如：5分钟失效。（为了避免过多的KEY 存储在redis中）**
4. 返回给用户，
5. 用户再次访问时，已经有KEY了。此时KEY的值是null而已，这样就可以在缓存中命中，解决了缓存穿透的问题。

> 注意：缓存空对象会有两个问题：

- 第一，空值做了缓存，意味着缓存层中存了更多的键，需要更多的内存空间 ( 如果是攻击，问题更严重 )，比较有效的方法是针对这类数据设置一个较短的过期时间，让其自动剔除。
- 第二，缓存层和存储层的数据会有一段时间窗口的不一致，可能会对业务有一定影响。例如过期时间设置为 5分钟，如果此时存储层添加了这个数据，那此段时间就会出现缓存层和存储层数据的不一致，此时可以利用消息系统或者其他方式清除掉缓存层中的空对象。
  - 使用canal解决

### Redis缓存雪崩问题

- 如果缓存集中在一段时间内失效，发生大量的缓存穿透，所有的查询都落在数据库上，造成了缓存雪崩。

> 解决方案

这个没有完美解决办法，但可以分析用户行为，尽量让失效时间点均匀分布。

+ 限流 加锁排队

在缓存失效后，通过对某一个key加锁或者是队列 来控制key的线程访问的数量。例如：某一个key 只允许一个线程进行 操作。

+ 限流

在缓存失效后，某一个key 做count统计限流，达到一定的阈值，直接丢弃，不再查询数据库。例如：令牌桶算法。等等。

+ 数据预热

在缓存失效应当尽量避免某一段时间，可以先进行数据预热，比如某些热门的商品。提前在上线之前，或者开放给用户使用之前，先进行loading 缓存中，这样用户使用的时候，直接从缓存中获取。要注意的是，要根据业务来进行过期时间的设置 ，尽量均匀。

+ 做缓存降级（二级(多级)缓存策略）

当分布式缓存失效的时候，可以采用本地缓存，本地缓存没有再查询数据库。这种方式，可以避免很多数据分布式缓存没有，就直接打到数据库的情况。![20210324123421](http://ruiimg.hifool.cn/img20210324123421.png)

## RabbitMQ

### 集群

- 普通集群
  - 写入->rabbit1->同步->rabbit2->取数据
  - 出口固定，容易产生瓶颈
- 镜像集群
  - ​消息实体会主动在镜像节点间同步，而不是在客户端取数据时临时拉取，也就是说多少节点消息就会备份多少份。
  - 该模式带来的副作用也很明显，除了降低系统性能外，如果镜像队列数量过多，加之大量的消息进入，集群内部的网络带宽将会被这种同步通讯大大消耗掉。所以在对可靠性要求较高的场合中适用
  - 由于镜像队列之间消息自动同步，且内部有选举master机制，即使master节点宕机也不会影响整个集群的使用，达到去中心化的目的，从而有效的防止消息丢失及服务不可用等问题。