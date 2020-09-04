package com.sdider.impl

import com.sdider.Sdider
import spock.lang.Specification

class SdiderScriptExecutorTest extends Specification {
    SdiderScriptExecutor executor
    Sdider sdider

    void setup() {
        sdider = Mock()
        executor = new SdiderScriptExecutor(sdider)
    }

    def "Inject"() {
        when:
        executor.inject(new File(getClass().getClassLoader().getResource("sdider-test-plugin.sdider").getFile()))

        then:
        1 * sdider.startRequests(_)
    }

    def "TestInject"() {
        when:
        executor.inject("sdider-test-plugin.sdider")

        then:
        1 * sdider.startRequests(_)
    }
}
