package com.willor.sentinel_v2.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import com.willor.lib_data.domain.Repo
import com.willor.sentinel_v2.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


// TODO Scanner should only thow a trigger once every 4 hours or so...Don't annoy user!!!

/**
 * Scanner that analyzes stock charts periodically according to specified interval.
 *
 * Triggers found are saved in the database
 */
@AndroidEntryPoint
class StockScannerService : Service() {

    @Inject
    lateinit var repo: Repo

    private val tag: String = StockScannerService::class.java.simpleName
    private val binder = SentinelScannerBinder()
    private val notificationId: Int = 8
    private val notificationChannelId: String = "STOCK_SCANNER"
    private val notificationChannelName: String = "Stock Scanner"

    // This scope is canceled in onDestroy()
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)


    override fun onBind(intent: Intent?): IBinder {
        Log.d(tag, "onBind() called")
        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand() called")

        // Start foreground providing the notificationId + a Notification
        createNotificationChannel()
        startForeground(notificationId, createNotification("Scanner Running"))

        return START_STICKY
    }


    override fun stopService(name: Intent?): Boolean {
        Log.d(tag, "stopService() called")
        return super.stopService(name)
    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        Log.d(tag, "onDestroy() called")
    }


    // TODO Add checks for version codes lower than < Oreo
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                notificationChannelId,
                notificationChannelName, NotificationManager.IMPORTANCE_HIGH
            )
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val service =
                this.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)
        }
    }


    // TODO Set small icon
    private fun createNotification(notificationText: String): Notification {

        // Pending Intent calls 'StopScannerReceiver', Which then stops the service
        val dismissIntent = PendingIntent.getBroadcast(
            this,
            8,
            Intent(this, StopScannerReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, notificationChannelId)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
            .addAction(R.drawable.ic_scanner_icon_24, "Dismiss", dismissIntent)
            .setContentText(notificationText)
            .build()
    }


    private fun updateNotification(newNotification: Notification) {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .notify(notificationId, newNotification)
    }


    private fun startScanner() {
        coroutineScope.launch {



        }
    }


    /*
     * TODO - Scanner Process Rough Outline
     *      - Get Charts for Sentinel Watchlist Tickers
     *      - Apply Analysis to Each Chart
     *      - If trigger is found, save it to the data base
     *      - Scanner should update it's current "status" as a Database entry to be displayed
     *
     * Notes * * * * *
     * - Remember to only fetch charts after full candle has been formed (This improves efficiency)
     */
//    private inner class Scanner {
//
//        private var curInterval: CandleInterval? = null
//        private var curWatchlist: List<String>? = null
//        private var curStrategy: Strategy? = null
//        private var scannerJob: Job? = null
//
//        init {
//            collectUserPrefsFlow()
//        }
//
//
//        /**
//         * Collects UserPrefs from DataStore flow.
//         */
//        fun collectUserPrefsFlow() {
//            coroutineScope.launch(Dispatchers.IO) {
//                repo.getUserPreferences().collectLatest {
//                    when (it) {
//                        is DataResourceState.Success -> {
//                            if (
//                                it.data.scannerChartInterval != curInterval ||
//                                it.data.sentinelWatchlist != curWatchlist ||
//                                it.data.scannerStrategy != curStrategy!!.strategyName
//                            ) {
//                                curInterval = it.data.scannerChartInterval
//                                curWatchlist = it.data.sentinelWatchlist
//                                curStrategy = determineStrategy(it.data)
//                                scannerJob?.cancel()
//                                startScanner()
//                            }
//                        }
//                        else -> {
//                            // Not needed
//                        }
//                    }
//                }
//            }
//        }
//
//
//        fun startScanner() {
//            scannerJob = coroutineScope.launch(Dispatchers.Default) {
//
//                // Make sure all required settings are present, if not, return
//                if (curWatchlist.isNullOrEmpty() || curInterval == null || curStrategy == null) {
//                    return@launch
//                }
//
//
//            }
//        }
//
//        private fun determineStrategy(userPrefs: UserPreferences): Strategy {
//            return when (userPrefs.scannerStrategy) {
//                StrategyName.TEST_STRATEGY -> {
//                    TestStrategy()
//                }
//                else -> {
//                    TestStrategy()
//                }
//            }
//        }
//
//        private suspend fun scan(scanTime: Long) {
//
//            // Determine next candle formation time
//            val curTime = System.currentTimeMillis()
//            val nextFullCandle = MarketTimeHelper
//                .findNextCandleFormationTime(curTime, curInterval!!)
//
//            // Wait
//            delay(nextFullCandle.time - curTime)
//
//            // Scan each ticker in watchlist using specified strategy
//            val tickerChartPairs = collectAllCharts()
//
//            // Loop through charts and build list of triggers
//            val validTriggers = mutableListOf<TriggerEntity>()
//            for ((sym, c) in tickerChartPairs){
//
//                // Verify chart
//                if (c == null){
//                    continue
//                }
//
//                // Collect analysis for chart, if trigger found, add to validTriggers list
//                val analysis = curStrategy!!.analyzeChart(c)
//                if (analysis.trigger != 0){
//                    validTriggers.add(
//                        TriggerEntity(
//                            ticker = analysis.ticker,
//                            strategyName = analysis.strategyDisplayName,
//                            strategyDescription = analysis.strategyDescription,
//                            triggerValue = analysis.trigger,
//                            triggerStrengthPercentage = analysis.triggerStrengthPercentage,
//                            suggestedStop = analysis.suggestedStop,
//                            suggestedTakeProfit = analysis.suggestedTakeProfit,
//                            shouldCloseLong = analysis.shouldCloseLongPositions,
//                            shouldCloseShort = analysis.shouldCloseShortPositions,
//                            stockPriceAtTime = analysis.stockPriceAtTime,
//                            timestamp = analysis.timestamp
//                        )
//                    )
//                }
//            }
//
//            repo.saveTriggers(*validTriggers.toTypedArray())
//        }
//
//
//        private suspend fun collectAllCharts(): List<Pair<String, StockChart?>> {
//
//            // List of deferred Pair<ticker, chart> (Collected asynchronously)
//            val charts: MutableList<Deferred<Pair<String, StockChart?>>> = mutableListOf()
//
//            // Loop through tickers and add deferred Pair<ticker, chart> to list
//            curWatchlist?.forEach { t ->
//                charts.add(
//                    coroutineScope.async(Dispatchers.IO){
//                        // Pair<String: Ticker, StockChart?: ChartForTicker>
//                        t to collectChartForTicker(t)
//                    }
//                )
//            }
//
//            return charts.awaitAll()
//        }
//
//
//        private suspend fun collectChartForTicker(t: String): StockChart? {
//
//            // Non nullability is guaranteed  by calling function
//
//            var chart: StockChart? = null
//            repo.getStockChart(
//                ticker = t,
//                interval = curInterval!!.serverCode,
//                periodRange = curStrategy!!.requiredPeriodRange,
//                prepost = curStrategy!!.requiredPrepost
//            ).collectLatest {
//                when (it) {
//                    is DataResourceState.Success -> {
//                        Log.d(tag, "Collected chart for ticker: $t")
//
//                        chart = StockChart(
//                            ticker = it.data!!.data.ticker,
//                            interval = curInterval!!,
//                            periodRange = it.data!!.data.periodRange,
//                            prepost = it.data!!.data.prepost,
//                            datetime = it.data!!.data.timestamp.map { ts ->
//
//                                // Timestamps are in seconds
//                                Date(ts * 1000L)
//                            },
//                            timestamp = it.data!!.data.timestamp,
//                            open = it.data!!.data.open,
//                            high = it.data!!.data.high,
//                            low = it.data!!.data.low,
//                            close = it.data!!.data.close,
//                            volume = it.data!!.data.volume
//                        )
//                        return@collectLatest
//                    }
//                    is DataResourceState.Error -> {
//                        Log.d(tag, "Failed to collect chart for ticker: $t")
//                        return@collectLatest
//                    }
//                    is DataResourceState.Loading -> {
//                        Log.d(tag, "Loading chart for ticker: $t")
//                    }
//                }
//            }
//
//            return chart
//        }
//
//
//    }


    inner
    class SentinelScannerBinder : Binder() {

        // This function can be called to retrieve an instance of the service
        fun getService(): StockScannerService {
            return this@StockScannerService
        }

    }

}

















