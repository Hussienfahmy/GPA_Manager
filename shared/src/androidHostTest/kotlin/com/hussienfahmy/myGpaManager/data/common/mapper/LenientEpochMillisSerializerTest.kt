package com.hussienfahmy.myGpaManager.data.common.mapper

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith

// Real Crashlytics crash: GitLive's FirebaseDecoderImpl.decodeLong() throws when the raw
// Firestore value is a legacy Timestamp instead of an epoch-millis Long - reproduced here via a
// fake Decoder that throws the same way. Plain Long.serializer() lets it crash the whole document
// decode ("before"); LenientEpochMillisSerializer swallows it ("after", the actual fix).
@RunWith(AndroidJUnit4::class)
@SmallTest
class LenientEpochMillisSerializerTest {

    private fun decoderThrowingOn(value: Any) = mockk<Decoder> {
        every { decodeLong() } throws SerializationException("Expected $value to be long")
    }

    @Test
    fun `before fix - plain LongSerializer crashes on Timestamp-shaped value`() {
        val decoder = decoderThrowingOn("Timestamp(seconds=1758378662, nanoseconds=474000000)")

        assertThrows(SerializationException::class.java) {
            Long.serializer().deserialize(decoder)
        }
    }

    @Test
    fun `after fix - LenientEpochMillisSerializer returns zero on Timestamp-shaped value`() {
        val decoder = decoderThrowingOn("Timestamp(seconds=1758378662, nanoseconds=474000000)")

        val result = LenientEpochMillisSerializer.deserialize(decoder)

        assertThat(result).isEqualTo(0L)
    }

    @Test
    fun `after fix - a normal epoch-millis value still decodes correctly`() {
        val decoder = mockk<Decoder> {
            every { decodeLong() } returns 1758378662474L
        }

        val result = LenientEpochMillisSerializer.deserialize(decoder)

        assertThat(result).isEqualTo(1758378662474L)
    }
}
