package com.acon.core.social.client

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import com.acon.acon.core.model.model.user.CredentialCode
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
class GoogleAuthClientTest {

    @MockK
    private lateinit var context: Context

    @MockK
    private lateinit var getSignInWithGoogleOption: GetSignInWithGoogleOption

    @MockK
    private lateinit var credentialManager: CredentialManager

    @InjectMockKs
    private lateinit var googleAuthClient: GoogleAuthClient

    @Test
    fun `getCredentialCode - Credential이 CustomCredential이고, type이 TYPE_GOOGLE_ID_TOKEN_CREDENTIAL일 경우에만 idToken 정상 반환`() =
        runTest {
            val testIdToken = "test-google-id-token-12345"

            val mockCredential = mockk<CustomCredential>()
            every { mockCredential.type } returns GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            every { mockCredential.data } returns mockk(relaxed = true)

            val mockTokenCredential = mockk<GoogleIdTokenCredential>()
            every { mockTokenCredential.idToken } returns testIdToken

            mockkObject(GoogleIdTokenCredential.Companion)
            every { GoogleIdTokenCredential.createFrom(mockCredential.data) } returns mockTokenCredential

            val mockCredentialResponse = mockk<GetCredentialResponse>()
            every { mockCredentialResponse.credential } returns mockCredential

            coEvery {
                credentialManager.getCredential(
                    request = any<GetCredentialRequest>(),
                    context = any<Context>()
                )
            } returns mockCredentialResponse

            val result: CredentialCode? = googleAuthClient.getCredentialCode()

            assertEquals(testIdToken, result?.value)

            unmockkObject(GoogleIdTokenCredential.Companion)
        }

    @ParameterizedTest
    @MethodSource("unsupportedCredentialProvider")
    fun `getCredentialCode - 지원하지 않는 Credential 타입일 때 null 반환`(mockCredential: Credential) = runTest {
        val mockCredentialResponse = mockk<GetCredentialResponse>()
        every { mockCredentialResponse.credential } returns mockCredential

        coEvery {
            credentialManager.getCredential(
                request = any<GetCredentialRequest>(),
                context = any<Context>()
            )
        } returns mockCredentialResponse

        val result = googleAuthClient.getCredentialCode()

        assertNull(result)
    }

    @Test
    fun `getCredentialCode - CredentialManager에서 예외가 발생 시 null 반환`() = runTest {
        coEvery { credentialManager.getCredential(
            request = any<GetCredentialRequest>(),
            context = any<Context>())
        } throws Exception("Authentication failed")

        val result = googleAuthClient.getCredentialCode()

        assertNull(result)
    }

    companion object {
        @JvmStatic
        fun unsupportedCredentialProvider(): Stream<Credential> {
            val unsupportedCustomCredential = mockk<CustomCredential>().apply {
                every { type } returns "unsupported.custom.credential.type"
            }
            val publicKeyCredential = mockk<PublicKeyCredential>()
            val passwordCredential = mockk<PasswordCredential>()
            val unknownCredential = mockk<Credential>()

            return Stream.of(
                unsupportedCustomCredential,
                publicKeyCredential,
                passwordCredential,
                unknownCredential
            )
        }
    }
}