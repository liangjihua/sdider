package com.sdider.impl.request

import com.sdider.RequestConfig


class DefaultRequestConfigImplTest extends RequestConfigBaseTest {

    @Override
    RequestConfig create() {
        return new DefaultRequestConfigImpl()
    }
}
