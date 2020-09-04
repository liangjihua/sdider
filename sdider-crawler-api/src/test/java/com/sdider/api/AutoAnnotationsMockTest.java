package com.sdider.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author yujiaxin
 */
public abstract class AutoAnnotationsMockTest {
    private AutoCloseable closeable;

    @BeforeEach
    void injectMock() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void cleanMock() throws Exception {
        closeable.close();
    }
}
