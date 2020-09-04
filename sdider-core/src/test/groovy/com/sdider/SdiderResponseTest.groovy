package com.sdider

import com.sdider.crawler.Item
import com.sdider.crawler.Request
import spock.lang.Specification

class SdiderResponseTest extends Specification {
    SdiderResponse response
    SdiderResult result

    void setup() {
        result = Mock()
        response = Mock {
            items(_) >> {
                callRealMethod()
            }
            targets(_) >> {
                callRealMethod()
            }
        }
    }

    def "Items"() {
        Item item = Mock()

        when:
        response.items {
            addItem(item)
        }

        then:
        1 * response.getResult() >> result
        1 * result.addItem(item)
    }

    def "Targets"() {
        Request request = Mock()

        when:
        response.targets {
            addRequest(request)
        }

        then:
        1 * response.getResult() >> result
        1 * result.addRequest(request)
    }
}
