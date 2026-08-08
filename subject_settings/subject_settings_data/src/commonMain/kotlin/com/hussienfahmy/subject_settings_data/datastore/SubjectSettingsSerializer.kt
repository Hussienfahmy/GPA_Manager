package com.hussienfahmy.subject_settings_data.datastore

import androidx.datastore.core.okio.OkioSerializer
import com.hussienfahmy.core.domain.crash.CrashReporter
import com.hussienfahmy.subject_settings_data.model.SubjectSettings
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

internal class SubjectSettingsSerializer(
    private val crashReporter: CrashReporter
) : OkioSerializer<SubjectSettings> {

    override val defaultValue: SubjectSettings
        get() = SubjectSettings()

    override suspend fun readFrom(source: BufferedSource): SubjectSettings {
        return try {
            Json.decodeFromString(
                deserializer = SubjectSettings.serializer(),
                string = source.readUtf8()
            )
        } catch (e: SerializationException) {
            crashReporter.recordException(e)
            defaultValue
        }
    }

    override suspend fun writeTo(t: SubjectSettings, sink: BufferedSink) {
        sink.writeUtf8(
            Json.encodeToString(
                serializer = SubjectSettings.serializer(),
                value = t
            )
        )
    }
}
