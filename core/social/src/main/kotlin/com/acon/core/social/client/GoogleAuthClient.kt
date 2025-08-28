package com.acon.core.social.client

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import com.acon.acon.core.model.model.user.CredentialCode
import com.acon.acon.core.model.model.user.SocialPlatform
import com.acon.core.social.BuildConfig
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ActivityContext
import timber.log.Timber

class GoogleAuthClient(@ActivityContext private val context: Context) : SocialAuthClient {

    override val platform = SocialPlatform.GOOGLE

    val credentialOption: GetSignInWithGoogleOption =
        GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_CLIENT_ID).build()

    val credentialManager: CredentialManager = CredentialManager.create(context)

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(credentialOption)
        .build()

    override suspend fun getCredentialCode(): CredentialCode? {
        try {
            val credentialResponse = credentialManager.getCredential(
                request = request,
                context = context
            )

            when (val credential = credentialResponse.credential) {
                is CustomCredential -> {
                    Timber.d("Credential is CustomCredential. Type: ${credential.type}")
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                        return CredentialCode(idToken)
                    } else {
                        Timber.e("Unknown credential type")
                        return null
                    }
                }

                is PublicKeyCredential -> {
                    Timber.e("Credential is PublicKeyCredential. Unsupported.")
                    return null
                }

                is PasswordCredential -> {
                    Timber.e("Credential is PasswordCredential. Unsupported.")
                    return null
                }

                else -> {
                    Timber.e("Unknown credential class: ${credential::class.java}")
                    return null
                }
            }
        } catch (e: Exception) {
            return null
        }
    }
}

