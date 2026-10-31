package ai.ctos.pocket.core.common

public sealed interface DomainResult<out T> {
    public data class Success<T>(val value: T) : DomainResult<T>
    public data class Failure(val error: DomainError) : DomainResult<Nothing>
}

public data class DomainError(
    val code: String,
    val message: String,
    val cause: Throwable? = null,
)

public interface Clock {
    public fun nowEpochMs(): Long
}

public class SystemClock : Clock {
    override fun nowEpochMs(): Long = System.currentTimeMillis()
}

public fun <T> resultOf(block: () -> T): DomainResult<T> =
    try {
        DomainResult.Success(block())
    } catch (t: Throwable) {
        DomainResult.Failure(DomainError("unexpected_error", t.message ?: "Unexpected failure", t))
    }
