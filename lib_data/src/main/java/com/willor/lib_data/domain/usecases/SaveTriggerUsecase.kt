package com.willor.lib_data.domain.usecases

import androidx.room.ColumnInfo
import com.willor.lib_data.domain.Repo
import com.willor.lib_data.domain.dataobjs.entities.TriggerEntity

class SaveTriggerUsecase(
    private val repo: Repo
) {
    
    suspend operator fun invoke(
            ticker: String,
            strategyName: String,
            strategyDescription: String,
            triggerValue: Int,
            triggerStrengthPercentage: Double,
            stockPriceAtTime: Double,

            suggestedStop: Double,
            suggestedTakeProfit: Double,
            shouldCloseLong: Boolean,
            shouldCloseShort: Boolean,
            timestamp: Long,
    ) {
        val t = TriggerEntity(
            ticker, strategyName, strategyDescription, triggerValue,
            triggerStrengthPercentage, stockPriceAtTime, suggestedStop,
            suggestedTakeProfit, shouldCloseLong, shouldCloseShort, timestamp
        )

        repo.saveTriggers(t)

    }
}