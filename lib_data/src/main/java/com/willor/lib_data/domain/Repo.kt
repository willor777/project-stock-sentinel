package com.willor.lib_data.domain

import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.UoaFilterOptions
import com.willor.lib_data.domain.dataobjs.entities.TriggerEntity
import com.willor.lib_data.domain.dataobjs.responses.authentication_resp.LoginResponse
import com.willor.lib_data.domain.dataobjs.responses.authentication_resp.RegistrationResponse
import com.willor.lib_data.domain.dataobjs.responses.chart_resp.StockChart
import com.willor.lib_data.domain.dataobjs.responses.etf_quote_resp.EtfQuote
import com.willor.lib_data.domain.dataobjs.responses.major_futures_resp.MajorFutures
import com.willor.lib_data.domain.dataobjs.responses.major_indices_resp.MajorIndices
import com.willor.lib_data.domain.dataobjs.responses.options_overview_resp.OptionsOverview
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_options_resp.PopularWatchlistOptions
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp.PopularWatchlist
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_search_resp.PopularWatchlistSearch
import com.willor.lib_data.domain.dataobjs.responses.stock_competitors_resp.StockCompetitors
import com.willor.lib_data.domain.dataobjs.responses.stock_quote_resp.StockQuote
import com.willor.lib_data.domain.dataobjs.responses.stock_snr_levels_resp.StockSnrLevels
import com.willor.lib_data.domain.dataobjs.responses.uoa_page_resp.UoaPage
import kotlinx.coroutines.flow.Flow

interface Repo {

    fun registerNewUser(email: String, password: String): Flow<DataResourceState<RegistrationResponse>>

    fun loginUser(email: String, password: String): Flow<DataResourceState<LoginResponse>>

    fun getStockChart(
        ticker: String, interval: String, periodRange: String, prepost: Boolean
    ): Flow<DataResourceState<StockChart?>>

    fun getMajorFutures(): Flow<DataResourceState<MajorFutures?>>

    fun getMajorIndices(): Flow<DataResourceState<MajorIndices?>>

    fun getStockCompetitors(ticker: String): Flow<DataResourceState<StockCompetitors?>>

    fun getStockSnrLevels(ticker: String): Flow<DataResourceState<StockSnrLevels?>>

    fun getOptionsOverview(ticker: String): Flow<DataResourceState<OptionsOverview?>>

    fun getUnusualOptionsActivity(
        page: Int = 0,
        sortAsc: Boolean = true,
        sortBy: UoaFilterOptions = UoaFilterOptions.Volume_OI_Ratio
    ): Flow<DataResourceState<UoaPage?>>

    suspend fun getUoa(
        page: Int = 0,
        sortAsc: Boolean = true,
        sortBy: UoaFilterOptions = UoaFilterOptions.Volume_OI_Ratio
    ): UoaPage?

    fun getStockQuote(ticker: String): Flow<DataResourceState<StockQuote?>>

    fun getEtfQuote(ticker: String): Flow<DataResourceState<EtfQuote?>>

    fun getAllAvailablePopularWatchlistOptions(): Flow<DataResourceState<PopularWatchlistOptions?>>

    fun getPopularWatchlist(wlName: String): Flow<DataResourceState<PopularWatchlist?>>

    fun getPopularWatchlistBySearchTags(searchTags: String): Flow<DataResourceState<PopularWatchlistSearch?>>

    fun getUserPreferences(): Flow<DataResourceState<UserPreferences>>

    suspend fun saveUserPreferences(userPrefs: UserPreferences)

    fun getTriggers(
        id: Int? = null,
        fromTime: Long? = null,
        toTime: Long? = null,
        ticker: String? = null,
        onlyLong: Boolean = false,
        onlyShort: Boolean = false,
    ): Flow<List<TriggerEntity>?>

    suspend fun saveTriggers(vararg triggers: TriggerEntity)
}










