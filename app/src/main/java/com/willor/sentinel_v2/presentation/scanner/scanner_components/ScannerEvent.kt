package com.willor.sentinel_v2.presentation.scanner.scanner_components

interface ScannerEvent {
    object InitialLoad: ScannerEvent
    data class TriggerClicked(val id: Int): ScannerEvent
    object StartScannerClicked: ScannerEvent
}