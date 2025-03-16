package com.palpa.autosyncapp.model

import android.content.Context
import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    // LiveData to observe user data
    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    // Function to insert a user
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    // Function to delete a user
    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    // Function to update a user
    suspend fun update(user: User) {
        userDao.update(user)
    }
}

