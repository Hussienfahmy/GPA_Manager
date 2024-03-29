package com.hussienFahmy.core.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hussienFahmy.core.data.local.entity.Grade
import com.hussienFahmy.core.data.local.entity.Subject
import com.hussienFahmy.core.data.local.model.GradeName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class SubjectDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var subjectDao: SubjectDao
    private val templateSubject = Subject(
        id = 10,
        name = "Subject 1",
        creditHours = 4.0,
        totalMarks = 100.0
    )

    private lateinit var gradeDao: GradeDao

    @Before
    fun setUp() = runTest {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        subjectDao = db.subjectDao

        subjectDao.upsert(templateSubject)

        // add initial grades data
        gradeDao = db.gradeDao
        Grade.generateInitialData().forEach {
            gradeDao.upsert(it)
        }
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun test() = runTest {
        val subjectsWithGrade = subjectDao.subjectsWithAssignedGrade.first()

        MatcherAssert.assertThat(subjectsWithGrade.isEmpty(), `is`(false))
    }

    @Test
    fun getSubjectsWithGrade() = runTest {
        val testGrade = gradeDao.getGradeByName(GradeName.A)

        subjectDao.updateGrade(templateSubject.id, testGrade?.metaData)

        val (_, grade) = subjectDao.subjectsWithAssignedGrade.map {
            it.toList()
        }.first().first()

        assertEquals(testGrade, grade)
    }

    @Test
    fun getSubjects() = runTest {
        val loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first

        assertEquals(templateSubject, loaded)
    }

    @Test
    fun upsert() = runTest {
        val subject = Subject(
            id = 1,
            name = "Subject 2",
            creditHours = 4.0,
            totalMarks = 200.0
        )

        subjectDao.upsert(subject)

        val loaded =
            subjectDao.subjectsWithAssignedGrade.map { subjectGradeMap ->
                subjectGradeMap.keys.find { it.id == subject.id }
            }.first()

        assertNotNull(loaded)
        assertEquals(subject, loaded)
    }

    @Test
    fun updateName() = runTest {
        val testName = "Test"
        subjectDao.updateName(templateSubject.id, testName)
        val loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first

        assertEquals(testName, loaded.name)
    }

    @Test
    fun updateCreditHours() = runTest {
        val testCreditHours = 4.0
        subjectDao.updateCreditHours(templateSubject.id, testCreditHours)

        val loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first
        assertEquals(testCreditHours, loaded.creditHours, 0.0)
    }

    @Test
    fun updateTotalMarks() = runTest {
        val testTotalMarks = 200.0
        subjectDao.updateTotalMarks(templateSubject.id, testTotalMarks)

        val loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first
        assertEquals(testTotalMarks, loaded.totalMarks, 0.0)
    }

    @Test
    fun updateGradeOrder() = runTest {
        val testGradeName = GradeName.APlus
        subjectDao.updateGrade(templateSubject.id, testGradeName)

        val loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first
        assertEquals(testGradeName, loaded.gradeName)
    }

    @Test
    fun updateMidterm() = runTest {
        val testMidterm = 15.0
        subjectDao.updateMidterm(templateSubject.id, testMidterm)

        val loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first

        assertEquals(testMidterm, loaded.semesterMarks?.midterm!!, 0.0)
    }

    @Test
    fun updatePractical() = runTest {
        val testPractical = 15.0
        subjectDao.updatePractical(templateSubject.id, testPractical)

        val loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first

        assertEquals(testPractical, loaded.semesterMarks?.practical!!, 0.0)
    }

    @Test
    fun updateOral() = runTest {
        val testOral = 15.0
        subjectDao.updateOral(templateSubject.id, testOral)

        val loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first

        assertEquals(testOral, loaded.semesterMarks?.oral!!, 0.0)
    }

    // Tests for max_grade
    @Test
    fun noSemesterWork_maxGrade() = runTest {
        subjectDao.updateMidterm(templateSubject.id, null)
        subjectDao.updatePractical(templateSubject.id, null)
        subjectDao.updateOral(templateSubject.id, null)

        val loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first

        assertEquals(GradeName.A, loaded.metadata.maxGradeNameCanAchieve)
    }

    @Test
    fun semesterWorkAndMaxGrade() = runTest {
        subjectDao.updateMidterm(templateSubject.id, 5.0)
        subjectDao.updatePractical(templateSubject.id, 10.0)

        var loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first

        assertEquals(GradeName.B, loaded.metadata.maxGradeNameCanAchieve)

        subjectDao.updateMidterm(templateSubject.id, 4.0)
        loaded = subjectDao.subjectsWithAssignedGrade.map { it.toList().first() }.first().first

        assertEquals(GradeName.CPlus, loaded.metadata.maxGradeNameCanAchieve)
    }
}