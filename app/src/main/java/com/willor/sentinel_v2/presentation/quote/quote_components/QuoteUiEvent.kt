package com.willor.sentinel_v2.presentation.quote.quote_components

sealed interface QuoteUiEvent{
    data class InitialLoad(val ticker: String?): QuoteUiEvent
    data class SearchTextUpdated(val txt: String): QuoteUiEvent
    data class SearchResultClicked(val ticker: String): QuoteUiEvent
    object RefreshCurrentData: QuoteUiEvent
    data class AddTickerToSentinelWatchlist(val ticker: String): QuoteUiEvent
    data class RemoveTickerFromSentinelWatchlist(val ticker: String): QuoteUiEvent
    data class WatchlistTickerClicked(val ticker: String): QuoteUiEvent
}