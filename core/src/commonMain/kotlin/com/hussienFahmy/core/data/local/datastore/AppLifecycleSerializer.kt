package com.hussienfahmy.core.data.local.datastore

import androidx.datastore.core.okio.OkioSerializer
import com.hussienfahmy.core.domain.crash.CrashReporter
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

internal class AppLifecycleSerializer(
    private val crashReporter: CrashReporter,
) : OkioSerializer<AppLifecycleState> {

    override val defaultValue: AppLifecycleState
        get() = AppLifecycleState()

    override suspend fun readFrom(source: BufferedSource): AppLifecycleState {
        return try {
            Json.decodeFromString(
                deserializer = AppLifecycleState.serializer(),
                string = source.readUtf8(),
            )
        } catch (e: SerializationException) {
            crashReporter.recordException(e)
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppLifecycleState, sink: BufferedSink) {
        sink.writeUtf8(
            Json.encodeToString(
                serializer = AppLifecycleState.serializer(),
                value = t,
            )
        )
    }
}
