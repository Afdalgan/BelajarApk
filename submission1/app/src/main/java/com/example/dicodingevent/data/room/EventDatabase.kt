package com.example.dicodingevent.data.room

import android.content.Context
import androidx.room.*
import com.example.dicodingevent.data.entity.EventEntity

//@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
//abstract class EventDatabase : RoomDatabase() {
//    abstract fun favoriteEventDao(): EventDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: EventDatabase? = null
//
//        fun getInstance(context: Context): EventDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    EventDatabase::class.java,
//                    "event_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}

@Database(entities = [EventEntity::class], version = 4, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): EventDatabase {
            if (instance == null) {
                synchronized(EventDatabase::class.java) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        EventDatabase::class.java, "event_database")
                        .build()
                }
            }
            return instance as EventDatabase
        }
    }
}