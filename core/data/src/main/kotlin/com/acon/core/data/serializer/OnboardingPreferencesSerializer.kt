package com.acon.core.data.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.acon.core.data.dto.entity.OnboardingPreferencesEntity
import com.acon.core.data.dto.entity.copy
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class OnboardingPreferencesSerializer @Inject constructor() : Serializer<OnboardingPreferencesEntity>{

    override val defaultValue: OnboardingPreferencesEntity = OnboardingPreferencesEntity.getDefaultInstance().copy {
        shouldShowIntroduce = true
        hasTastePreference = false
        hasVerifiedArea = false
    }

    override suspend fun readFrom(input: InputStream): OnboardingPreferencesEntity {
        return try {
            OnboardingPreferencesEntity.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: OnboardingPreferencesEntity, output: OutputStream) {
        t.writeTo(output)
    }
}