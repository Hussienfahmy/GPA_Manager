package com.hussienfahmy.myGpaManager.data.common.mapper

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// Documents written before the switch to GitLive Firestore store certain timestamp fields as a
// native Firestore Timestamp (the old Android SDK's server-timestamp type), not the epoch-millis
// Long used now (see TimestampMapper.kt for why). These fields are write-only bookkeeping - so on
// a legacy Timestamp-shaped document there's nothing to recover; just don't let it crash the
// whole document decode.
object LenientEpochMillisSerializer : KSerializer<Long> {
    override val descriptor = PrimitiveSerialDescriptor("LenientEpochMillis", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Long) = encoder.encodeLong(value)

    override fun deserialize(decoder: Decoder): Long {
        return try {
            decoder.decodeLong()
        } catch (e: Exception) {
            0L
        }
    }
}
