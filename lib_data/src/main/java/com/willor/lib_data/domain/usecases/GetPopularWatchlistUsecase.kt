package com.willor.lib_data.domain.usecases

import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp.PopularWatchlist
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp.WatchlistData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.abs

class GetPopularWatchlistUsecase(
    private val repo: Repo
) {

    operator fun invoke(wlName: String): Flow<DataResourceState<PopularWatchlist?>> =
        repo.getPopularWatchlist(wlName).map {
            when (it) {
                is DataResourceState.Loading -> {
                    it
                }
                is DataResourceState.Error -> {
                    it
                }
                is DataResourceState.Success -> {
                    // Sort the tickers list
                    val tickerList = it.data!!.watchlistData.tickers.sortedWith { o1, o2 ->
                        val absOne = abs(o1!!.changePercent)
                        val absTwo = abs(o2!!.changePercent)

                        when {
                            absOne > absTwo -> {
                                1
                            }
                            absOne < absTwo -> {
                                -1
                            }
                            else -> {
                                0
                            }
                        }
                    }.reversed()

                    // make a new object with sorted list
                    DataResourceState.Success(
                        data = PopularWatchlist(
                            lastUpdated = it.data.lastUpdated,
                            watchlistData = WatchlistData(
                                name = it.data.watchlistData.name,
                                tickers = tickerList
                            )
                        )
                    )
                }

            }
        }


//    suspend operator fun invoke(wlName: String): Flow<DataState<PopularWatchlist?>> = flow {
//        repo.getPopularWatchlist((wlName)).collectLatest {
//            when (it) {
//                is DataState.Loading -> {
//                    emit(it)
//                }
//                is DataState.Error -> {
//                    emit(it)
//                }
//                is DataState.Success -> {
//
//                    // Sort by absolute value of % change
//                    val tickerList = it.data!!.watchlistData.tickers.sortedWith { o1, o2 ->
//                        val absOne = abs(o1!!.changePercent)
//                        val absTwo = abs(o2!!.changePercent)
//
//                        when {
//                            absOne > absTwo -> {
//                                1
//                            }
//                            absOne < absTwo -> {
//                                -1
//                            }
//                            else -> {
//                                0
//                            }
//                        }
//                    }
//
//                    // Rebuild the DataState object with the sorted Popular Watchlist and emit
//                    emit(
//                        DataState.Success(
//                            data = PopularWatchlist(
//                                watchlistData = WatchlistData(
//                                    name = it.data.watchlistData.name,
//                                    tickers = tickerList
//                                ),
//                                lastUpdated = it.data.lastUpdated
//                            )
//                        )
//                    )
//                }
//            }
//        }
//    }
}