package ai.ctos.pocket.discovery

public interface DetectorRegistry {
    public fun orderedDetectors(): List<DeviceDetector>
    public fun register(detector: DeviceDetector)
}

public class DefaultDetectorRegistry : DetectorRegistry {
    private val detectors = mutableListOf<DeviceDetector>()

    override fun orderedDetectors(): List<DeviceDetector> =
        detectors.sortedByDescending { it.priority }

    override fun register(detector: DeviceDetector) {
        detectors.add(detector)
    }
}
