package com.hussienfahmy.gpa_system_settings_data.datastore

import androidx.datastore.core.Serializer
import com.hussienfahmy.gpa_system_settings_data.model.GPA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object GPASerializer : Serializer<GPA> {

    override val defaultValue: GPA
        get() = GPA()

    override suspend fun readFrom(input: InputStream): GPA {
        return try {
            Json.decodeFromString(
                deserializer = GPA.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: GPA, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = GPA.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}