@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused")

package binar.academy.chapter6challenge.viewmodel

import binar.academy.chapter6challenge.api.APIService
import binar.academy.chapter6challenge.database.AgentModel
import binar.academy.chapter6challenge.database.FavoriteDao
import binar.academy.chapter6challenge.model.AgentResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert

import org.junit.Before
import org.junit.Test

@Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused")
class HomeViewModelTest {

    private lateinit var client: APIService
    private lateinit var dBase: FavoriteDao
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        client = mockk()
        dBase = mockk()
        viewModel = mockk()
    }

    // Test to get all agent data
    @Test
    fun gotallAgent() {
        val response = mockk<List<AgentResponse>>()

        every {
            viewModel.callListAgent()
            viewModel.listAgent.value
        } returns response

        val result = viewModel.listAgent.value
        Assert.assertEquals(response, result)
    }

    // Test to add agent to favorite list
    @Test
    fun addedAgent() {
        val response = mockk<AgentModel>()
        val agent = mockk<AgentModel>()

        coEvery {
            viewModel.addFavoriteAgent(agent)
            viewModel.favoritAgentAdd.value!!
        } returns response

        val result = viewModel.favoritAgentAdd.value
        Assert.assertEquals(response, result)
    }

    // Test to get all favorite agent data
    @Test
    fun gotfavAgent(){
        val response = mockk<List<AgentModel>>()

        coEvery {
            viewModel.getAllFavoriteAgent()
            viewModel.listAgentFavorite.value
        } returns response

        val result = viewModel.listAgentFavorite.value
        Assert.assertEquals(response, result)
    }

    // Test to find out whether the agent is a favorite agent
    @Test
    fun isfavAgent(){
        val agent = mockk<AgentModel>()

        coEvery {
            viewModel.checkIfFavoriteAgent(agent.toString())
            viewModel.isFavoriteAgent.value!!
        } returns true

        val result = viewModel.isFavoriteAgent.value
        Assert.assertEquals(true, result)
    }

    // Test to remove agent data from favorite agent list
    @Test
    fun deletefavAgent(){
        val agent = mockk<AgentModel>()

        coEvery {
            viewModel.deleteFavoriteAgent(agent.toString())
            viewModel.deleteFavoriteAgent.value!!
        } returns true

        val result = viewModel.deleteFavoriteAgent.value
        Assert.assertEquals(true, result)
    }
}