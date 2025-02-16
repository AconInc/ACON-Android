package com.acon.acon.domain.error.user

sealed class CredentialException(message: String) : Exception(message) {

    data class UserCanceled(
        override val message: String
    ) : CredentialException(message)

    data class NoStoredCredentials(
        override val message: String
    ) : CredentialException(message)
}
