package com.example.a7minutesworkout.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Delete
    suspend fun delete(historyEntity: HistoryEntity)

    @Query("SELECT * FROM `history-table`")
    fun fetchAllHistories(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM `history-table` WHERE id = :id")
    fun fetchHistoryById(id: Int): Flow<HistoryEntity>
}