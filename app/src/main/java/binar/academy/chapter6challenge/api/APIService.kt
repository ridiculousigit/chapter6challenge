package binar.academy.chapter6challenge.api

import binar.academy.chapter6challenge.model.AgentResponse
import retrofit2.Call
import retrofit2.http.GET

interface APIService {

    @GET("Agent")
    fun getAgentList() : Call<List<AgentResponse>>

}
