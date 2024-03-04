package com.rkcoding.taskreminder.todo_features.data.repository

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.rkcoding.taskreminder.todo_features.domain.model.UserData
import com.rkcoding.taskreminder.todo_features.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {
    override suspend fun loginUserWithEmailPassword(
        email: String,
        password: String
    ): Result<UserData> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null){
                val user = result.user!!
                Result.success(
                   UserData(
                       userId = user.uid,
                       userName = user.displayName,
                       userEmail = user.email,
                       userPassword = "",
                       profileImageUrl = user.photoUrl.toString()
                   )
                )
            }else{
                Result.failure(Exception("sinIn failed"))
            }

        }catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun registerUserWithEmailPassword(
        name: String,
        email: String,
        password: String,
        profileUri: String?
    ): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (result.user != null) {

                val user = result.user!!
                val profileInfoBuilder = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                if (profileUri != null) {
                    profileInfoBuilder.photoUri = profileUri.toUri()
                }
                val profileInfo = profileInfoBuilder.build()

                user.updateProfile(profileInfo).await()
                Result.success(user.uid)
            } else {
                Result.failure(Exception("user is null"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}