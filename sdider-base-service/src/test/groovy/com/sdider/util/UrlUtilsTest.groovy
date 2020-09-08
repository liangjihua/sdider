package com.sdider.util

import spock.lang.Specification

import static com.sdider.util.UrlUtils.urlJoin

class UrlUtilsTest extends Specification {

    def "urlJoin #a and #b is #c"() {
        expect:
        urlJoin(a, b) == c

        where:
        a                   | b                     || c
        null                | 'http://foo.bar'      || 'http://foo.bar'
        'http://foo.bar'    | null                  || 'http://foo.bar'
        'http://foo.bar'    | 'http://bar.bar'      || 'http://bar.bar'
        'http://foo.bar'    | '/foo'                || 'http://foo.bar/foo'
        'http://foo.bar'    | 'foo'                 || 'http://foo.bar/foo'
        'http://foo.bar'    | '?foo=bar&bar=foo'    || 'http://foo.bar?foo=bar&bar=foo'
        'http://foo.bar'    | 'foo?foo=bar&bar=foo' || 'http://foo.bar/foo?foo=bar&bar=foo'
        'http://foo.bar'    | '/foo?foo=bar&bar=foo'|| 'http://foo.bar/foo?foo=bar&bar=foo'
    }
}
