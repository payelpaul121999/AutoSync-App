package com.palpa.autosyncapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.palpa.autosyncapp.model.User
import com.palpa.autosyncapp.model.UserViewModel
import kotlinx.coroutines.flow.callbackFlow


class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by lazy { UserViewModel(application) }
    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val nameEditText = findViewById<EditText>(R.id.etName)
        val dobEditText = findViewById<EditText>(R.id.etDob)
        val addressEditText = findViewById<EditText>(R.id.etAddress)
        val pinEditText = findViewById<EditText>(R.id.etPin)
        val districtEditText = findViewById<EditText>(R.id.etDistrict)
        val stateEditText = findViewById<EditText>(R.id.etState)
        imageView = findViewById(R.id.imageView)
        val btnPickImage = findViewById<Button>(R.id.btnPickImage)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnPickImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        btnSave.setOnClickListener {
            val user = User(
                name = nameEditText.text.toString(),
                dob = dobEditText.text.toString(),
                address = addressEditText.text.toString(),
                pin = pinEditText.text.toString(),
                district = districtEditText.text.toString(),
                state = stateEditText.text.toString(),
                imageUri = imageUri.toString()
            )
           /*
            userViewModel.insert(user)*/
            /*val user = User(
                name = "John Doe",
                dob = "1990-01-01",
                address = "123 Main St",
                pin = "123456",
                district = "XYZ",
                state = "ABC",
                imageUri = "path_to_image"
            )*/

            userViewModel.insert(user) // Insert user into database
            Toast.makeText(this@MainActivity, "User Saved!", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }
    fun fetchTaskCostList() {
        val firestore = FirebaseFirestore.getInstance()
        val taskTypesRef = firestore.collection("taskCosts")

        taskTypesRef.get()
            .addOnSuccessListener { querySnapshot ->
//                val _taskCostList =
//                    querySnapshot.documents.mapNotNull { it.toObject(TaskCost::class.java) }
//                taskCostList.addItems(_taskCostList)
            }
            .addOnFailureListener { exception ->
               // taskCostList.addItems(emptyList())
            }
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

}