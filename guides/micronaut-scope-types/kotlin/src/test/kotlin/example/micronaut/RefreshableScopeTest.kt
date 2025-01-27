//tag::imports[]
package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.core.type.Argument
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


@Property(name = "endpoints.refresh.enabled", value = StringUtils.TRUE) // <1>
@Property(name = "endpoints.refresh.sensitive", value = StringUtils.FALSE) // <2>
@MicronautTest // <3>
class RefreshableScopeTest(@Client("/") val httpClient: HttpClient) { // <4>
//end::imports[]

    //tag::test[]
    @ParameterizedTest
    @ValueSource(strings = ["/refreshable"])
    fun refreshableScopeIsACustomScopeThatAllowsABeansStateToBeRefreshedViaTheRefreshEndpoint(path: String) {
        val responses = executeRequest(httpClient, path).toMutableSet()
        assertEquals(1, responses.size) // <5>
        responses.addAll(executeRequest(httpClient, path))
        assertEquals(1, responses.size) // <6>
        refresh() // <7>
        responses.addAll(executeRequest(httpClient, path))
        assertEquals(2, responses.size) // <8>
    }

    private fun executeRequest(client: HttpClient, path: String): List<String> {
        return client.toBlocking().retrieve(
            HttpRequest.GET<Any>(path),
            Argument.listOf(String::class.java)
        )
    }

    private fun refresh() {
        httpClient.toBlocking().exchange<Any, Any>(
            HttpRequest.POST(
                "/refresh",
                mapOf("force" to true)
            )
        )
    }

}
//end::test[]