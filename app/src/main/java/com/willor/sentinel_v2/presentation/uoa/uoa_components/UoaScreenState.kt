package com.willor.sentinel_v2.presentation.uoa.uoa_components

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.UoaFilterOptions
import com.willor.lib_data.domain.dataobjs.responses.uoa_page_resp.UnusualOption
import kotlinx.coroutines.flow.Flow

@Stable
data class UoaScreenState(
    val userPrefs: DataResourceState<UserPreferences> = DataResourceState.Loading(),
    val uoaPagingFlow: Flow<PagingData<UnusualOption>>? = null,
    val sortAsc: Boolean = false,
    val sortBy: UoaFilterOptions = UoaFilterOptions.Volume_OI_Ratio
)
