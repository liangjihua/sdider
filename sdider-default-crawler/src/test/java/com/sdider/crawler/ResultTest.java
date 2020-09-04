package com.sdider.crawler;

public class ResultTest {
//    protected Result result;
//
//    @BeforeEach
//    protected void setUp() {
//        Result temp = create();
//        MockingDetails mockingDetails = mockingDetails(temp);
//        if (mockingDetails.isMock() || mockingDetails.isSpy()) {
//            result = temp;
//        } else {
//            result = Mockito.spy(temp);
//        }
//    }
//
//    protected Result create() {
//        return mock(Result.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
//    }
//
//    @Test
//    void consume() {
//        Item item = mock(Item.class);
//        Item item1 = mock(Item.class);
//        when(result.getItems()).thenReturn(Arrays.asList(item, item1));
//        Pipeline pipeline = mock(Pipeline.class);
//        result.consume(pipeline);
//
//        verify(item).consume(same(pipeline));
//        verify(item1).consume(same(pipeline));
//    }
}