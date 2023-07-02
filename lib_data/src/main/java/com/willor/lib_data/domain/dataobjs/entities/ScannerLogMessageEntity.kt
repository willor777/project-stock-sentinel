package com.willor.lib_data.domain.dataobjs.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willor.lib_data.data.local.db.StockDataDb


@Entity(tableName = StockDataDb.SCANNER_LOG_TABLE)
data class ScannerLogMessageEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo val msg: String,
    @ColumnInfo val timestamp: Long, 
)
