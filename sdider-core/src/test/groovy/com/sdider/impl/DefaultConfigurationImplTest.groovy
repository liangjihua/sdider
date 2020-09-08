package com.sdider.impl

import com.sdider.AbstractDynamicPropertiesObjectTest
import com.sdider.api.common.DynamicPropertiesObject

class DefaultConfigurationImplTest extends AbstractDynamicPropertiesObjectTest {
    DefaultConfigurationImpl configuration

    def setup() {
        configuration = Spy(dpObject as DefaultConfigurationImpl)
    }

    def "MethodMissing"() {
        when:
        configuration.methodMissing('foo', ['bar'] as String[])

        then:
        'bar' == configuration.get('foo')
    }

    def "getProperty"() {
        configuration.set('foo', 'bar')
        when:
        def result = configuration.getProperty("properties")

        then:
        1 * configuration.getProperties()
        result == configuration.getProperties()

        when:
        result = configuration.getProperty('foo')

        then:
        1 * configuration.has('foo')
        1 * configuration.get('foo')
        'bar' == result

        when:
        configuration.getProperty('not-exists')

        then:
        thrown(MissingPropertyException)
    }

    def "configure"() {
        configuration.set('foo', 'bar')
        configuration.set('name', 'monica')
        def configured = [:]

        when:
        configuration.configure(configured)

        then:
        [foo:'bar', name:'monica'] == configured
    }

    def "configure specified key"() {
        configuration.set('foo', 'bar')
        configuration.set('name', 'monica')
        def configured = [:]

        when:
        configuration.configure('apple', configured)
        then:
        configured.isEmpty()

        when:
        configuration.configure('foo', configured)

        then:
        [foo:'bar'] == configured
    }

    def "configure Closure"() {
        configuration.set('clo', {
            name = 'monica'
        })
        def configured = [:]

        when:
        configuration.configure('clo', configured)

        then:
        [name: 'monica'] == configured
    }

    def "configure obj"() {
        def config = new Config()
        configuration.set('name', 'monica')
        configuration.set('foo', 'bar')
        configuration.set('age', 'errorValue')

        when:
        configuration.configure(config)

        then:
        config.name == 'monica'
        config.age == null
    }

    static class Config {
        String name
        Integer age
    }

    @Override
    protected DynamicPropertiesObject create() {
        return new DefaultConfigurationImpl()
    }
}
