package com.sdider.impl.pipeline

import com.sdider.api.Pipeline
import com.sdider.impl.exception.UndefinedPipelineException
import spock.lang.Specification

class DefaultPipelineContainerTest extends Specification {
    DefaultPipelineContainer container
    Pipeline pipeline
    Pipeline pipeline2

    void setup() {
        pipeline = Mock(Pipeline) {
            getName() >> 'pipeline'
        }
        pipeline2 = Mock(Pipeline) {
            getName() >> 'pipeline2'
        }
        container = new DefaultPipelineContainer()
        container.add(pipeline)
        container.add(pipeline2)
    }

    def "Enable"() {
        when:
        container.enable('pipeline')
        container.enable(pipeline2)
        then:
        [pipeline, pipeline2] == container.getEnabledPipelines()
    }

    def "enable non-exists"() {
        when:
        container.enable('foo')
        then:
        thrown(UndefinedPipelineException)
    }

    def "enable twice"() {
        when:
        container.enable('pipeline')
        container.enable('pipeline')
        then:
        [pipeline] == container.getEnabledPipelines()
    }

    @SuppressWarnings('GroovyAssignabilityCheck')
    def "Disable"() {
        container.enable(arg)
        when:
        container.disable(arg)
        then:
        container.getEnabledPipelines().isEmpty()

        where:
        arg << ['pipeline', Mock(Pipeline)]
    }

    def "disable non-exists"() {
        container.enable('pipeline')
        when:
        container.disable('foo')
        then:
        [pipeline] == container.getEnabledPipelines()
    }

    def "GetEnabledPipelines"() {
        expect:
        container.getEnabledPipelines().isEmpty()

        when:
        container.enable(pipeline2)
        container.enable(pipeline)
        then:
        [pipeline2, pipeline] == container.getEnabledPipelines()
    }

    def "GetAllPipelines"() {
        expect:
        [pipeline, pipeline2] == container.getAllPipelines()

        when:
        container.remove(pipeline)
        then:
        [pipeline2] == container.getAllPipelines()

        when:
        container.add(pipeline)
        then:
        [pipeline2, pipeline] == container.getAllPipelines()
    }

    def "getAllPipelines and modify"() {
        def pipelines = container.getAllPipelines()
        expect:
        [pipeline, pipeline2] == container.getAllPipelines()

        when:
        pipelines.add(Mock(Pipeline))
        pipelines.add(Mock(Pipeline))
        then:
        [pipeline, pipeline2] == container.getAllPipelines()
        4 == pipelines.size()

        when:
        container.remove('pipeline')
        then:
        [pipeline2] == container.getAllPipelines()
        4 == pipelines.size()
    }

    def "Add"() {
        container = new DefaultPipelineContainer()

        when:
        container.add(pipeline)
        container.add(pipeline2)
        container.add(pipeline)
        then:
        [pipeline, pipeline2] == container.getAllPipelines()
        pipeline == container.getByName('pipeline')
        pipeline2 == container.getByName('pipeline2')
        ['pipeline', 'pipeline2'] == container.getAllNames()
    }

    def "Remove"() {
        container.enable(pipeline)
        container.enable(pipeline2)
        when:
        container.remove(pipeline)
        container.remove(pipeline2)
        then:
        container.getAllNames().isEmpty()
        container.getAllPipelines().isEmpty()
        container.getEnabledPipelines().isEmpty()
    }

    def "remove non-exists"() {
        when:
        container.remove('foo')
        then:
        noExceptionThrown()
    }

    def "GetByName"() {
        expect:
        pipeline === container.getByName('pipeline')

        when:
        container.remove('pipeline')
        then:
        null == container.getByName('pipeline')
    }

    def "GetAllNames"() {
        def names = container.getAllNames()
        expect:
        ['pipeline', 'pipeline2'] == names
    }

    def "getAllNames2"() {
        container = new DefaultPipelineContainer()
        expect:
        container.getAllNames().isEmpty()
    }

    def "propertyMissing"() {
        expect:
        pipeline === container.propertyMissing("pipeline")

        when:
        container.propertyMissing("no-exists")

        then:
        thrown(MissingPropertyException)
    }
}
