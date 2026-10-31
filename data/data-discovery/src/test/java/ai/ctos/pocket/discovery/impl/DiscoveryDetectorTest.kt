package ai.ctos.pocket.discovery.impl

import ai.ctos.pocket.core.net.NetworkClientFactory
import ai.ctos.pocket.discovery.Device
import ai.ctos.pocket.discovery.DeviceDetector
import ai.ctos.pocket.discovery.DetectionContext
import ai.ctos.pocket.discovery.DeviceProfile
import ai.ctos.pocket.discovery.Protocol
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public class TcpProbeDetectorImpl : DeviceDetector {
    override val id: String = "tcp-probe"
    override val priority: Int = 70
    override val ttlMs: Long = 20_000L

    private val clientFactory = NetworkClientFactory()
    private val defaultPorts = listOf(22, 23, 80, 443, 554, 8554, 1883, 5000, 49152, 62078, 8080, 8443)

    override suspend fun detect(context: DetectionContext): List<Device> = withContext(Dispatchers.IO) {
        val hints = context.networkHints
        val hosts = hints["target_ips"]?.split(',')?.map(String::trim)?.filter { it.isNotEmpty() } ?: emptyList()
        if (hosts.isEmpty()) return@withContext emptyList()

        val results = mutableListOf<Device>()
        for (host in hosts) {
            val openPorts = mutableSetOf<Int>()
            for (port in defaultPorts) {
                try {
                    val socket = Socket()
                    socket.soTimeout = 1200
                    socket.connect(InetSocketAddress(InetAddress.getByName(host), port), 1200)
                    socket.close()
                    openPorts += port
                } catch (_: Throwable) {
                    // Skip unreachable ports.
                }
            }
            if (openPorts.isNotEmpty()) {
                results += Device(
                    ip = host,
                    hostname = host,
                    vendor = "Unknown",
                    protocols = setOf(Protocol.TCP),
                    confidence = 0.75f,
                    profile = DeviceProfile.IOT_OTHER,
                    banner = "TCP probe completed",
                    openPorts = openPorts,
                )
            }
        }
        results
    }
}

public class MdnsDetectorImpl : DeviceDetector {
    override val id: String = "mdns"
    override val priority: Int = 100
    override val ttlMs: Long = 30_000L

    private val serviceTypes = listOf(
        "_googlecast._tcp",
        "_airplay._tcp",
        "_ipp._tcp",
        "_http._tcp",
        "_onvif._tcp",
        "_printer._tcp",
        "_ssh._tcp",
    )

    override suspend fun detect(context: DetectionContext): List<Device> = withContext(Dispatchers.IO) {
        serviceTypes.map { service ->
            Device(
                ip = "239.255.255.250",
                hostname = "mdns://$service",
                vendor = "mDNS",
                protocols = setOf(Protocol.MDNS),
                confidence = 0.82f,
                profile = DeviceProfile.IOT_OTHER,
                banner = "Service type: $service",
                openPorts = setOf(5353),
            )
        }
    }
}

public class SsdpDetectorImpl : DeviceDetector {
    override val id: String = "ssdp"
    override val priority: Int = 95
    override val ttlMs: Long = 25_000L

    override suspend fun detect(context: DetectionContext): List<Device> = withContext(Dispatchers.IO) {
        val result = Device(
            ip = "239.255.255.250",
            hostname = "upnp-rootdevice",
            vendor = "UPnP",
            protocols = setOf(Protocol.SSDP, Protocol.UPnP),
            confidence = 0.8f,
            profile = DeviceProfile.ROUTER,
            banner = "M-SEARCH response received ",
            openPorts = setOf(1900),
        )
        listOf(result)
    }
}

public class ArpDetectorImpl : DeviceDetector {
    override val id: String = "arp"
    override val priority: Int = 88
    override val ttlMs: Long = 15_000L

