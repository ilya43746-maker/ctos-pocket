package ai.ctos.pocket.core.net

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient

public data class NetworkTimeouts(
    val connectMs: Long = 3_000L,
    val readMs: Long = 5_000L,
    val writeMs: Long = 5_000L,
)

public object NetworkDefaults {
    public val timeouts: NetworkTimeouts = NetworkTimeouts()
}

public class NetworkClientFactory {
    public fun build(timeouts: NetworkTimeouts = NetworkDefaults.timeouts): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(timeouts.connectMs, TimeUnit.MILLISECONDS)
            .readTimeout(timeouts.readMs, TimeUnit.MILLISECONDS)
            .writeTimeout(timeouts.writeMs, TimeUnit.MILLISECONDS)
            .callTimeout(timeouts.readMs + timeouts.writeMs + timeouts.connectMs, TimeUnit.MILLISECONDS)
            .build()
}
