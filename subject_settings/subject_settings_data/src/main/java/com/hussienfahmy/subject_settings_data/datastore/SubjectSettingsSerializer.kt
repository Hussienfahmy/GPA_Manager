package com.hussienfahmy.subject_settings_data.datastore

import androidx.datastore.core.Serializer
import com.hussienfahmy.subject_settings_data.model.SubjectSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object SubjectSettingsSerializer : Serializer<SubjectSettings> {

    override val defaultValue: SubjectSettings
        get() = SubjectSettings()

    override suspend fun readFrom(input: InputStream): SubjectSettings {
        return try {
            Json.decodeFromString(
                deserializer = SubjectSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SubjectSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = SubjectSettings.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}