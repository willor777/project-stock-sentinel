package com.willor.lib_data.utils

import java.io.BufferedReader
import java.io.InputStreamReader

object TickerSymbolLoader {

    private var charIndexLocMap: Map<Char, Int>? = null

    /**
     * Loads symbols from the json file. Then creates a map containing the first index location for each
     * letter in the alphabet.
     */
    fun loadSymbols(): List<List<String>>{
        val fn = "res/raw/equity_and_etf_symbols.json"
        val stream = this::class.java.classLoader?.getResourceAsStream(fn)
        val txt = BufferedReader(InputStreamReader(stream)).readText()
        //  Gson().fromJson<Map<String, List<List<String>>>>(txt, Map::class.java)
        val data = gson.fromJson<Map<String, List<List<String>>>>(txt, Map::class.java)

        return buildAlphabetizedIndexMap(data["data"]!!)
    }

    private fun buildAlphabetizedIndexMap(data: List<List<String>>): List<List<String>>{

        // Sort the data by the Ticke Symbol
        val sortedData = data.sortedBy{
            it[0]
        }

        // Check if the alphabetical index location map has already been built
        if (charIndexLocMap != null){
            return sortedData
        }

        // Build index map
        val indexLocsMap = mutableMapOf<Char, Int>()
        var curChar: Char = ' '

        // Loop through and record the first index location for each character
        sortedData.forEachIndexed { index, strings ->
            val (ticker, _, _) = strings

            if (ticker[0] != curChar){
                curChar = ticker[0]
                indexLocsMap[curChar] = index
            }
        }

        charIndexLocMap = indexLocsMap
        return sortedData
    }

    fun findStartingIndexOfChar(c: Char): Int{
        return charIndexLocMap?.get(c) ?: 0
    }
}














