package com.willor.sentinel_v2.presentation.scanner

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.usecases.UseCases
import com.willor.sentinel_v2.presentation.scanner.scanner_components.ScannerEvent
import com.willor.sentinel_v2.presentation.scanner.scanner_components.ScannerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ScannerViewModel @Inject constructor(
    val useCases: UseCases,
) : ViewModel() {

    private val tag = "ScannerViewModel"

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState get() = _uiState.asStateFlow()

    private lateinit var triggerFlowCollectorJob: Job


    init {
        Log.d(tag, "Initialized!")
    }


    fun handleEvent(event: ScannerEvent) {

        when (event) {
            is ScannerEvent.InitialLoad -> {
                // Todo
                //  - Load User Prefs
                //  - Start flow of triggers
                //  - Maybe load MajorIndices/Futures (Idk yet)
                startUserPrefsFlowCollection()
                startTriggerFlowCollection()
            }
            is ScannerEvent.TriggerClicked -> {
                // Todo
                //  - Load quote so that current data can be compared to "time of trigger"
            }
            is ScannerEvent.StartScannerClicked -> {

            }
        }
    }


    private fun startUserPrefsFlowCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getUserPreferencesUsecase().collectLatest {
                when (it) {

                    // Update _uiState UserPreferences
                    is DataResourceState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                userPrefs = it
                            )
                        }
                        Log.d(tag, "User Prefs Flow Collection Triggered! Loaded Successfully")
                    }

                    // Log the collections
                    is DataResourceState.Loading -> {
                        Log.d(tag, "User Prefs Flow Collection Triggered! Currently Loading")
                    }
                    is DataResourceState.Error -> {
                        Log.d(tag, "User Prefs Flow Collection Triggered! Error Loading")
                    }
                }
            }
        }
    }


    private fun startTriggerFlowCollection() {
        triggerFlowCollectorJob = viewModelScope.launch(Dispatchers.IO) {
            useCases.getTriggersUsecase().collectLatest {
                when {
                    // IS NOT null or emtpy: Trigger List
                    !it.isNullOrEmpty() -> {
                        _uiState.update { state ->
                            state.copy(triggers = it)
                        }
                        Log.d(tag, "Triggers Load Success! Size: ${it.size}")
                    }

                    // IS null or empty: Blank List
                    else -> {
                        _uiState.update { state ->
                            state.copy(triggers = listOf())
                        }
                        Log.d(tag, "Triggers Loaded Empty List!")
                    }
                }
            }
        }
    }


    private suspend fun fetchStockQuote(ticker: String): Boolean {
        val coroutineSuccess = viewModelScope.async(Dispatchers.IO) {
            var quoteSuccess = false

            // Attempt stock quote first
            useCases.getStockQuoteUsecase(ticker).collectLatest {
                when (it) {
                    is DataResourceState.Success -> {

                        // Set the success flag so that no EtfQuote is requested
                        quoteSuccess = true

                        // Pull out the cur map of stock quotes, add this data to it
                        val quoteMap = _uiState.value.stockQuotes.toMutableMap()
                        quoteMap[ticker] = it.data!!

                        // Update the _uiState with the updated map
                        _uiState.update { state ->
                            state.copy(
                                stockQuotes = quoteMap.toMap()
                            )
                        }
                    }
                    is DataResourceState.Loading -> {
                        Log.d(tag, "StockQuote Loading for: $ticker")
                    }
                    is DataResourceState.Error -> {
                        Log.d(tag, "StockQuote Error for: $ticker...Attempting ETF Quote")
                    }
                }
            }

            return@async quoteSuccess
        }
        return coroutineSuccess.await()
    }

}

















