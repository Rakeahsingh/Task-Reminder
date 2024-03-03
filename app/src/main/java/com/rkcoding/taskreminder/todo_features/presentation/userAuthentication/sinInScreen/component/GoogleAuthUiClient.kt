package com.rkcoding.taskreminder.todo_features.presentation.userAuthentication.sinInScreen.component

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.rkcoding.taskreminder.R
import com.rkcoding.taskreminder.todo_features.domain.model.SinInResult
import com.rkcoding.taskreminder.todo_features.domain.model.UserData
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val onTapClient: SignInClient
) {

    private val auth = Firebase.auth
    suspend fun sinIn(): IntentSender? {
        val result = try {
            onTapClient.beginSignIn(
                beginSinInRequest()
            ).await()

        }catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }

        return result?.pendingIntent?.intentSender
    }


    suspend fun sinInWithIntent(intent: Intent): SinInResult {
        val credential = onTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
           val user =  auth.signInWithCredential(googleCredential).await().user
            SinInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profileImageUrl = photoUrl.toString()
                    )
                },
                errorMessage = null
            )

        }catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            SinInResult(
                data = null,
                errorMessage = e.message
            )
        }

    }

    fun getSingedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            userName = displayName,
            profileImageUrl = photoUrl.toString()
        )
    }

    suspend fun sinOut(){
        try {
            onTapClient.signOut().await()
            auth.signOut()
        }catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    private fun beginSinInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(false)
            .build()
    }

}