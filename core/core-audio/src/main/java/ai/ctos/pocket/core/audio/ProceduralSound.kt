package ai.ctos.pocket.core.audio

import kotlin.math.sin

public data class ToneSpec(
    val frequencyHz: Double,
    val durationMs: Long,
    val amplitude: Double = 1.0,
)

public interface ProceduralAudio {
    public fun renderTone(spec: ToneSpec): ByteArray
}

public class SimpleProceduralAudio : ProceduralAudio {
    override fun renderTone(spec: ToneSpec): ByteArray {
        val sampleRate = 44100
        val frames = ((spec.durationMs * sampleRate) / 1000.0).toInt().coerceAtLeast(1)
        val buffer = ByteArray(frames * 2)
        for (i in 0 until frames) {
            val t = i.toDouble() / sampleRate.toDouble()
            val signal = sin(2.0 * Math.PI * spec.frequencyHz * t) * spec.amplitude
            val sample = (signal * 32767).toInt().coerceIn(-32768, 32767)
            val lo = sample and 0xFF
            val hi = (sample shr 8) and 0xFF
            buffer[i * 2] = lo.toByte()
            buffer[i * 2 + 1] = hi.toByte()
        }
        return buffer
    }
}
