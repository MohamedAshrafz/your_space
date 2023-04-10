package com.example.your_space.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg spaces: WorkingSpaceDB)

    @Query("SELECT * FROM workingSpaces_table")
    fun gelAllWorkingSpaces(): LiveData<List<WorkingSpaceDB>>

    @Query("DELETE FROM workingSpaces_table")
    fun deleteAllWorkingSpaces()
}