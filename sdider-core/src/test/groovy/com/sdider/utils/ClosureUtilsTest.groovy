package com.sdider.utils

import spock.lang.Specification

class ClosureUtilsTest extends Specification {
    Closeable closeable = Mock()

    def "DelegateRun"() {
        when:
        ClosureUtils.delegateRun(closeable) {
            close()
        }

        then:
        1 * closeable.close()
    }

    def "DelegateCall"() {
        when:
        def result = ClosureUtils.delegateCall(closeable) {
            close()
            'foo'
        }

        then:
        'foo' == result
        1 * closeable.close()
    }
}
