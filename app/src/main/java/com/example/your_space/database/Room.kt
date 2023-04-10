package com.example.your_space.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [DatabaseSpaceItem::class, User::class, Request::class, Room::class, Space::class, Booking::class, SpaceRoom::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: AppDao

    companion object {
        @Volatile
        private var databaseINSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                // local instance of the database to allow using the smart casting function
                var localInstance = databaseINSTANCE

                // check if the local instance (which represent a local clone of the data base)
                // has been instantiated before
                if (localInstance == null) {
                    // it it was not -> instantiate it
                    localInstance =
                        Room.databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            "AppDatabase"
                        ).build()
                    // and then assign it to the global one
                    // and if it has been instantiated before -> return the existing one
                    databaseINSTANCE = localInstance
                }
                return localInstance
            }
        }
    }
}