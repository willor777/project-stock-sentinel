package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.UoaFilterOptions
import com.willor.lib_data.domain.dataobjs.responses.uoa_page_resp.UoaPage

class GetUoaUsecase(
    private val repo: Repo
) {

    suspend operator fun invoke(
        page: Int = 0,
        sortAsc: Boolean = false,
        sortBy: UoaFilterOptions = UoaFilterOptions.Volume_OI_Ratio
    ): UoaPage? = repo.getUoa(
        page = page,
        sortAsc = sortAsc,
        sortBy = sortBy
    )
}