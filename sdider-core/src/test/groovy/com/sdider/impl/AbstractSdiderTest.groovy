package com.sdider.impl

import com.sdider.ExtensionContainer
import com.sdider.ParserContainer
import com.sdider.PipelineContainer
import com.sdider.SdiderRequestContainer
import com.sdider.api.*
import com.sdider.api.common.DynamicPropertiesObject
import com.sdider.impl.exception.SdiderExecuteException
import org.slf4j.Logger
import spock.lang.Specification

class AbstractSdiderTest extends Specification {
    AbstractSdider sdider
    Closeable closeable
    ParserContainer parserContainer
    PipelineContainer pipelineContainer
    CrawlerFactory factory
    Crawler crawler
    ExtensionContainer extensions
    DynamicPropertiesObject properties

    @SuppressWarnings('GroovyAssignabilityCheck')
    void setup() {
        extensions = Mock()
        properties = Mock()
        closeable = Mock()
        parserContainer = Mock()
        pipelineContainer = Mock()
        crawler = Mock()
        factory = Mock(CrawlerFactory) {
            create(_, _, _, _) >> crawler
        }
        sdider = Mock(constructorArgs:[], AbstractSdider) {
            execute() >> {
                callRealMethod()
            }
            propertyMissing(_) >> {
                callRealMethod()
            }
            createCrawler() >> {
                callRealMethod()
            }
            getParsers() >> parserContainer
            getPipelines() >> pipelineContainer
            getExceptionHandler() >> Mock(ExceptionHandler)
            getLogger() >> Mock(Logger)
            findCrawlerFactory() >> factory
            getExtensions() >> extensions
            getProperties() >> properties
            getStartRequests() >> Mock(SdiderRequestContainer)
        }
    }

    def "Execute"() {
        parserContainer.getMainParser() >> Mock(ResponseParser)
        pipelineContainer.getEnabledPipelines() >> [Mock(Pipeline)]
        when:
        sdider.execute()

        then:
        1 * factory.create(_, _, _, _) >> crawler
        1 * sdider.callBeforeCrawl()
        1 * sdider.callAfterCrawl()
        1 * crawler.run()
        1 * sdider.beforeExecute()
        1 * sdider.afterExecute()
    }

    def "mainParser check"() {
        when:
        sdider.execute()

        then:
        1 * parserContainer.getMainParser() >> null
        thrown(SdiderExecuteException)
    }

    def "exceptionHandler check"() {
        parserContainer.getMainParser() >> Mock(ResponseParser)
        pipelineContainer.getEnabledPipelines() >> [Mock(Pipeline)]

        when:
        sdider.execute()

        then:
        1 * sdider.getExceptionHandler() >> null
        thrown(SdiderExecuteException)
    }

    def "enable pipelines check" () {
        parserContainer.getMainParser() >> Mock(ResponseParser)
        when:
        sdider.execute()

        then:
        1 * pipelineContainer.getEnabledPipelines() >> []
        thrown(SdiderExecuteException)
    }

    def "PropertyMissing"() {
        when:
        sdider.propertyMissing('foo')

        then:
        1 * extensions.contains('foo') >> false
        0 * extensions.getByName(_)
        1 * properties.has('foo') >> false
        0 * properties.get('foo')
        thrown(MissingPropertyException)

    }

    def "propertyMissing2"() {
        when:
        def result = sdider.propertyMissing('foo')

        then:
        1 * extensions.contains('foo') >> true
        1 * extensions.getByName('foo') >> 'bar'
        0 * properties.has(_)
        0 * properties.get(_)
        noExceptionThrown()
        result == 'bar'
    }

    def "propertyMissing3"() {
        when:
        def result = sdider.propertyMissing('foo')

        then:
        1 * extensions.contains('foo') >> false
        0 * extensions.getByName(_)
        1 * properties.has('foo') >> true
        1 * properties.get('foo') >> 'bar'
        noExceptionThrown()
        result == 'bar'
    }

    def "findCrawlerFactory"() {
        when:
        def factory = sdider.findCrawlerFactory()

        then:
        1 * sdider.findCrawlerFactory() >> {
            callRealMethod()
        }
        1 * sdider.getConfiguration() >> Mock(Configuration) {
            get('crawlerFactoryClass') >> 'com.sdider.impl.AbstractSdiderTest$TestCrawlerFactory'
        }
        factory instanceof TestCrawlerFactory
    }

    static class TestCrawlerFactory implements CrawlerFactory {

        @Override
        Crawler create(ResponseParser parser, List<Pipeline> pipelines, ExceptionHandler handler, Configuration configuration) {
            return null
        }
    }
}
