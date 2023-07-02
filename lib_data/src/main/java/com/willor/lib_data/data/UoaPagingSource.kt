package com.willor.lib_data.data

import android.accounts.NetworkErrorException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.willor.lib_data.domain.dataobjs.responses.uoa_page_resp.UnusualOption
import com.willor.lib_data.domain.dataobjs.responses.uoa_page_resp.UoaPage


/**
 * PagingSource for Unusual Options Activity pages.
 *
 * uoaPageProvider: Function that can be called to retrieve a page of Unusual Options by
 * page number. Used a lambda so that the call to get the page can include search options easier.
 */
class UoaPagingSource(
    private val uoaPageProvider: suspend (pageNum: Int) -> UoaPage?
): PagingSource<Int, UnusualOption>() {

    /**
     * Loads the next page using the LoadParams object which keeps track of what page
     * number is next.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnusualOption> {
        // Load first page if key is null
        val curPage = params.key ?: 0

        // Make request for data
        val data = uoaPageProvider(curPage)

        // Check success and return Page/Error
        return if (data != null){
            LoadResult.Page(
                data = data.data.data,
                prevKey = null,         // Only paging forward
                nextKey = curPage + 1
            )
        }else{
            LoadResult.Error(NetworkErrorException())
        }
    }

    /**
     *
     */
    override fun getRefreshKey(state: PagingState<Int, UnusualOption>): Int? {
        return state.anchorPosition?.let{ anchorPos ->
            val anchorPage = state.closestPageToPosition(anchorPos)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}