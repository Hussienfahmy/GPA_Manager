package com.hussienfahmy.core.data.local


import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.model.GradeName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class GradeDaoTest {
    // todo refactor in the future
    private lateinit var db: AppDatabase

    private lateinit var gradeDao: GradeDao

    private lateinit var initialData: List<Grade>

    @Before
    fun setUp() = runTest {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        gradeDao = db.gradeDao

        // add initial grades data
        initialData = Grade.generateInitialData().onEach {
            gradeDao.upsert(it)
        }
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getGradeByName() = runTest {
        val loadedBPlusGrade = gradeDao.getGradeByName(GradeName.BPlus)

        assertEquals(initialData.find { it.metaData == GradeName.BPlus }, loadedBPlusGrade)
    }

    @Test
    fun getGradeByPoints() = runTest {
        val loadedBPlusGrade = gradeDao.getGradeByPoints(3.38)
        val loadedBGrade = gradeDao.getGradeByPoints(3.1)
        val loadedAGrade = gradeDao.getGradeByPoints(3.67)

        assertEquals(initialData.find { it.metaData == GradeName.BPlus }, loadedBPlusGrade)
        assertEquals(initialData.find { it.metaData == GradeName.B }, loadedBGrade)
        assertEquals(initialData.find { it.metaData == GradeName.AMinus }, loadedAGrade)
    }

    @Test
    fun upsert() = runTest {
        val testGrade = initialData.first().copy(percentage = 50.0, points = 3.0, active = true)

        gradeDao.upsert(testGrade)

        val loadedGrade = gradeDao.getGradeByName(testGrade.metaData)

        assertEquals(testGrade, loadedGrade)
    }

    @Test
    fun updatePoints_Value() = runTest {
        val testGradeName = GradeName.A
        val testPoints = 3.55

        gradeDao.updatePoints(testGradeName, testPoints)

        val loadedGrade = gradeDao.getGradeByName(testGradeName)

        assertEquals(testPoints, loadedGrade?.points!!, 0.0)
        assertEquals(true, loadedGrade.active)
    }

    @Test
    fun updatePoints_Null() = runTest {
        val testGradeName = GradeName.A
        val testPoints: Double? = null

        gradeDao.updatePoints(testGradeName, testPoints)

        val loadedGrade = gradeDao.getGradeByName(testGradeName)

        assertEquals(testPoints, loadedGrade?.points)
        assertEquals(false, loadedGrade?.active)
    }

    @Test
    fun updatePercentage_Value() = runTest {
        val testGradeName = GradeName.A
        val testPercentage = 75.8

        gradeDao.updatePercentage(testGradeName, testPercentage)

        val loadedGrade = gradeDao.getGradeByName(testGradeName)

        assertEquals(testPercentage, loadedGrade?.percentage!!, 0.0)
        assertEquals(true, loadedGrade.active)
    }

    @Test
    fun updatePercentage_Null() = runTest {
        val testGradeName = GradeName.A
        val testPercentage: Double? = null

        gradeDao.updatePercentage(testGradeName, testPercentage)

        val loadedGrade = gradeDao.getGradeByName(testGradeName)

        assertEquals(testPercentage, loadedGrade?.percentage)
        assertEquals(false, loadedGrade?.active)
    }

    @Test
    fun setActive() = runTest {
        val testGradeName = GradeName.A
        val testActive = true

        gradeDao.setActive(testGradeName, testActive)

        val loadedGrade = gradeDao.getGradeByName(testGradeName)

        assertEquals(testActive, loadedGrade?.active)
    }
}