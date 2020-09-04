package com.sdider.impl.log

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import spock.lang.Specification

class LogConfigActionTest extends Specification {
    LogConfigAction action

    void setup() {
        action = new LogConfigAction('foo', 'bar')
    }

    void cleanup() {
        System.clearProperty(LogConfigAction.CONFIGURATION_NAME)
        System.clearProperty(LogConfigAction.LOG_FILENAME)
    }

    def "DoConfigure"() {
        when:
        action.doConfigure()
        then:
        'foo' == System.getProperty(LogConfigAction.CONFIGURATION_NAME)
        'bar' == System.getProperty(LogConfigAction.LOG_FILENAME)
    }

    def "doConfigureWithConfigLocation"() {
        URI configLocation = setConfigLocation()
        when:
        action.doConfigure()
        then:
        assertConfigLocation(configLocation)
    }

    private URI setConfigLocation() {
        URI configLocation = this.getClass().getClassLoader().getResource('log4j2-test.yml').toURI()
        action.setConfigLocation(configLocation)
        configLocation
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    void assertConfigLocation(URI configLocation) {
        assert !System.getProperty(LogConfigAction.CONFIGURATION_NAME)
        assert !System.getProperty(LogConfigAction.LOG_FILENAME)
        LoggerContext context = (LoggerContext) LogManager.getContext(false)
        assert configLocation == context.getConfigLocation()
    }

    def "doConfigureWithConfigAction"() {
        Closeable closeable = setConfigAction()
        when:
        action.doConfigure()
        then:
        assertConfigAction(closeable)
    }

    private Closeable setConfigAction() {
        Closeable closeable = Mock() {
            1 * close()
        }
        action.setConfigAction {
            closeable.close()
            LogConfiguration.DEFAULT_CONFIGURATION.setLevel('error')
        }
        closeable
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    void assertConfigAction(Closeable closeable) {
        assert 'error' == LogConfiguration.DEFAULT_CONFIGURATION.getLevel()
    }

    def "doConfigureWithBoth"() {
        Closeable closeable = setConfigAction()
        URI configLocation = setConfigLocation()
        when:
        action.doConfigure()
        then:
        assertConfigLocation(configLocation)
        assertConfigAction(closeable)
    }
}
