package com.hussienfahmy.gpa_system_settings_data.datastore

import androidx.datastore.core.okio.OkioSerializer
import com.hussienfahmy.gpa_system_settings_data.model.GPA
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

internal object GPASerializer : OkioSerializer<GPA> {

    override val defaultValue: GPA
        get() = GPA()

    override suspend fun readFrom(source: BufferedSource): GPA {
        return try {
            Json.decodeFromString(
                deserializer = GPA.serializer(),
                string = source.readUtf8()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: GPA, sink: BufferedSink) {
        sink.writeUtf8(
            Json.encodeToString(
                serializer = GPA.serializer(),
                value = t
            )
        )
    }
}
