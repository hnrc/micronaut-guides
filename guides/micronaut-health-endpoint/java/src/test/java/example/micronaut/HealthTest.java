package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest  // <1>
public class HealthTest {

    @Inject
    @Client("/") // <2>
    HttpClient client;

    @Test
    public void healthEndpointExposed() {
        HttpStatus status = client.toBlocking().retrieve(HttpRequest.GET("/health"), HttpStatus.class); // <3>
        assertEquals(HttpStatus.OK, status);
    }
}
