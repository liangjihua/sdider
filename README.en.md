# sdider

> A crawler framework based on DSL scripts, designed to facilitate the development and distribution of crawlers.

For crawlers that are simple in logic and need to be distributed among multiple environments, it is not very convenient
to use Python (scrapy) or Java. In addition to runtime, a series of dependencies, compilation, packaging, and Virtual 
environment installation and other processes. It is also difficult to adjust the packaged crawler application.
sdider uses one single plain text DSL script to write crawlers, which provides a complete crawler function and extension 
capabilities, can easily write crawlers with high readability, and it's easy to distribute.

#### Instructions

This is a simple script for crawling open source projects recommended by gitee:
```groovy
configuration {
    requests {
        userAgent UA_FIREFOX //declare the globally UA
    }
}

startRequests {
    request "https://gitee.com/explore/all"  //add a start requests
}

parser {    //declare a response parser
    items { //items for extract data
        def list = css.select('div.explore-repo__list div.content')//css is an implicit variable in the parser, actually an instance of [Jsoup](https://jsoup.org/)
        for (def e : list) {
            item {      //declare a data item, each item will pass through each pipeline in turn
                title           e.select('h3').text()   //put a data to item as key-value
                watch           e.select('div.pull-right > a:nth-child(1) > span').text()
                star            e.select('div.pull-right > a:nth-child(2) > span').text()
                fork            e.select('div.pull-right > a:nth-child(3) > span').text()
                'project-meta'  e.select('.project-meta').text()    //when the key is not a valid variable name, use single and double quotes
            }
        }
    }
    targets{    //targets for extract requests
        request css.select('div.pagination > a[rel=next]').attr('href')   //same as the startRequests block, use request to add a request
    }
}
```
I think the above script is not difficult for people with programming experience to understand. The sdider script is 
mainly composed of some declarative DSL blocks. Those who have used **gradle** may find it familiar. In fact, it is the 
same as **gradle**. The DSL of the sdider script is also developed in Groovy. Those who are familiar with Groovy should 
be very familiar with it. easy to use. It doesn't matter if you haven't used Groovy, because Groovy is fully compatible 
with Java syntax (support for Java native lambda expressions was also added in 3.0).

The above script has been included in the sdider distribution package. Use `./bin/sdider gitee.sdider` (in windows, use 
`.\bin\sdider.bat`) to run the script in the main directory of sdider.
When the script does not declare `pipeline`, sdider will add a default console pipeline, so the running result of the 
script is:
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
......
```

#### Installation

Download the distribution package and unzip it to the installation directory. You can also add the `bin` directory to 
environment variables so that you can use sdider anywhere.

