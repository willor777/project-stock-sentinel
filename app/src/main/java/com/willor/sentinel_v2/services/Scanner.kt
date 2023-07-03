package com.willor.sentinel_v2.services

import android.util.Log
import com.github.willor777.stock_analysis_lib.analysis.AnalysisResults
import com.github.willor777.stock_analysis_lib.analysis.Strategies
import com.github.willor777.stock_analysis_lib.analysis.StrategyBase
import com.github.willor777.stock_analysis_lib.analysis.StrategyReqData
import com.github.willor777.stock_analysis_lib.analysis.strategies.PremarketRangeBreak
import com.github.willor777.stock_analysis_lib.analysis.strategies.RSRWBasic
import com.github.willor777.stock_analysis_lib.analysis.strategies.TestStrategy
import com.github.willor777.stock_analysis_lib.charts.StockChart
import com.github.willor777.stock_analysis_lib.common.CandleInterval
import com.github.willor777.stock_analysis_lib.common.MarketTimeUtil
import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.usecases.UseCases
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class Scanner(
    val coroutineScope: CoroutineScope,
    val usecases: UseCases
) {

    val tag = Scanner::class.java.simpleName

    var userPrefs: UserPreferences? = null

    /** Main function of scanner. Launches scanner loop in provided coroutineScope */
    fun runScanner() {

        coroutineScope.launch(Dispatchers.Default) {

            while(this.isActive) {
                // Get user prefs data
                val prefs = loadUserPrefsAsync().await()
                val candleInterval = prefs!!.scannerCandleInterval
                val scannerStrategy = prefs.scannerStrategy
                val scannerWatchlist = prefs.sentinelWatchlist

                // Determine next candle formation time
                val nextCandleTime = MarketTimeUtil
                    .getNextFullyFormedCandle(System.currentTimeMillis(), candleInterval)

                // Sleep until time
                delay (nextCandleTime - System.currentTimeMillis())

                // Gather required data
                val reqData = gatherRequiredData(scannerWatchlist, candleInterval, scannerStrategy)

                // Feed data into user's chosen scan strategy
                val results = executeStrategy(reqData, scannerStrategy)

                // Recieve output, save triggers found to Database
                //      - Take note of stocks with triggers, put them on a temporary
                //          ignore list, so that repeating triggers are avoided
                saveTriggers(results)
            }
        }
    }

    private fun saveTriggers(analysisResults: List<AnalysisResults>) {
        coroutineScope.launch(Dispatchers.IO) {
            for (i in analysisResults) {
                if (i.triggerValue != 0) {
                    usecases.saveTriggerUsecase(
                        i.ticker, i.strategyDisplayName, i.strategyDescription, i.triggerValue,
                        i.triggerStrengthPercentage, i.stockPriceAtTime, i.suggestedStop,
                        i.suggestedTakeProfit, i.shouldCloseLong, i.shouldCloseShort, i.timestamp
                    )
                }
            }
        }
    }

    /** Feeds data into selected strategy and returns results as List<AnalysisResults> */
    private fun executeStrategy(reqData: StrategyReqData, strategy: Strategies): List<AnalysisResults> {

        var results: List<AnalysisResults>? = null
        when(strategy) {
            Strategies.TEST_STRATEGY -> {
                val rd = reqData as StrategyReqData.TestStrategyReqData
                results = TestStrategy().analyze(rd)
            }
            Strategies.PREMARKET_RANGE_BREAK -> {
                val rd = reqData as StrategyReqData.PreMarketRangeBreakReqData
                results = PremarketRangeBreak().analyze(rd)
            }
            Strategies.RSRW_Basic -> {
                val rd = reqData as StrategyReqData.RSRWBasicReqData
                results = RSRWBasic().analyze(rd)
            }
        }
        return results
    }

    /** Gathers data required to execute strategy */
    private suspend fun gatherRequiredData(
        tickers: List<String>, candleInterval: CandleInterval, strategy: Strategies
    ): StrategyReqData {

        when (strategy) {
            Strategies.TEST_STRATEGY -> {
                val charts = gatherStockChartsAsync(
                    tickers = tickers,
                    interval = candleInterval,
                    "2d",
                    false
                ).await()
                return StrategyReqData.TestStrategyReqData(charts)
            }
            Strategies.PREMARKET_RANGE_BREAK -> {
                // Requires 2d chart + premarket data
                val charts = gatherStockChartsAsync(
                    tickers = tickers,
                    interval = candleInterval,
                    "2d",
                    true,
                ).await()
                return StrategyReqData.PreMarketRangeBreakReqData(charts)
            }
            Strategies.RSRW_Basic -> {
                // This strategy requires 1 "controll" chart used for comparison
                // along with all the ticker's charts

                val controlChart = gatherStockChartsAsync(
                    tickers = listOf("SPY"),
                    candleInterval,
                    "2d",
                    true,
                ).await()
                val tickerCharts = gatherStockChartsAsync(
                    tickers,
                    candleInterval,
                    "2d",
                    true,
                ).await()
                return StrategyReqData.RSRWBasicReqData(controlChart[0], tickerCharts)
            }
        }
    }

    /** Gathers stock charts, returns Deferred<List<StockChart>> */
    private fun gatherStockChartsAsync(
        tickers: List<String>,
        interval: CandleInterval,
        periodRange: String,
        prepost: Boolean): Deferred<List<StockChart>> {

        val chartsAsync = coroutineScope.async(Dispatchers.IO) {
            usecases.getStockChartsForAnalysisUsecase(
                tickers, interval.serverCode, periodRange, prepost
            )
        }

        return chartsAsync
    }

    /** Loads user prefs, returns Deferred */
    private suspend fun loadUserPrefsAsync(): Deferred<UserPreferences?> =

        coroutineScope.async(Dispatchers.IO) {

            var prefs: UserPreferences? = null

            usecases.getUserPreferencesUsecase().collectLatest {
                when (it) {
                    is DataResourceState.Success -> {
                        Log.d(tag, "loadUserPrefs() Successfully loaded UserPreferences")
                        prefs = it.data
                    }
                    is DataResourceState.Loading -> {
                        Log.d(tag, "loadUserPrefs() Loading UserPreferences")
                    }
                    is DataResourceState.Error -> {
                        Log.d(tag, "loadUserPrefs() Error UserPreferences")
                    }
                }
            }
            return@async prefs
        }


}