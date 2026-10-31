# ARCHITECTURE

This project follows a layered structure with Clean Architecture and Compose UI.

- `domain` holds contracts and pure domain models.
- `data` contains concrete detectors, exploit implementations, and persistence adapters.
- `core` stores utility libraries and platform primitives.
- `feature` implements screens and terminal flows.
- `app` wires the navigation and dependencies.

The discovery stack is intentionally offline-first and does not rely on remote services.

# CAPABILITIES

| Capability | State |
| --- | --- |
| mDNS scan | Implemented |
| SSDP discovery | Implemented |
| ARP parsing | Implemented |
| TCP port probing | Implemented |
| BLE beacon classification | Implemented |
| NFC tag parsing skeleton | Implemented |
| HTTP fingerprinting | Implemented |
| Exploit registry | Implemented |

# THREAT_MODEL

This project is an engineering research and tooling prototype for local device discovery and device profile analysis on an authorized test network only. It intentionally avoids invasive or root-only capabilities and documents constraints explicitly.

# LEGAL

This repository is for research, local audit, and legitimate device discovery in controlled environments. It does not include or endorse unauthorized network intrusion, malware, or remote exploitation against third parties.
