package com.acon.core.social.client

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import com.acon.acon.core.model.model.user.CredentialCode
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ActivityContext
import timber.log.Timber
import javax.inject.Inject

class GoogleAuthClient @Inject constructor() : SocialAuthClient {

    override suspend fun getCredentialCode(@ActivityContext context: Context): CredentialCode {
        val credentialOption: GetSignInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_CLIENT_ID).build()

        val credentialManager: CredentialManager = CredentialManager.create(context)

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(credentialOption)
            .build()

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
                    throw UnsupportedOperationException("지원되지 않거나 알 수 없는 인증 유형입니다.")
                }
            }

            is PublicKeyCredential -> {
                Timber.e("Credential is PublicKeyCredential. Unsupported.")
                throw UnsupportedOperationException("지원되지 않는 사용자 인증 유형입니다.")
            }

            is PasswordCredential -> {
                Timber.e("Credential is PasswordCredential. Unsupported.")
                throw UnsupportedOperationException("지원되지 않는 사용자 인증 유형입니다.")
            }

            else -> {
                Timber.e("Unknown credential class: ${credential::class.java}")
                throw UnsupportedOperationException("지원되지 않거나 알 수 없는 인증 유형입니다.")
            }
        }
    }
}

