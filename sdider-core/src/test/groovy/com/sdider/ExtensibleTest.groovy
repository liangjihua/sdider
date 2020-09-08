package com.sdider

import spock.lang.Specification

class ExtensibleTest extends Specification {
    Extensible extensible
    ExtensionContainer extensions

    def setup() {
        extensions = Mock()
        extensible = Mock() {
            propertyMissing(_) >> {
                callRealMethod()
            }
            methodMissing(_, _) >> {
                callRealMethod()
            }
        }
    }

    def "PropertyMissing"() {
        given: //mock和stub必须声明在同一个语句中；then 中的mock会被前提至when前面
        1 * extensible.getExtensions() >> extensions
        1 * extensions.getByName("foo") >> 'bar'
        //由于目前spock2.0-M3还不够完善，因此不能直接使用 extensible.foo这种方式来测试propertyMissing
        //查看JavaMockInterceptor第70行
        when:
        def result = extensible.propertyMissing("foo")

        then:
        'bar' == result
    }

    def "delegate to a closure"() {
        given:
        def bar = "bar"
        extensible.getExtensions() >> extensions
        1 * extensions.contains("foo") >> true
        1 * extensions.getByName("foo") >> bar

        when:
        def result = extensible.foo {
            return it
        }

        then:
        result == bar
    }

    def "call a closure" () {
        def closure = {
            return "bar"
        }
        extensible.getExtensions() >> extensions
        1 * extensions.contains("foo") >> true
        1 * extensions.getByName("foo") >> closure

        when:
        def result = extensible.foo()

        then:
        "bar" == result
    }

    def "call a closure with args"() {
        def closure = {
            return it
        }
        extensible.getExtensions() >> extensions
        1 * extensions.contains("foo") >> true
        1 * extensions.getByName("foo") >> closure

        when:
        def result = extensible.foo("bar")

        then:
        "bar" == result
    }

    def "missing extension"() {
        extensible.getExtensions() >> extensions
        1 * extensions.contains("foo") >> false

        when:
        extensible.foo()

        then:
        thrown(MissingMethodException)
    }

    def "call unsuitable extension"() {
        extensible.getExtensions() >> extensions
        1 * extensions.contains("foo") >> true
        extensions.getByName('foo') >> new Object()

        when:
        extensible.foo('bar')

        then:
        thrown(MissingMethodException)
    }

}
