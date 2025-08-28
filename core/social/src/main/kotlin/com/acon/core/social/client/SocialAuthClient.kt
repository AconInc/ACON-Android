package com.acon.core.social.client

import android.content.Context
import com.acon.acon.core.model.model.user.CredentialCode
import dagger.hilt.android.qualifiers.ActivityContext

interface SocialAuthClient {

    suspend fun getCredentialCode(
        @ActivityContext context: Context
    ) : CredentialCode
}