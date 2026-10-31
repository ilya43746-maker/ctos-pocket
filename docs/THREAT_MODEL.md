# THREAT_MODEL

The app is explicitly designed for local network profiling and target identification against devices that the operator owns or is authorized to test. It is not a general-purpose attack framework.

## Threats considered
- Inaccurate device classification
- Unstable port probes on hostile networks
- High false-positive rates for devices behind NAT

## Countermeasures
- TTL cache and priority ordering for detectors
- Clear UI for confidence and profile
- Domain models with explicit readiness and side-effect mapping
