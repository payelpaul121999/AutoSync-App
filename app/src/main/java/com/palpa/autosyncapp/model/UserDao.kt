package com.palpa.autosyncapp.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    // Insert a new user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    // Update an existing user
    @Update
    suspend fun update(user: User)

    // Delete a user
    @Delete
    suspend fun delete(user: User)

    // Get all users from the database
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<User>>
}


