package com.example.megogo

import android.content.Context
import androidx.room.Room

object DatabaseClient {
    private var instance: CinemaDatabase? = null

    fun getInstance(context: Context): CinemaDatabase {
        if (instance == null) {
            synchronized(CinemaDatabase::class.java) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CinemaDatabase::class.java,
                        "cinema_database"
                    ).build()
                }
            }
        }
        return instance!!
    }
}
