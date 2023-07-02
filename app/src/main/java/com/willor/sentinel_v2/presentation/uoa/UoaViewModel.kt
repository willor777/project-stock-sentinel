package com.willor.sentinel_v2.presentation.uoa

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.willor.lib_data.data.UoaPagingSource
import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.UoaFilterOptions
import com.willor.lib_data.domain.usecases.UseCases
import com.willor.sentinel_v2.presentation.uoa.uoa_components.UoaScreenEvent
import com.willor.sentinel_v2.presentation.uoa.uoa_components.UoaScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UoaViewModel @Inject constructor(
    private val usecases: UseCases,
) : ViewModel() {

    private val tag: String = UoaViewModel::class.java.simpleName

    private val _uiState: MutableStateFlow<UoaScreenState> = MutableStateFlow(UoaScreenState())
    val uiState get() = _uiState.asStateFlow()


    init {
        Log.d(tag, "Initialized ")
    }


    fun handleEvent(event: UoaScreenEvent) {
        when (event) {
            is UoaScreenEvent.InitialLoad -> {
                loadUserPrefs()
                initializePager()
            }

            is UoaScreenEvent.SortAscDscClicked -> {
                updateSortAsc(event.sortAsc)
            }

            is UoaScreenEvent.SortByOptionClicked -> {
                updateSortBy(event.sortByOption)
            }

            is UoaScreenEvent.RemoveFromSentinelWatchlistClicked -> {

                removeTickerFromSentinelWatchlist(event.ticker)
            }

            is UoaScreenEvent.TickerFromSentinelWatchlistClicked -> TODO()

        }
    }


    /*
    TODO
        - Initial...
            - Load sort options from datastore prefs
            - Create pager with loaded options and set it for UoaScreenState(uoaPagingFlow=<pager>)
        - Then...
            - If user clicks a new sort option
            - Just create a new pager
     */
    private fun initializePager() {

        viewModelScope.launch(Dispatchers.Default){
            // Wait for user prefs to be loaded
            while(_uiState.value.userPrefs !is DataResourceState.Success){
                Log.d(tag, "initializePager() Waiting for user prefs to load...")
                delay(100L)
            }

            // Initialize pager
            val pagerSource = Pager(
                config = PagingConfig(                      // More config options available here
                    pageSize = 50,
                    prefetchDistance = 50
                ),
                pagingSourceFactory = {                     // This factory function creates a new pager
                    UoaPagingSource(
                        uoaPageProvider = { pageNum: Int ->      // This lambda provides Uoa Pages
                            usecases.getUoaUsecase(
                                page = pageNum,
                                sortAsc = (_uiState.value.userPrefs as DataResourceState.Success).data.uoaSortAsc,
                                sortBy = (_uiState.value.userPrefs as DataResourceState.Success).data.uoaSortBy
                            )
                        }
                    )
                }
            ).flow.cachedIn(viewModelScope)

            _uiState.update { state ->
                state.copy(
                    uoaPagingFlow = pagerSource
                )
            }
        }
    }


    private fun loadUserPrefs() {
        viewModelScope.launch(Dispatchers.IO) {
            usecases.getUserPreferencesUsecase().collectLatest {
                when (it) {
                    is DataResourceState.Loading -> {
                        Log.d(tag, "User Prefs Loading")
                    }
                    is DataResourceState.Error -> {
                        Log.d(tag, "User Prefs Error")
                    }
                    is DataResourceState.Success -> {
                        Log.d(tag, "User Prefs Successfully Loaded")
                        _uiState.update { state ->
                            state.copy(
                                userPrefs = it,
                                sortAsc = it.data.uoaSortAsc,
                                sortBy = it.data.uoaSortBy
                            )
                        }
                    }
                }
            }
        }
    }


    private fun saveUserPrefs(prefs: UserPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            usecases.saveUserPreferencesUsecase(prefs)
            Log.d(tag, "User Prefs Saved!")
        }
    }


    private fun updateSortAsc(sortAscNewValue: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {

            // Shouldn't happen but...Wait for user prefs to load
            while(_uiState.value.userPrefs !is DataResourceState.Success){
                delay(100L)
            }

            // Update user prefs
            val prefs = (_uiState.value.userPrefs as DataResourceState.Success)
            prefs.data.uoaSortAsc = sortAscNewValue
            saveUserPrefs(prefs.data)

            // Re-initialize the pager
            initializePager()
        }
    }


    private fun updateSortBy(sortByNewValue: UoaFilterOptions) {
        viewModelScope.launch(Dispatchers.IO){

            // Shouldn't happen but...Wait for user prefs to load
            while(_uiState.value.userPrefs !is DataResourceState.Success){
                delay(100L)
            }

            // Update user prefs on file
            val prefs = (_uiState.value.userPrefs as DataResourceState.Success)
            prefs.data.uoaSortBy = sortByNewValue
            saveUserPrefs(prefs.data)
//
//            // Update local value
//            _uiState.update { state ->
//                state.copy(userPrefs = prefs)
//            }

            // Re-initialize a pager
            initializePager()
        }
    }

    private fun removeTickerFromSentinelWatchlist(ticker: String){
        viewModelScope.launch(Dispatchers.IO){

            // Verify that prefs are loaded
            while (_uiState.value.userPrefs !is DataResourceState.Success){
                delay(100L)
                Log.d(tag, "removeTickerFromSentinelWatchlist() called and waiting for " +
                        "user prefs to load before removing $ticker")
            }

            // Remove ticker from watchlist if exists
            val prefs = _uiState.value.userPrefs as DataResourceState.Success
            val newList = prefs.data.sentinelWatchlist.toMutableList()
            if (!newList.contains(ticker)){
                return@launch
            }
            newList.remove(ticker)
            prefs.data.sentinelWatchlist = newList

            // Update prefs, (Flow collection will trigger after save)
            saveUserPrefs(prefs.data)
        }
    }
}
