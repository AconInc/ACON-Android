package com.acon.core.social.client

import com.acon.acon.core.model.model.user.CredentialCode
import com.acon.acon.core.model.model.user.SocialPlatform

/**
 * 소셜 플랫폼 인증 클라이언트 인터페이스
 */
interface SocialAuthClient {

    val platform: SocialPlatform

    /**
     * 소셜 플랫폼의 자격 증명 코드를 가져옵니다.
     * 이 코드는 백엔드에서 사용자를 인증하는 데 사용할 수 있습니다.
     *
     * @return 자격 증명 코드. 실패한 경우 null 반환
     */
    suspend fun getCredentialCode() : CredentialCode?
}