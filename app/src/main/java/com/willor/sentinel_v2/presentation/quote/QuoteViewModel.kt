package com.willor.sentinel_v2.presentation.quote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.usecases.UseCases
import com.willor.lib_data.utils.TickerSymbolLoader
import com.willor.sentinel_v2.presentation.quote.quote_components.QuoteUiEvent
import com.willor.sentinel_v2.presentation.quote.quote_components.QuoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val usecases: UseCases
) : ViewModel() {

    private val tag: String = QuoteViewModel::class.java.simpleName

    private val _uiState: MutableStateFlow<QuoteUiState> = MutableStateFlow(QuoteUiState())
    val uiState: StateFlow<QuoteUiState> get() = _uiState.asStateFlow()

    private var bigListOfCompanies: List<List<String>>? = null

    private var curQuoteType = QuoteType.UNKNOWN

    /**
     * Responsible for handling all interaction events dealing with the UI.
     */
    fun handleUiEvent(event: QuoteUiEvent) {

        when (event) {
            is QuoteUiEvent.InitialLoad -> {
                loadUserPrefs()
                if (event.ticker != null){
                    updateCurrentTicker(event.ticker)
                    loadDataForTicker()
                }
            }

            is QuoteUiEvent.SearchTextUpdated -> {
                updateSearchText(event.txt)
                updateSearchResults()
            }

            is QuoteUiEvent.SearchResultClicked -> {
                updateCurrentTicker(event.ticker)
                updateSearchText("")
                loadDataForTicker()
            }

            is QuoteUiEvent.AddTickerToSentinelWatchlist -> {
                addTickerToSentinelWatchlist(event.ticker)
            }

            is QuoteUiEvent.RemoveTickerFromSentinelWatchlist -> {
                removeTickerFromSentinelWatchlist(event.ticker)
            }

            is QuoteUiEvent.WatchlistTickerClicked -> {
                updateCurrentTicker(event.ticker)
                loadDataForTicker()
            }

            // TODO
            is QuoteUiEvent.RefreshCurrentData -> {

            }
        }
        Log.d(tag, "handleEvent() called: Event -> $event")
    }


    private fun loadDataForTicker(){
        loadQuote()
        loadOptionsOverview()
        loadCompetitors()
        loadSnrLevels()
    }


    private fun loadSnrLevels(){

        // Check what the current ticker is in uiState
        val ticker = _uiState.value.currentTicker

        // Launch a coroutine to reteive data from api
        viewModelScope.launch(Dispatchers.IO){

            usecases.getSnrLevelsUsecase(ticker).collect {
                when(it){
                    is DataResourceState.Success ->{
                        _uiState.update { uiState ->
                            uiState.copy(
                                snrLevels = it
                            )
                        }
                    }

                    is DataResourceState.Loading -> {
                        Log.d(tag, "Snr Levels Loading For $ticker")
                    }

                    else -> {
                        Log.d(tag, "Snr Levels Failed To Load For $ticker")
                    }
                }
            }
        }
    }


    private fun loadCompetitors(){
        val ticker = _uiState.value.currentTicker

        viewModelScope.launch(Dispatchers.IO){
            usecases.getStockCompetitorsUsecase(ticker).collectLatest {
                when(it){
                    is DataResourceState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                competitors = it
                            )
                        }
                        Log.d(tag, "Competitors Loaded Successfully For $ticker")
                    }

                    is DataResourceState.Loading -> {
                        Log.d(tag, "Competitors Loading For $ticker")
                    }

                    else -> {
                        Log.d(tag, "Competitors Failed To Load For $ticker")
                    }
                }
            }
        }
    }


    private fun loadUserPrefs(){
        viewModelScope.launch(Dispatchers.IO){
            usecases.getUserPreferencesUsecase().collectLatest {newPrefs ->
                _uiState.update {
                    it.copy(
                        userPrefs = newPrefs
                    )
                }
                Log.d(tag, "User Prefs Loaded: $newPrefs")
            }
        }
    }


    private fun saveUserPrefs(userPrefs: UserPreferences){
        viewModelScope.launch(Dispatchers.IO){
            usecases.saveUserPreferencesUsecase(userPrefs)
            Log.d(tag, "User Prefs Saved: $userPrefs")
        }
    }


    private fun addTickerToSentinelWatchlist(ticker: String){
        viewModelScope.launch(Dispatchers.IO){

            // Verify that user prefs have been loaded
            if (_uiState.value.userPrefs is DataResourceState.Success){
                val curPrefs = _uiState.value.userPrefs as DataResourceState.Success
                val curList = curPrefs.data.sentinelWatchlist
                if (curList.contains(ticker)){
                    Log.d(tag, "Sentinel Watchlist already contains $ticker, Nothing added")
                    return@launch
                }
                curList.toMutableList().add(ticker)
                curPrefs.data.sentinelWatchlist = curList
                saveUserPrefs(curPrefs.data)
                loadUserPrefs()

                Log.d(tag, "Ticker added to sentinel watchlist: $ticker")
            }
        }
    }


    private fun removeTickerFromSentinelWatchlist(ticker: String){
        viewModelScope.launch(Dispatchers.IO){
            if (_uiState.value.userPrefs is DataResourceState.Success){
                val curPrefs = _uiState.value.userPrefs as DataResourceState.Success
                val curList = curPrefs.data.sentinelWatchlist
                if (!curList.contains(ticker)){
                    Log.d(tag, "Sentinel Watchlist does not contain $ticker, Nothing changed")
                    return@launch
                }
                curList.toMutableList().remove(ticker)
                curPrefs.data.sentinelWatchlist = curList
                saveUserPrefs(curPrefs.data)
                loadUserPrefs()
                Log.d(tag, "Ticker added to sentinel watchlist: $ticker")
            }
        }
    }


    private fun loadQuote() {

        val ticker = _uiState.value.currentTicker

        // Shouldn't happen but just in case
        if (ticker.isEmpty()) {
            return
        }

        Log.d(tag, "loadQuote() called for current ticker: $ticker")

        viewModelScope.launch(Dispatchers.IO) {

            when (curQuoteType) {
                QuoteType.STOCK_QUOTE -> {
                    loadStockQuote()
                    loadOptionsOverview()
                }
                QuoteType.ETF_QUOTE -> {
                    loadEtfQuote()
                    loadOptionsOverview()

                }
                QuoteType.UNKNOWN -> {
                    loadUnknownQuote()
                    loadOptionsOverview()
                }
            }
        }
    }


    private fun loadUnknownQuote() {
        viewModelScope.launch(Dispatchers.IO) {

            val ticker = _uiState.value.currentTicker
            var getEtfQuote = false
            Log.d(tag, "loadUnknownQuote() called for $ticker")

            // attempt to retreive a stock quote first
            usecases.getStockQuoteUsecase(ticker).collectLatest {
                when (it) {
                    is DataResourceState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            stockQuote = it
                        )
                    }
                    is DataResourceState.Error -> {
                        getEtfQuote = true
                    }
                    is DataResourceState.Loading -> {

                    }
                }
            }

            // If stock quote fails retrieve etf quote
            if (getEtfQuote) {
                usecases.getEtfQuoteUsecase(ticker).collectLatest {
                    when (it) {
                        is DataResourceState.Success -> {
                            _uiState.value = _uiState.value.copy(
                                etfQuote = it
                            )
                        }
                        is DataResourceState.Loading -> {

                        }
                        is DataResourceState.Error -> {

                        }
                    }
                }
            }
        }
    }


    private fun loadStockQuote() {

        val ticker = _uiState.value.currentTicker

        viewModelScope.launch(Dispatchers.IO) {
            usecases.getStockQuoteUsecase(ticker).collectLatest {
                when (it) {
                    is DataResourceState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            stockQuote = it
                        )
                        Log.d(tag, "StockQuote Loaded Successfully: $ticker")
                        curQuoteType = QuoteType.STOCK_QUOTE
                    }
                    is DataResourceState.Loading -> {
                        Log.d(tag, "StockQuote Loading in Progress: $ticker")
                    }
                    is DataResourceState.Error -> {
                        Log.d(tag, "StockQuote Loading Failed for: $ticker\n" +
                                "Error Message: ${it.msg}\nException: ${it.exception}")
                    }
                }
            }
        }
    }


    private fun loadEtfQuote() {
        val ticker = _uiState.value.currentTicker

        viewModelScope.launch(Dispatchers.IO) {
            usecases.getEtfQuoteUsecase(ticker).collectLatest {
                when (it) {
                    is DataResourceState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            etfQuote = it
                        )
                        Log.d(tag, "EtfQuote Loaded Successfully: $ticker")
                        curQuoteType = QuoteType.ETF_QUOTE
                    }
                    is DataResourceState.Loading -> {
                        Log.d(tag, "EtfQuote Loading in Progress: $ticker")
                    }
                    is DataResourceState.Error -> {
                        Log.d(tag, "EtfQuote Loading Failed for: $ticker\n" +
                                "Error Message: ${it.msg}\nException: ${it.exception}")
                    }
                }
            }
        }
    }


    private fun loadOptionsOverview() {
        val ticker = _uiState.value.currentTicker

        viewModelScope.launch(Dispatchers.IO) {
            usecases.getOptionsOverviewUsecase(ticker).collectLatest {
                when (it) {
                    is DataResourceState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                optionsOverview = it
                            )
                        }

                        Log.d(tag, "OptionsOverview Loaded Successfully: $ticker")
                    }
                    is DataResourceState.Loading -> {
                        Log.d(tag, "OptionsOverview Loading in Progress: $ticker")
                    }
                    is DataResourceState.Error -> {
                        Log.d(tag, "OptionsOverview Loading Failed for: $ticker\n" +
                                "Error Message: ${it.msg}\nException: ${it.exception}")
                    }
                }
            }
        }
    }


    /**
     * Updates the QuotUiState currentTicker field, resets the stock, etf, options quotes
     */
    private fun updateCurrentTicker(ticker: String) {

        // Reset the current data
        _uiState.value = _uiState.value.copy(
            currentTicker = ticker,
            currentSearchResults = listOf(),
            stockQuote = DataResourceState.Loading(),
            etfQuote = DataResourceState.Loading(),
            optionsOverview = DataResourceState.Loading(),
            competitors = DataResourceState.Loading(),
            snrLevels = DataResourceState.Loading()
        )

        // Reset the quote type
        curQuoteType = QuoteType.UNKNOWN
    }


    /**
     * Updates the search text in the QuoteUiState object's state flow.
     * */
    private fun updateSearchText(usrSearchText: String) {
        _uiState.value = _uiState.value.copy(
            currentSearchText = usrSearchText
        )
    }


    /**
     * Checks the bigListOfCompanies for matching search, then updates the QuoteUiState with search results.
     */
    private fun updateSearchResults() {

        // Extract usr search text from _uiState, check ifEmpty()
        val usrSearchText = _uiState.value.currentSearchText
        if (usrSearchText.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                currentSearchResults = listOf()
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            // Load list if not already loaded
            if (bigListOfCompanies.isNullOrEmpty()) {
                bigListOfCompanies = TickerSymbolLoader.loadSymbols()
            }

            // Search list
            val firstChar = usrSearchText[0]
            val startIndex = TickerSymbolLoader.findStartingIndexOfChar(firstChar)
            val searchResults = mutableListOf<List<String>>()

            for (n in startIndex..bigListOfCompanies!!.lastIndex) {

                val (ticker, _, _) = bigListOfCompanies!![n]

                // Check if into a new Char
                if (ticker[0] != firstChar) {
                    break
                }

                // Check if ticker is a match to usr text
                if (ticker.startsWith(usrSearchText)) {
                    searchResults.add(bigListOfCompanies!![n])
                }
            }

            _uiState.value = _uiState.value.copy(
                currentSearchResults = searchResults
            )
        }
    }


    /**
     * Enum Class representing the 3 different quote types: UNKNOWN, STOCK_QUOTE, ETF_QUOTE.
     * Only used within the QuoteViewModel
     */
    private enum class QuoteType{
        UNKNOWN,
        STOCK_QUOTE,
        ETF_QUOTE,
    }
}


