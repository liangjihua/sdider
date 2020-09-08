package com.sdider.impl

import com.sdider.api.ExceptionHandler
import com.sdider.api.Item
import com.sdider.api.Request
import com.sdider.api.Response
import spock.lang.Specification

class DefaultSdiderTest extends Specification {
    DefaultSdider sdider
    Closeable closeable

    def setup() {
        closeable = Mock()
        sdider = Spy(new DefaultSdider(new File(getClassPathFile('sdider-test.sdider'))))
    }

    def "Apply"() {
        when:
        sdider.apply(getClassPathFile('sdider-test-plugin.sdider'))
        def startRequests = sdider.getStartRequests().getRequests()

        then:
        1 == startRequests.size()
        'GET' == startRequests[0].getMethod()
        'http://foo.bar' == startRequests[0].getUrl()
    }

    def "Properties"() {
        when:
        sdider.properties {
            foo 'bar'
        }

        then:
        'bar' == sdider.propertyMissing('foo') //spock 2.0-M3不够完善，不能直接使用sdider.foo这种方式触发
    }

    def "GetProperties"() {
        expect:
        sdider.getProperties().getProperties().isEmpty()
        sdider.getProperties() == sdider.getProperties()

        when:
        sdider.properties {
            foo 'bar'
        }

        then:
        'bar' == sdider.getProperties().get('foo')
    }

    def "Extensions"() {
        when:
        sdider.extensions {
            add('foo', 'bar')
        }

        then:
        'bar' === sdider.getExtensions().getByName('foo')
    }

    def "Configuration"() {
        when:
        sdider.configuration {
            foo 'bar'
        }

        then:
        'bar' == sdider.getConfiguration().get('foo')
    }

    def "GetConfiguration"() {
        expect:
        null != sdider.getConfiguration()
        sdider.getConfiguration() == sdider.getConfiguration()
    }

    def "Logger"() {
        when:
        sdider.logger {
            level 'debug'
        }

        then:
        'debug' == sdider.getLogConfig().getLevel()
    }

    def "TestLogger"() {
        def config = getClassPathFile('log4j2-test.yml')
        when:
        sdider.logger(config)

        then:
        config == sdider.getLogConfig().getConfigLocation().toString()
    }

    def "GetLogger" () {
        expect:
        sdider.getLogger() == sdider.getLogger()
    }

    def "BeforeCrawl"() {
        when:
        sdider.beforeCrawl {
            getCloseable().close()
        }
        sdider.callBeforeCrawl()

        then:
        1 * closeable.close()
    }

    def "AfterCrawl"() {
        when:
        sdider.afterCrawl {
            getCloseable().close()
        }
        sdider.callAfterCrawl()

        then:
        1 * closeable.close()
    }

    def "StartRequests"() {
        when:
        sdider.startRequests {
            request{
                POST 'http://foo.bar'
            }
        }
        def startRequests = sdider.getStartRequests().getRequests()

        then:
        1 == startRequests.size()
        'POST' == startRequests[0].getMethod()
        'http://foo.bar' == startRequests[0].getUrl()
    }

    def "GetStartRequests"() {
        expect:
        null != sdider.getStartRequests()
        sdider.getStartRequests().getRequests().isEmpty()

        when:
        sdider.startRequests {
            request{
                POST 'http://foo.bar'
            }
        }
        def requests = sdider.getStartRequests().getRequests()

        then:
        1 == requests.size()
        'POST' == requests[0].getMethod()
        'http://foo.bar' == requests[0].getUrl()
    }

    def "Parser"() {
        def response = Mock(Response) {
            getHeaders() >> [:]
        }
        def clo = {
            getCloseable().close()
        }

        when:
        sdider.parser(clo)
        sdider.parsers.mainParser.parse(response)

        then:
        1 * closeable.close()
    }

    def "TestParser"() {
        def response = Mock(Response) {
            getHeaders() >> [:]
        }
        def clo = {
            getCloseable().close()
        }
        when:
        sdider.parser('foo', clo)
        sdider.parsers.getByName('foo').parse(response)
        then:
        1 * closeable.close()
    }

    def "GetParsers"() {
        expect:
        null != sdider.getParsers()
        sdider.getParsers() == sdider.getParsers()

        when:
        def response = Mock(Response) {
            getHeaders() >> [:]
        }
        sdider.parser('foo') {
            getCloseable().close()
        }
        sdider.getParsers().getByName('foo').parse(response)
        then:
        1 * closeable.close()
    }

    def "ExceptionHandler"() {
        when:
        sdider.exceptionHandler {
            getCloseable().close()
        }
        sdider.getExceptionHandler().handleException(new Exception(), Mock(Request), null)

        then:
        1 * closeable.close()
    }

    def "TestExceptionHandler"() {
        ExceptionHandler eh = Mock()
        when:
        sdider.exceptionHandler(eh)
        then:
        eh == sdider.getExceptionHandler()
    }

    def "ExceptionHandler3"() {
        expect:
        null == sdider.getExceptionHandler()
    }

    def "Pipeline"() {
        when:
        sdider.pipeline('foo') {
            getCloseable().close()
        }
        sdider.pipelines.getByName('foo').process(Mock(Item))
        then:
        1 * closeable.close()
    }

    def "GetPipelines"() {
        expect:
        null != sdider.getPipelines()

        when:
        sdider.pipeline('foo'){}
        sdider.pipeline('bar'){}

        then:
        null != sdider.getPipelines().getByName('foo')
        null != sdider.getPipelines().getByName('bar')
    }

    def "GetExtensions"() {
        expect:
        null != sdider.getExtensions()
        sdider.getExtensions() == sdider.getExtensions()
    }

    def "CallBeforeCrawl"() {
        when:
        sdider.callBeforeCrawl()
        then:
        notThrown(NullPointerException)

        when:
        sdider.beforeCrawl {
            getCloseable().close()
        }
        sdider.callBeforeCrawl()
        then:
        1 * closeable.close()
    }

    def "CallAfterCrawl"() {
        when:
        sdider.callAfterCrawl()
        then:
        notThrown(NullPointerException)

        when:
        sdider.afterCrawl {
            getCloseable().close()
        }
        sdider.callAfterCrawl()
        then:
        1 * closeable.close()
    }

    private String getClassPathFile(String fileName) {
        getClass().getClassLoader().getResource(fileName).getFile()
    }

}
