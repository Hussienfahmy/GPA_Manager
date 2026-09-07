package com.hussienfahmy.core.data.local

import android.content.Context
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.sqlite.execSQL
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Runs the real APP_DATABASE_MIGRATIONS list used by DatabaseModule.kt.
@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        file = context.getDatabasePath(TEST_DB),
        driver = AndroidSQLiteDriver(),
        databaseClass = AppDatabase::class,
        databaseFactory = AppDatabaseConstructor::initialize,
    )

    @Test
    fun migrate13To14_backfillsGpaCreditHoursFromTotalCreditHours() {
        helper.createDatabase(13).apply {
            execSQL(
                """
                INSERT INTO semester
                    (id, label, level, type, semesterGPA, totalCreditHours, status, `order`, createdAt, archivedAt)
                VALUES
                    (1, 'Year 1, Semester 1', 1, 'SUMMARY', 3.2, 17, 'ARCHIVED', 1, 0, 0)
                """.trimIndent()
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate(14, APP_DATABASE_MIGRATIONS.toList())
        migrated.prepare("SELECT gpaCreditHours FROM semester WHERE id = 1").use { statement ->
            statement.step()
            assertThat(statement.getLong(0)).isEqualTo(17L)
        }
        migrated.close()
    }

    private companion object {
        const val TEST_DB = "migration-test"
    }
}
