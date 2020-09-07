# sdider

> 一个以DSL脚本为核心的爬虫框架，主要的目的是为了方便爬虫的开发和分发。

对于一些逻辑简单，又需要在多个环境之间分发的爬虫来说，无论是使用Python(scrapy)或是Java都不是十分方便，
除了runtime之外，还需要一系列的依赖、编译、打包、虚拟环境安装等流程。对于打包好的爬虫应用，也难以做出微调。
sdider使用一个纯文本的DSL脚本来编写爬虫，提供了完整的爬虫功能和扩展能力，能够方便的编写具有高可读性的爬虫，
同时又易于分发。

#### 使用

这是一个简单的脚本，用于爬取码云推荐的开源项目：
```groovy
configuration {
    requests {
        userAgent UA_FIREFOX //声明全局请求的UA
    }
}

startRequests {
    request "https://gitee.com/explore/all"  //添加一个起始请求
}

parser {    //声明一个请求响应解析
    items { //items用于提取数据项
        def list = css.select('div.explore-repo__list div.content')//css是parser中的隐式变量，实际上是[Jsoup](https://jsoup.org/)的一个实例
        for (def e : list) {
            item {      //声明一个数据项，每一个数据项会依次通过pipeline
                title           e.select('h3').text()   //数据字段：key value的方式声明
                watch           e.select('div.pull-right > a:nth-child(1) > span').text()
                star            e.select('div.pull-right > a:nth-child(2) > span').text()
                fork            e.select('div.pull-right > a:nth-child(3) > span').text()
                'project-meta'  e.select('.project-meta').text()    //key不是合法的变量名时，使用单双引号包裹
            }
        }
    }
    targets{    //targets用于提取请求
        request css.select('div.pagination > a[rel=next]').attr('href')   //与startRequests block同样，使用request添加一个请求
    }
}
```
我相信上面的脚本对于有编程经验的人来说不难理解。sdider的脚本主要由一些声明式DSL block组成，使用过**gradle**的同学可能觉得很眼熟，实际上与
**gradle**相同，sdider脚本的DSL也是用Groovy开发的，熟悉Groovy的同学应该很容易上手。没有使用过Groovy的同学也没关系，因为Groovy完全兼容Java
的语法（在3.0中也增加了对Java原生lambda表达式的支持）。

sdider的发行包中已经包含了上面的示例脚本，在sdider的主目录下使用`./bin/sdider gitee.sdider`(windows下使用`.\bin\sdider.bat`)运行脚本。
当脚本未声明`pipeline`时，sdider会添加一个默认的console pipeline，所以该脚本的运行结果为：
```text
❯ .\bin\sdider.bat gitee.sdider

request: GET https://gitee.com/explore/all
item: {
    fork=9
    star=18
    title=腾讯开源/TencentOS-kernel
    watch=3
    project-meta=C 操作系统 GPL-2.0
}

request: GET https://gitee.com/explore/all
item: {
    fork=7
    star=21
    title=腾讯开源/wwsearch
    watch=9
    project-meta=C++ 搜索引擎
}
... 省略
```

#### 安装

下载发行包解压至安装目录。也可以将`bin`目录加入环境变量，这样就可以在任意地方使用sdider。


