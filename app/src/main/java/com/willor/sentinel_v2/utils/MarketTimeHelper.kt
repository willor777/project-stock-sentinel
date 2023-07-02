//package com.willor.sentinel_v2.utils
//
//import com.willor.stock_analysis_lib.common.CandleInterval
//import com.willor.stock_analysis_lib.common.CandleInterval.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//// TODO TEST ME!!!!
//object MarketTimeHelper {
//
//    // Time to offset the findNextCandleFormationTime as a buffer
//    private val bufferInSeconds = 5
//
//    fun isBusinessDay(ts: Long): Boolean {
//        val cal = Calendar.getInstance().apply { timeInMillis = ts }
//        return cal.get(Calendar.DAY_OF_WEEK) in Calendar.MONDAY..Calendar.FRIDAY
//    }
//
//    fun findNextBusinessDay(ts: Long): Date {
//
//        // Init Calendar set with tomorrows time, loop until "if biz day" is true
//        val cal = Calendar.getInstance().apply { timeInMillis = ts + 24 * 60 * 60 * 1000 }
//        while (cal.get(Calendar.DAY_OF_WEEK) !in Calendar.MONDAY..Calendar.FRIDAY) {
//            cal.apply { timeInMillis += 24 * 60 * 60 * 1000 }
//        }
//
//        cal.set(Calendar.HOUR_OF_DAY, 0)
//        cal.set(Calendar.MINUTE, 0)
//        cal.set(Calendar.SECOND, 0)
//        cal.set(Calendar.MILLISECOND, 0)
//        return cal.time
//    }
//
//    fun findNextStepInInterval(ts: Long, interval: CandleInterval): Pair<Int, Int> {
//        val cal = Calendar.getInstance().apply { timeInMillis = ts }
//        val hour = cal.get(Calendar.HOUR_OF_DAY)
//        val minute = cal.get(Calendar.MINUTE)
//
//        return when (interval) {
//            CandleInterval.M1 -> {
//                if (minute >= 59) {
//                    Pair(hour + 1, 0)
//                } else {
//                    Pair(hour, minute + 1)
//                }
//            }
//            M5 -> {
//                if (minute >= 55) {
//                    Pair(hour + 1, 0)
//                } else {
//                    Pair(hour, minute + (5 - (minute % 5)))
//                }
//            }
//            M15 -> {
//                if (minute >= 45) {
//                    Pair(hour + 1, 0)
//                } else {
//                    Pair(hour, minute + (15 - (minute % 15)))
//                }
//            }
//            M30 -> {
//                if (minute >= 30) {
//                    Pair(hour + 1, 0)
//                } else {
//                    Pair(hour, 30)
//                }
//            }
//            H1 -> {
//                Pair(hour + 1, 0)
//            }
//            D1 -> {
//                Pair(99, 99)
//            }
//        }
//    }
//
//    // TODO Especially test me!!! This a bad mamma jamma
//    fun findNextCandleFormationTime(ts: Long, interval: CandleInterval): Date {
//
//        // If weekend return next business day + time of first candle of day
//        if (!isBusinessDay(ts)) {
//            val nextBusinessDay = findNextBusinessDay(ts)
//
//            // Init a Calendar and set the timestamp to the next biz day's first candle
//            val cal = Calendar.getInstance().apply { timeInMillis = nextBusinessDay.time }
//            cal.set(Calendar.HOUR_OF_DAY, interval.hourOfFirstCandle)
//            cal.set(Calendar.MINUTE, interval.minuteOfFirstCandle)
//            cal.set(Calendar.SECOND, bufferInSeconds)
//            cal.set(Calendar.MILLISECOND, 0)
//            return cal.time
//        }
//
//        // Find next step in interval
//        var (hour, minute) = findNextStepInInterval(ts, interval)
//        println("hour/min $hour/$minute ........... interval $interval")
//
//        // If pre/postmarket (not between 09:30-16:00)
//        if (!((hour in 10..15) || (hour == 9 && minute >= 30))) {
//            println("not in")
//            // If in postmarket find next biz day first candle
//            if (hour >= 16) {
//                val nextBusinessDay = findNextBusinessDay(ts)
//
//                // Init Calendar and set it's time to next biz day time of first candle
//                val cal = Calendar.getInstance().apply { timeInMillis = nextBusinessDay.time }
//                cal.set(Calendar.HOUR_OF_DAY, interval.hourOfFirstCandle)
//                cal.set(Calendar.MINUTE, interval.minuteOfFirstCandle)
//                cal.set(Calendar.SECOND, bufferInSeconds)
//                cal.set(Calendar.MILLISECOND, 0)
//                return cal.time
//            }
//
//            // If early
//            hour = 9
//            minute = 30
//        }
//
//        // Init Calendar and set with provided timestamp
//        val cal = Calendar.getInstance().apply { timeInMillis = ts }
//
//        // Set the Hour/Minute/Second/Millisecond of the calendar to the calculated times
//        cal.set(Calendar.HOUR_OF_DAY, hour)
//        cal.set(Calendar.MINUTE, minute)
//        cal.set(Calendar.SECOND, bufferInSeconds)
//        cal.set(Calendar.MILLISECOND, 0)
//
//        return cal.time
//    }
//}
//
