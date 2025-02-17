package com.acon.acon.data.datasource.remote

import android.content.Context
import android.os.Build
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.acon.acon.data.BuildConfig
import com.acon.acon.data.error.ErrorMessages
import com.acon.acon.domain.error.user.CredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.qualifiers.ActivityContext
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class TokenRemoteDataSource @Inject constructor(
    @ActivityContext private val context: Context
) {

    private val rawNounce = UUID.randomUUID().toString()
    private val bytes = rawNounce.toByteArray()
    private val md = MessageDigest.getInstance("SHA-256")
    private val digest = md.digest(bytes)
    private val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

    private val googleIdOption: GetGoogleIdOption by lazy {
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .build()
    }

    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(context)
    }

    suspend fun signIn(): Result<String> = runCatching {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
           credentialManager.prepareGetCredential(
               GetCredentialRequest(
                   listOf(
                       googleIdOption
                   )
               )
           )
        }

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        credentialManager.getCredential(
            request = request,
            context = context
        )
    }.fold(
        onSuccess = { response ->
            when (val credential = response.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                        Result.success(idToken)
                    } else {
                        throw IllegalStateException(ErrorMessages.UNKNOWN_CREDENTIAL_TYPE)
                    }
                }
                is PublicKeyCredential -> {
                    throw IllegalStateException(ErrorMessages.UNSUPPORTED_CREDENTIAL_TYPE)
                }
                is PasswordCredential -> {
                    throw IllegalStateException(ErrorMessages.UNSUPPORTED_CREDENTIAL_TYPE)
                }
                else -> {
                    throw IllegalStateException(ErrorMessages.UNKNOWN_CREDENTIAL_TYPE)
                }
            }
        },
        onFailure = { e ->
            Result.failure(
                when (e) {
                    is GetCredentialCancellationException -> CredentialException.UserCanceled(ErrorMessages.USER_CANCELED)
                    is NoCredentialException -> {
                        CredentialException.NoStoredCredentials(ErrorMessages.NO_CREDENTIAL_AVAILABLE)
                    }

                    is GoogleIdTokenParsingException -> e
                    is SecurityException -> e
                    else -> IllegalStateException(ErrorMessages.UNKNOWN_ERROR)
                }
            )
        }
    )
}
