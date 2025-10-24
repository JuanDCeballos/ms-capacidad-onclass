//package co.onclass.consumer;
//
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.io.IOException;
//
//
//class TecnologiaRestConsumerTest {
//
//    private static TecnologiaRestConsumer tecnologiaRestConsumer;
//
//    private static MockWebServer mockBackEnd;
//
//
//    @BeforeAll
//    static void setUp() throws IOException {
//        mockBackEnd = new MockWebServer();
//        mockBackEnd.start();
//        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
//        tecnologiaRestConsumer = new TecnologiaRestConsumer(webClient);
//    }
//
//    @AfterAll
//    static void tearDown() throws IOException {
//
//        mockBackEnd.shutdown();
//    }
//}