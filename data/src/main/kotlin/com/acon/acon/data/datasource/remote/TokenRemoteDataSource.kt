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
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.qualifiers.ActivityContext
import timber.log.Timber
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

    private val googleIdOption: GetSignInWithGoogleOption =
        GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_CLIENT_ID)
            .build()

    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(context)
    }

    suspend fun googleSignIn(): Result<String> = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            credentialManager.prepareGetCredential(
                GetCredentialRequest(
                    listOf(googleIdOption)
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
            Timber.tag(TAG).d("Received credential response: $response")

            when (val credential = response.credential) {
                is CustomCredential -> {
                    Timber.tag(TAG)
                        .d("Credential is CustomCredential. Type: ${credential.type}")
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                        Timber.tag(TAG).d("Successfully parsed idToken")
                        Result.success(idToken)
                    } else {
                        Timber.tag(TAG).e("Unknown credential type")
                        throw IllegalStateException(ErrorMessages.UNKNOWN_CREDENTIAL_TYPE)
                    }
                }

                is PublicKeyCredential -> {
                    Timber.tag(TAG).e("Credential is PublicKeyCredential. Unsupported.")
                    throw IllegalStateException(ErrorMessages.UNSUPPORTED_CREDENTIAL_TYPE)
                }

                is PasswordCredential -> {
                    Timber.tag(TAG).e("Credential is PasswordCredential. Unsupported.")
                    throw IllegalStateException(ErrorMessages.UNSUPPORTED_CREDENTIAL_TYPE)
                }

                else -> {
                    Timber.tag(TAG)
                        .e("Unknown credential class: ${credential::class.java}")
                    throw IllegalStateException(ErrorMessages.UNKNOWN_CREDENTIAL_TYPE)
                }
            }
        },
        onFailure = { e ->
            Timber.tag(TAG).e(e, "GoogleLogin failed: ${e.message}")

            Result.failure(
                when (e) {
                    is GetCredentialCancellationException -> {
                        Timber.tag(TAG).e("User cancelled login")
                        CredentialException.UserCanceled(ErrorMessages.USER_CANCELED)
                    }

                    is NoCredentialException -> {
                        Timber.tag(TAG).e("No stored credentials available")
                        CredentialException.NoStoredCredentials(ErrorMessages.NO_CREDENTIAL_AVAILABLE)
                    }

                    is GoogleIdTokenParsingException -> {
                        Timber.tag(TAG).e(e, "GoogleIdTokenParsingException")
                        e
                    }

                    is SecurityException -> {
                        Timber.tag(TAG).e(e, "SecurityException")
                        e
                    }

                    else -> {
                        Timber.tag(TAG).e(e, "Unknown error occurred")
                        IllegalStateException(ErrorMessages.UNKNOWN_ERROR)
                    }
                }
            )
        }
    )
    
    companion object {
        const val TAG = "GoogleLogin"
    }
}