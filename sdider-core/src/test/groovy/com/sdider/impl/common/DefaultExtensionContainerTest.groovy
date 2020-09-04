package com.sdider.impl.common

import spock.lang.Specification

class DefaultExtensionContainerTest extends Specification {
    DefaultExtensionContainer container

    void setup() {
        container = new DefaultExtensionContainer()
    }

    def "Add"() {
        container.add('foo', 'bar')
        expect:
        container.contains('foo')
        'bar' == container.getByName('foo')
    }

    def "Add null"() {
        when:
        container.add('foo', null)
        then:
        thrown(Exception)
    }

    def "GetByName"() {
        when:
        container.add('foo', 'bar')
        then:
        'bar' == container.getByName('foo')
    }

    def "get non-existent"() {
        when:
        container.getByName('foo')
        then:
        thrown(MissingPropertyException)
    }

    def "Contains"() {
        expect:
        !container.contains('foo')

        when:
        container.add('foo', 'bar')
        then:
        container.contains('foo')
    }
}
