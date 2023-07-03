package com.willor.sentinel_v2.presentation.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.usecases.UseCases
import com.willor.sentinel_v2.presentation.dashboard.dash_components.DashboardUiEvent
import com.willor.sentinel_v2.presentation.dashboard.dash_components.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

/* TODO
*   There is a problem with UserPrefs not loading. It causes 'loadDefaultWatchlist()' to get hung
*       up waiting for UserPrefs.defaultWatchlist.
* */

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    val tag: String = DashboardViewModel::class.java.simpleName

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        Log.d(tag, "Initialized.")
    }

    /**
     * Handles ALL Events that occur with the Dashboard UI
     */
    fun handleEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.InitialLoad -> {
                // Load user prefs
                loadUserPrefs()

                // Load major futures
                loadMajorFuturesData()

                // Load major indices
                loadMajorIndicesData()

                // Load Wl Options
                loadPopularWatchlistOptions()

                // Load Default Wl
                loadDefaultWatchlist()
            }

            is DashboardUiEvent.RefreshData -> {
                loadMajorFuturesData()
                loadMajorIndicesData()

                val curWatchlist =
                    (_uiState.value.userPrefs as DataResourceState.Success).data.lastPopularWatchlistSelected
                loadWatchlist(curWatchlist)
            }

            is DashboardUiEvent.WatchlistOptionClicked -> {
                val wlName = event.name
                loadWatchlist(wlName)
                setUserPrefLastWatchlistSelected(wlName)
            }

            is DashboardUiEvent.AddTickerToSentinelWatchlist -> {
                addTickerToSentinelWatchlist(event.ticker)
            }

            is DashboardUiEvent.RemoveTickerFromSentinelWatchlist -> {
                removeTickerFromSentinelWatchlist(event.ticker)
            }

            // Only here for the 'non-exhaustive when statement'
            else -> {

            }
        }
        Log.d(tag, "Dashboard Ui Event triggered. Event: $event")
    }


    /**
     * Adds a ticker to the user prefs list
     */
    private fun addTickerToSentinelWatchlist(ticker: String) {
        viewModelScope.launch(Dispatchers.IO) {

            if (_uiState.value.userPrefs is DataResourceState.Success){
                // Get cur watchlist, check if list already contains ticker
                val userPrefs = (_uiState.value.userPrefs as DataResourceState.Success).data
                val curList = userPrefs.sentinelWatchlist.toMutableList()
                if (curList.contains(ticker)) {
                    return@launch
                }

                // Add ticker + update
                curList.add(ticker)
                userPrefs.sentinelWatchlist = curList.toList()
                updateUserPrefs(userPrefs)

                Log.d(tag, "Ticker added to user prefs watchlist: $ticker")
            }
        }
    }


    private fun removeTickerFromSentinelWatchlist(ticker: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val userPrefs = (_uiState.value.userPrefs as DataResourceState.Success).data
            val curList = userPrefs.sentinelWatchlist.toMutableList()
            curList.remove(ticker)
            userPrefs.sentinelWatchlist = curList
            updateUserPrefs(userPrefs)

            Log.d(tag, "Ticker removed from user prefs watchlist: $ticker")
        }
    }


    // TODO Figure out a better way to wait for user prefs to load.
    /**
     * Waits for user prefs to load, then loads the last watchlist user selected.
     */
    private fun loadDefaultWatchlist() {
        viewModelScope.launch(Dispatchers.IO) {

            // Wait for user prefs to load
            delay(5000)

            // TODO - Note - The below code block causes an infinite loop. It continues to loop
            //      even if user prefs load success
//            while (this.isActive) {
//                if (_uiState.value.userPrefs is DataResourceState.Success) {
//                    break
//                }
//                Log.d(tag, "loadDefaultWatchlist() waiting for user prefs")
//                delay(500)
//            }

            // Cast userPrefs and load the watchlist last selected
            val userPrefs = (_uiState.value.userPrefs as DataResourceState.Success).data
            loadWatchlist(userPrefs.lastPopularWatchlistSelected)
            Log.d(tag, "Watchlist Loaded")
        }
    }


    /**
     * Collects user preferences flow from repo/datastore
     */
    private fun loadUserPrefs() {

        viewModelScope.launch(Dispatchers.IO) {

            val up = useCases.getUserPreferencesUsecase().first()
            _uiState.value = _uiState.value.copy(
                userPrefs = up
            )
            Log.d(tag, "UserPrefs Loaded Successfully")
        }
    }


    /**
     * Updates the local copy of userPrefs, then updates the data store
     */
    private fun updateUserPrefs(userPrefs: UserPreferences) {
        viewModelScope.launch(Dispatchers.IO) {

            // Local update
            _uiState.value = _uiState.value.copy(
                userPrefs = DataResourceState.Success(userPrefs)
            )

            // Update datastore
            useCases.saveUserPreferencesUsecase(userPrefs)

            Log.d(tag, "UserPrefs Updated Successfully")
        }
    }


    /**
     * Updates the last selected watchlist in user prefs so that it can be loaded on the next screen load
     */
    private fun setUserPrefLastWatchlistSelected(wlName: String) {
        // Set wlName as default in prefs
        val prefs = (_uiState.value.userPrefs as DataResourceState.Success).data
        prefs.lastPopularWatchlistSelected = wlName
        updateUserPrefs(prefs)
        Log.d(tag, "UserPrefs lastPopularWatchlistSelected changed to: $wlName")

    }


    /**
     * Loads futures data from Repo. Will be called on a loop using Launched Effect to refresh.
     */
    private fun loadMajorFuturesData() {

        viewModelScope.launch(Dispatchers.IO) {

            useCases.getMajorFuturesUsecase().collectLatest {

                Log.d(tag, "Major Futures Data: $it")

                _uiState.value = _uiState.value.copy(
                    majorFutures = it
                )

                Log.d(tag, "MajorFutures Loaded Successfully")
            }
        }
    }


    /**
     * Loads indices data from Repo. Will be called on a loop using Launched Effect to refresh.
     */
    private fun loadMajorIndicesData() {

        viewModelScope.launch(Dispatchers.IO) {
            useCases.getMajorIndicesUsecase().collectLatest {
                _uiState.value = _uiState.value.copy(
                    majorIndices = it
                )

                Log.d(tag, "MajorIndices Loaded Successfully")
            }
        }
    }


    /**
     * Loads the PopularWatchlistOptions from Repo. When Success -> loadWatchlist() is called
     * with the first watchlist in the options list.
     */
    private fun loadPopularWatchlistOptions() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getPopularWatchlistOptionsUsecase().collectLatest {

                _uiState.value = _uiState.value.copy(
                    popularWatchlistOptions = it
                )

                Log.d(tag, "PopularWatchlistOptions Loaded Successfully")
            }
        }
    }


    /**
     * Loads Popular watchlist to be displayed and sets UserPref.defaultWatchlist as wlName
     */
    private fun loadWatchlist(wlName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(tag, "loadWatchlist() called!")
            // Load popular watchlist
            useCases.getPopularWatchlistUsecase(wlName).collectLatest {
                when (it) {
                    is DataResourceState.Success -> {
                        Log.d(tag, "PopularWatchlist Success!")
                    }
                    is DataResourceState.Loading -> {
                        Log.d(tag, "PopularWatchlist Loading!")
                    }
                    is DataResourceState.Error -> {
                        Log.d(
                            tag, "PopularWatchlist Error! Msg: ${it.msg} " +
                                    "Exception: ${it.exception}"
                        )
                    }
                }
                _uiState.value = _uiState.value.copy(
                    popularWatchlistDisplayed = it
                )
            }
        }
    }
}
