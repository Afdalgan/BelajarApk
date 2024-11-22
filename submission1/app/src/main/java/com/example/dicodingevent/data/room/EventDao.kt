package com.example.dicodingevent.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dicodingevent.data.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM event ORDER BY id DESC")
    fun getTheEventList(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) //.IGNORE
    suspend fun insertEvent(event: List<EventEntity>)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("DELETE FROM event") //WHERE isFavorite = 0
    suspend fun deleteAll()

    @Query("SELECT * FROM event WHERE finished = :finished")
    fun getEventsByFinished(finished: Int): List<EventEntity>

    @Query("SELECT * FROM event where isFavorite = 1")
    fun getFavoriteEvent(): List<EventEntity>

    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id AND isFavorite = 1)")
    suspend fun isEventFavorite(id: Int?): Boolean

    @Query("SELECT * FROM event WHERE id = :id LIMIT 1")
    fun getEventDetail(id: String): EventEntity?
}