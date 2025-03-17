# User Data Storage & Sync App

## Overview
This Android application allows users to store their personal data, including name, date of birth, image, and address, offline using Room Database. When the device is connected to the internet (WiFi or mobile data), the app automatically:
1. Uploads the stored images to Firebase Firestore and retrieves the image URL.
2. Uploads the user data (including the image URL) to Firebase Realtime Database.
3. Uses WorkManager to handle background data uploads.
4. Automatically deletes the data from the local Room Database once the upload is successful.

## Features
- **Offline Data Storage:** Uses Room Database to store user data locally.
- **Automatic Data Sync:** Detects internet availability and uploads data automatically.
- **Image Upload & Management:** Uploads images to Firestore and stores their paths in Firebase Realtime Database.
- **WorkManager Implementation:** Ensures background upload operations.
- **Automatic Cleanup:** Deletes locally stored data after successful synchronization.

## Tech Stack
- **Programming Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **Local Database:** Room Database
- **Cloud Storage:** Firebase Firestore (for images)
- **Database:** Firebase Realtime Database
- **Dependency Injection:** Koin
- **Background Task Handling:** WorkManager
- **UI Framework:** Jetpack Compose

## Project Setup
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repository.git
   ```
2. Open the project in Android Studio.
3. Add your Firebase configuration file (`google-services.json`) to the `app/` directory.
4. Sync the project with Gradle.
5. Run the app on an emulator or device.

## How It Works
1. Users enter their details and select an image.
2. Data is stored locally using Room Database.
3. The app listens for internet connectivity changes.
4. When connected, WorkManager initiates the upload process:
   - Image is uploaded to Firestore, and its download URL is generated.
   - User data, including the image URL, is uploaded to Firebase Realtime Database.
   - After successful upload, local Room Database data is deleted.
5. The cycle repeats for new data entries.

## Dependencies
Add the following dependencies to your `build.gradle`:
```kotlin
// Room Database
implementation("androidx.room:room-runtime:2.5.0")
kapt("androidx.room:room-compiler:2.5.0")

// Firebase
implementation("com.google.firebase:firebase-firestore:24.6.0")
implementation("com.google.firebase:firebase-database:20.2.0")
implementation("com.google.firebase:firebase-storage:20.3.0")

// WorkManager
implementation("androidx.work:work-runtime-ktx:2.8.0")

// Koin for Dependency Injection
implementation("io.insert-koin:koin-android:3.3.0")
```

## License
This project is licensed under the MIT License.

---
Feel free to modify this `README.md` as needed!

