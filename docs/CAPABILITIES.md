# CAPABILITIES

This file documents the real and intended capabilities of the application.

## Real
- Local network discovery via mDNS / SSDP / ARP / TCP port probes
- Offline vendor mapping based on OUI prefixes
- Device classification based on signatures and port openness
- Compose interface for radar and terminal operations

## Not implemented or not real on stock Android without root
- Monitor mode Wi-Fi sniffing
- Global packet capture without VPN or privileged host access
- System-wide network injection or remote takeover

## Security note
This project uses safe, explicit discovery flows and does not claim root-level interception.
