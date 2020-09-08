package com.sdider.util

import spock.lang.Specification

import static com.sdider.util.Args.notEmpty
import static com.sdider.util.Args.notNull

class ArgsTest extends Specification{

    def "NotNull"() {
        when:
        def a = notNull('foo', 'foo')
        then:
        a == 'foo'

        when:
        notNull(null, "foo")
        then:
        IllegalArgumentException argumentException = thrown()
        argumentException.getMessage() == "invalid argument: {'name':'foo', 'reason':'argument is null'}"
    }

    def "NotEmpty"() {
        when:
        def a = notEmpty('foo', 'foo')
        then:
        a == 'foo'

        when:
        notEmpty('', 'foo')
        then:
        IllegalArgumentException argumentException = thrown()
        argumentException.getMessage() == "invalid argument: {'name':'foo', 'reason':'String argument is empty'}"

        when:
        notEmpty(null, 'foo')
        then:
        IllegalArgumentException argumentException2 = thrown()
        argumentException2.getMessage() == "invalid argument: {'name':'foo', 'reason':'argument is null'}"
    }
}
