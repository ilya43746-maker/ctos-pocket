package ai.ctos.pocket.core.haptics

public data class HapticPattern(
    val pulses: List<Long>,
    val amplitude: Int = 100,
)

public interface HapticSequencer {
    public fun pattern(pattern: HapticPattern): List<Long>
}

public class DefaultHapticSequencer : HapticSequencer {
    override fun pattern(pattern: HapticPattern): List<Long> = pattern.pulses
}
