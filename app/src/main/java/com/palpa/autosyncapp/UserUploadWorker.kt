package com.palpa.autosyncapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.lifecycle.asFlow
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.palpa.autosyncapp.model.User
import com.palpa.autosyncapp.model.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserUploadWorker(private val context: Context,private val workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val firestore = FirebaseFirestore.getInstance()


        val taskRef = firestore.collection("users_data").document()
        return try {
            val dao=  UserDatabase.getDatabase(context).userDao()
            dao.getAllUsers().asFlow().collect{users->
                val job= CoroutineScope(Dispatchers.IO).launch {
                    val jobs = users.map {user->
                        CoroutineScope(Dispatchers.IO).async {
                            val imageRef = user.imageUri.toUri()
                            val fileArray = context.contentResolver.openInputStream(imageRef)?.use {
                                it.readBytes()
                            }
                            if (fileArray != null) {
                                val imageUplaoderUrl = storeFileToServer(fileArray,"imagePath/")
                                user.imageUri = imageUplaoderUrl.orEmpty()
                                addNewUser(user)
                                UserDatabase.getDatabase(context).clearAllTables()
                            }
                        }
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getForegroundInfo(applicationContext)
    }

    private fun getForegroundInfo(context: Context): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID,
            createNotification(context, "Uploading", "Files Are Uploading"),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    }

    private  fun addNewUser(user:User) {
        val firestore = FirebaseFirestore.getInstance()
        val taskRef = firestore.collection("users_data").document()
        val taskWithId = taskRef.id
        taskRef.set(user)
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->

            }

    }



    suspend fun storeFileToServer(image: ByteArray, folderPath: String): String? {
        val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
        return suspendCoroutine { continuation ->
            val imageRef =
                firebaseStorage.getReference(folderPath).child(UUID.randomUUID().toString())
            imageRef.putBytes(image).addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    continuation.resume(imageUrl)
                }
            }.addOnFailureListener { error ->
                continuation.resume(null)
            }


        }
    }
    private fun createNotification(
        context: Context,
        title: String,
        description: String
    ): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(description)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setOngoing(true)
            .build()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        NOTIFICATION_CHANNEL_ID
        val importance = NotificationManager.IMPORTANCE_HIGH // Set the importance level
        val channel =
            NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    companion object{
        private const val NOTIFICATION_ID = 1

        private const val NOTIFICATION_CHANNEL_ID = "UPLOAD WORKER"

        private const val NOTIFICATION_CHANNEL_NAME = "FILE UPLOAD WORKER"
    }
}
