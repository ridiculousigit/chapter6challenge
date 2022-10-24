@file:OptIn(DelicateCoroutinesApi::class)

package binar.academy.chapter6challenge.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import binar.academy.chapter6challenge.api.APIService
import binar.academy.chapter6challenge.database.AgentModel
import binar.academy.chapter6challenge.database.FavoriteDao
import binar.academy.chapter6challenge.model.AgentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val client: APIService, val db: FavoriteDao) : ViewModel() {

    val listAgent: MutableLiveData<List<AgentResponse>?> = MutableLiveData()
    val listAgentFavorite: MutableLiveData<List<AgentModel>?> = MutableLiveData()
    val isFavoriteAgent: MutableLiveData<Boolean> = MutableLiveData()
    val favoritAgentAdd: MutableLiveData<AgentModel> = MutableLiveData()
    val deleteFavoriteAgent: MutableLiveData<Boolean> = MutableLiveData()

    fun callListAgent() {
        client.getAgentList().enqueue(object : Callback<List<AgentResponse>> {
            override fun onResponse(
                call: Call<List<AgentResponse>>,
                response: Response<List<AgentResponse>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        listAgent.postValue(data)
                    }
                } else {
                    Log.e("Error : ", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<AgentResponse>>, t: Throwable) {
                Log.e("Error ; ", "onFailure: ${t.message}")
            }
        })
    }

    fun getAllFavoriteAgent() {
        GlobalScope.launch {
            listAgentFavorite.postValue(db.getAllFavorite())
        }
    }

    fun checkIfFavoriteAgent(name: String) {
        GlobalScope.launch {
            isFavoriteAgent.postValue(db.isFavorite(name))
        }
    }

    fun addFavoriteAgent(agentModel: AgentModel) {
        GlobalScope.launch {
            db.addFavorite(agentModel)
            favoritAgentAdd.postValue(agentModel)
        }
    }

    fun deleteFavoriteAgent(name: String) {
        GlobalScope.launch {
            db.deleteFavorite(name)
            deleteFavoriteAgent.postValue(true)
        }
    }

}