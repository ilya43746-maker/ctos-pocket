package ai.ctos.pocket.discovery

import java.util.Locale

public enum class Protocol {
    MDNS,
    SSDP,
    ARP,
    TCP,
    BLE,
    NFC,
    HTTP,
    SSH,
    RTSP,
    MQTT,
    UPnP,
    OTHER,
}

public enum class DeviceProfile {
    CAMERA,
    LIGHT,
    SPEAKER,
    TV,
    ROUTER,
    PRINTER,
    PHONE,
    IOT_OTHER,
}

public data class Device(
    val ip: String,
    val mac: String = "",
    val hostname: String = "",
    val vendor: String = "",
    val protocols: Set<Protocol> = emptySet(),
    val confidence: Float = 0.0f,
    val profile: DeviceProfile = DeviceProfile.IOT_OTHER,
    val banner: String = "",
    val openPorts: Set<Int> = emptySet(),
) {
    public val normalizedHostname: String
        get() = hostname.ifBlank { ip }

    public val displayName: String
        get() = hostname.ifBlank { vendor.ifBlank { ip } }
}

data class DetectionContext(
    val source: String = "local",
    val networkHints: Map<String, String> = emptyMap(),
    val nowEpochMs: Long = System.currentTimeTimeMillis(),
)

public interface DeviceDetector {
    public val id: String
    public val priority: Int
    public val ttlMs: Long
    public suspend fun detect(context: DetectionContext): List<Device>
}

public interface ExpiringDetectorCache {
    public fun get(key: String): List<Device>
    public fun put(key: String, devices: List<Device>, ttlMs: Long)
    public fun evictExpired(nowEpochMs: Long)
}

public class InMemoryDetectorCache : ExpiringDetectorCache {
    private data class Entry(val value: List<Device>, val expiresAtMs: Long)
    private val entries = linkedMapOf<String, Entry>()

    override fun get(key: String): List<Device> =
        entries[key]?.takeIf { it.expiresAtMs > System.currentTimeMillis() }?.value ?: emptyList()

    override fun put(key: String, devices: List<Device>, ttlMs: Long) {
        entries[key] = Entry(devices, System.currentTimeMillis() + ttlMs)
    }

    override fun evictExpired(nowEpochMs: Long) {
        entries.entries.removeIf { it.value.expiresAtMs <= nowEpochMs }
    }
}

public fun Device.withProtocol(protocol: Protocol): Device =
    copy(protocols = protocols + protocol)

public fun classifyVendor(mac: String): String {
    val clean = mac.filter { it.isLetterOrDigit() }.uppercase(Locale.US)
    if (clean.length < 6) return "Unknown"
    val oui = clean.substring(0, 6)
    return OUI_MAP[oui] ?: "Unknown"
}

private val OUI_MAP = mapOf(
    "001B2F" to "Cisco",
    "A0B3CC" to "Samsung",
    "A4C1B5" to "Apple",
    "98D6BB" to "Apple",
    "000A0D" to "TP-Link",
    "D8C7C8" to "TP-Link",
    "E0D5C1" to "Xiaomi",
    "28F0E4" to "Google",
    "F8F1B5" to "Philips",
    "00E0B4" to "Intel",
    "C0E4BB" to "Sonos",
    "B0C4D2" to "Raspberry Pi",
    "00A0C9" to "Hikvision",
    "CC7A06" to "Google",
    "6C5AB3" to "TP-Link",
    "703D5A" to "Sony",
)
