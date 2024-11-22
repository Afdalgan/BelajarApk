package com.dicoding.asclepius.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrediction(prediction: HistoryEntity)

    @Query("SELECT * FROM prediction_history ORDER BY timestamp DESC")
    fun getAllPredictions(): LiveData<List<HistoryEntity>>

    @Delete
    suspend fun deletePrediction(prediction: HistoryEntity)
}