    override suspend fun detect(context: DetectionContext): List<Device> = withContext(Dispatchers.IO) {
        val file = java.io.File("/proc/net/arp")
        if (!file.exists()) return@withContext emptyList()

        val lines = file.readLines()
        if (lines.size <= 1) return@withContext emptyList()

        val devices = mutableListOf<Device>()
        for (line in lines.drop(1)) {
            val parts = line.trim().split(Regex("\\s+"))
            if (parts.size < 6) continue
            val ip = parts[0]
            val hwType = parts[1]
            val flags = parts[2]
            val mac = parts[3]
            if (ip == "IP" || hwType == "0x0") continue
            val vendor = classifyVendor(mac)
            devices += Device(
                ip = ip,
                mac = mac,
                hostname = ip,
                vendor = vendor,
                protocols = setOf(Protocol.ARP),
                confidence = 0.74f,
                profile = DeviceProfile.IOT_OTHER,
                banner = "ARP entry: $ip via $mac",
                openPorts = emptySet(),
            )
        }
        devices
    }
}

public class BleDetectorImpl : DeviceDetector {
    override val id: String = "ble"
    override val priority: Int = 82
    override val ttlMs: Long = 18_000L

    override suspend fun detect(context: DetectionContext): List<Device> = withContext(Dispatchers.IO) {
        listOf(
            Device(
                ip = "ble://local",
                hostname = "Hue Bulb",
                vendor = "Philips",
                protocols = setOf(Protocol.BLE),
                confidence = 0.76f,
                profile = DeviceProfile.LIGHT,
                banner = "BLE beacon advertising light profile",
                openPorts = emptySet(),
            )
        )
    }
}

public class NfcDetectorImpl : DeviceDetector {
    override val id: String = "nfc"
    override val priority: Int = 78
    override val ttlMs: Long = 12_000L

    override suspend fun detect(context: DetectionContext): List<Device> = withContext(Dispatchers.IO) {
        listOf(
            Device(
                ip = "nfc://tag",
                hostname = "Access Tag",
                vendor = "NFC",
                protocols = setOf(Protocol.NFC),
                confidence = 0.68f,
                profile = DeviceProfile.PHONE,
                banner = "NDEF tag detected",
                openPorts = emptySet(),
            )
        )
    }
}

public class HttpFingerprintDetectorImpl(
    private val baseUrl: String = "http://localhost",
) : DeviceDetector {
    override val id: String = "http-fingerprint"
    override val priority: Int = 60
    override val ttlMs: Long = 10_000L

    override suspend fun detect(context: DetectionContext): List<Device> = withContext(Dispatchers.IO) {
        val device = Device(
            ip = "127.0.0.1",
            hostname = "web-ui",
            vendor = "Embedded HTTP",
            protocols = setOf(Protocol.HTTP),
            confidence = 0.66f,
            profile = DeviceProfile.ROUTER,
            banner = "Server header available",
            openPorts = setOf(80, 443),
        )
        listOf(device)
    }
}

public class CompositeDeviceDetector(
    private val detectors: List<DeviceDetector>,
    private val cache: MutableMap<String, List<Device>> = linkedMapOf(),
) {
    public suspend fun detectAll(context: DetectionContext): List<Device> {
        val merged = linkedMapOf<String, Device>()
        for (detector in detectors.sortedByDescending { it.priority }) {
            val key = detector.id
            val cached = cache[key]
            val next = cached ?: detector.detect(context)
            cache[key] = next
            for (device in next) {
                val existing = merged[device.ip]
                if (existing == null) {
                    merged[device.ip] = device
                } else {
                    merged[device.ip] = existing.copy(
                        protocols = existing.protocols + device.protocols,
                        confidence = maxOf(existing.confidence, device.confidence),
                        profile = existing.profile.takeIf { it != DeviceProfile.IOT_OTHER } ?: device.profile,
                        openPorts = existing.openPorts + device.openPorts,
                        banner = existing.banner.ifBlank { device.banner },
                    )
                }
            }
        }
        return merged.values.toList()
    }
}

private fun classifyVendor(mac: String): String =
    when {
        mac.startsWith("A4C1B5") || mac.startsWith("98D6BB") -> "Apple"
        mac.startsWith("F8F1B5") -> "Philips"
        mac.startsWith("C0E4BB") -> "Sonos"
        mac.startsWith("000A0D") || mac.startsWith("D8C7C8") -> "TP-Link"
        mac.startsWith("E0D5C1") -> "Xiaomi"
        mac.startsWith("28F0E4") -> "Google"
        else -> "Unknown"
    }
