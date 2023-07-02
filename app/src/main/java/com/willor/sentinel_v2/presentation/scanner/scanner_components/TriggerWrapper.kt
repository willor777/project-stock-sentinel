package com.willor.sentinel_v2.presentation.scanner.scanner_components

import com.willor.lib_data.domain.dataobjs.entities.TriggerEntity
import com.willor.lib_data.domain.dataobjs.responses.stock_quote_resp.StockQuote


/*
TODO
    - This is just a starting point, thinking out loud kind of thing.
 */
data class TriggerWrapper(
    val trigger: TriggerEntity,
    val cardIsExpanded: Boolean,
    val quote: StockQuote? = null,              // Change this to the new generic quote type (ETF + Stock)
    
)
