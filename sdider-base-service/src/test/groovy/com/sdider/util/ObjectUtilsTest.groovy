package com.sdider.util

import spock.lang.Specification

import java.util.function.Supplier

import static com.sdider.util.ObjectUtils.or
import static com.sdider.util.ObjectUtils.which

class ObjectUtilsTest extends Specification{

    def "Which"() {
        expect:
        //noinspection GroovyAssignabilityCheck
        which(a, b) == c

        where:
        a    |   b    ||   c
        null |   1    ||   1
        2    |   null ||   2
        '3'  |   '2'    ||   '3'
        null |   null ||   null
    }

    def "Or: a is null"() {
        def a = null
        Supplier b = Spy(newSupplier(1))
        when:
        def c = or(a, b)
        then:
        1 * b.get()
        1 == c
    }

    def "Or: a not null"() {
        def a = 2
        Supplier b = Spy(newSupplier(1))

        when:
        def c = or(a, b)

        then:
        0 * b.get()
        2 == c
    }

    def "Or: a is String"() {
        def a = '3'
        Supplier b = Spy(newSupplier('2'))

        when:
        def c = or(a, b)

        then:
        0 * b.get()
        '3' == c
    }

    def "Or: both null"() {
        def a = null
        def b = null
        when:
        or(a, b)
        then:
        thrown(NullPointerException)
    }

    private static Supplier newSupplier(Object o) {
        return new Supplier() {
            @Override
            Object get() {
                return o
            }
        }
    }
}
