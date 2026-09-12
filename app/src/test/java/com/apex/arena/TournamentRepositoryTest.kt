package com.apex.arena

import com.apex.arena.data.repository.TournamentRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TournamentRepositoryTest {

    private lateinit var repository: TournamentRepositoryImpl

    @Before
    fun setUp() {
        repository = TournamentRepositoryImpl()
    }

    @Test
    fun getTournaments_returnsListWithValidData() = runTest {
        val list = repository.getTournaments().first()
        assertTrue(list.isNotEmpty())
        val firstItem = list.first()
        assertNotNull(firstItem.title)
        assertTrue(firstItem.prizePool > 0)
    }

    @Test
    fun getTournaments_withFilter_filtersProperly() = runTest {
        val filteredList = repository.getTournaments(filterFormat = "SOLO").first()
        assertTrue(filteredList.all { it.format == "SOLO" })
    }

    @Test
    fun registerForTournament_returnsSuccess() = runTest {
        val result = repository.registerForTournament("t1", "user_demo_01")
        assertTrue(result.isSuccess)
    }
}
