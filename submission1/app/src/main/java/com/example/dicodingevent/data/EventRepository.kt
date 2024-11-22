package com.example.dicodingevent.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.dicodingevent.data.entity.EventEntity
import com.example.dicodingevent.data.retrofit.ApiService
import com.example.dicodingevent.data.room.EventDao
import com.example.dicodingevent.utils.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<EventEntity>>>()

    fun getHeadlineEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        Log.d("EventRepository", "Memulai getHeadlineEvent()")
        emit(Result.Loading) // Emit loading state
        try {
            val response0 = apiService.getEvent("0")
            val eventList = ArrayList<EventEntity>()

            response0.listEvents?.forEach { eventItem ->
                Log.d("EventRepository", "Data dari API: $response0")
                val isFavorite = eventDao.isEventFavorite(eventItem!!.id)
                val event = EventEntity(
                    id = eventItem.id!!,
                    name = eventItem.name!!,
                    description = eventItem.description!!,
                    mediaCover = eventItem.mediaCover!!,
                    ownerName = eventItem.ownerName!!,
                    quota = eventItem.quota!!,
                    beginTime = eventItem.beginTime!!,
                    link = eventItem.link!!,
                    registrants = eventItem.registrants!!,
                    finished = 0,
                    isFavorite = isFavorite
                )
                eventList.add(event)
            }

            val response1 = apiService.getEvent("1")
            response1.listEvents?.forEach { eventItem ->
                Log.d("EventRepository", "Data dari API: $response1")
                val isFavorite = eventDao.isEventFavorite(eventItem!!.id)
                val event = EventEntity(
                    id = eventItem.id!!,
                    name = eventItem.name!!,
                    description = eventItem.description!!,
                    mediaCover = eventItem.mediaCover!!,
                    ownerName = eventItem.ownerName!!,
                    quota = eventItem.quota!!,
                    beginTime = eventItem.beginTime!!,
                    link = eventItem.link!!,
                    registrants = eventItem.registrants!!,
                    finished = 1,
                    isFavorite = isFavorite
                )
                eventList.add(event)
            }

            eventDao.deleteAll()
            eventDao.insertEvent(eventList)

            emit(Result.Success(eventList))

        } catch (e: Exception) {
            Log.e("EventRepository", "getHeadlineEvent: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }

        val localData: LiveData<List<EventEntity>> = eventDao.getTheEventList()
        emitSource(localData.map {
            if (it.isNotEmpty()) Result.Success(it)
            else Result.Loading
        })
    }

    fun getEventsByFinished(finished: Int): LiveData<Result<List<EventEntity>>> {
        val result = MutableLiveData<Result<List<EventEntity>>>()
        result.value = Result.Loading

        CoroutineScope(Dispatchers.IO).launch {
            val localData = eventDao.getEventsByFinished(finished)
            if (localData.isNotEmpty()) {
                result.postValue(Result.Success(localData))
            } else {
                result.postValue(Result.Error("Tidak ada data"))
            }
        }
        return result
    }

    fun getEventsByUpcoming(finished: Int): LiveData<Result<List<EventEntity>>> {
        val result = MutableLiveData<Result<List<EventEntity>>>()
        result.value = Result.Loading

        CoroutineScope(Dispatchers.IO).launch {
            val localData = eventDao.getEventsByFinished(finished)
            if (localData.isNotEmpty()) {
                result.postValue(Result.Success(localData))
            } else {
                result.postValue(Result.Error("Tidak ada data"))
            }
        }
        return result
    }

    fun getDetailEvent(id: String): LiveData<Result<EventEntity>> {
        val result = MediatorLiveData<Result<EventEntity>>()
        result.value = Result.Loading

        CoroutineScope(Dispatchers.IO).launch {
            val event = eventDao.getEventDetail(id)
            if (event != null) {
                result.postValue(Result.Success(event))
            } else {
                result.postValue(Result.Error("Event tidak ditemukan"))
            }
        }
        return result
    }

    fun getFavoriteEvent(): LiveData<Result<List<EventEntity>>> {
        val result = MutableLiveData<Result<List<EventEntity>>>()
        result.value = Result.Loading

        CoroutineScope(Dispatchers.IO).launch {
            val localData = eventDao.getFavoriteEvent()
            if (localData.isNotEmpty()) {
                result.postValue(Result.Success(localData))
            } else {
                result.postValue(Result.Error("Tidak ada data"))
            }
        }
        return result    }

    suspend fun setEventFavorite(event: EventEntity, favoriteState: Boolean) {
        event.isFavorite = favoriteState
        eventDao.updateEvent(event)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}