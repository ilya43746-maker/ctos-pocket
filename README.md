# ctos-pocket

Android cyber toolkit prototype for a ctOS Pocket terminal.

## Modules
- `app`
- `core:core-common`
- `core:core-ui`
- `core:core-audio`
- `core:core-haptics`
- `core:core-net`
- `domain:discovery`
- `domain:exploit`
- `data:data-discovery`
- `data:data-exploit`
- `data:data-storage`
- `feature:feature-radar`
- `feature:feature-target`
- `feature:feature-ar`
- `feature:feature-terminal`
- `feature:feature-settings`
- `feature:feature-onboarding`

## Discovery
The discovery layer implements a priority-based detector chain with TTL cache. Detectors cover mDNS, SSDP, ARP, TCP probing, BLE, NFC, and HTTP fingerprinting.

## Exploit
Exploit contracts are defined for match/probe/execute/rollback flows with registry support.

## Build
```bash
./gradlew assembleDebug
```
