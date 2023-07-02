package com.willor.sentinel_v2.presentation.uoa.uoa_components

import com.willor.lib_data.domain.dataobjs.UoaFilterOptions


sealed class UoaScreenEvent{
    object InitialLoad: UoaScreenEvent()
    data class RemoveFromSentinelWatchlistClicked(val ticker: String): UoaScreenEvent()
    data class TickerFromSentinelWatchlistClicked(val ticker: String): UoaScreenEvent()
    data class SortByOptionClicked(val sortByOption: UoaFilterOptions): UoaScreenEvent()
    data class SortAscDscClicked(val sortAsc: Boolean): UoaScreenEvent()
}