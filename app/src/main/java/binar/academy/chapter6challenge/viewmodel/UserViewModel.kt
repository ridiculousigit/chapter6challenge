@file:OptIn(DelicateCoroutinesApi::class)

package binar.academy.chapter6challenge.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import binar.academy.chapter6challenge.database.DataStoreUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(@ApplicationContext appContext: Context) : ViewModel() {

    var livedataUsername: MutableLiveData<String> = MutableLiveData()
    var livedataEmail: MutableLiveData<String> = MutableLiveData()
    var livedataPassword: MutableLiveData<String> = MutableLiveData()
    var livedataIsLogin: MutableLiveData<Boolean> = MutableLiveData()
    var dataUser: MutableLiveData<String> = MutableLiveData()
    private val userStore: DataStoreUser = DataStoreUser(appContext)

    fun callDataUser(lifecycle: LifecycleOwner) {
        getUsername(lifecycle)
        getEmail(lifecycle)
        getPassword(lifecycle)
    }

    fun saveData(username: String, email: String, password: String) {
        GlobalScope.launch {
            userStore.saveData(username, email, password)
        }
    }

    fun getUsername(lifecycle: LifecycleOwner) {
        userStore.getUsername.asLiveData().observe(lifecycle) {
            livedataUsername.postValue(it)
        }
    }

    fun getEmail(lifecycle: LifecycleOwner) {
        userStore.getEmail.asLiveData().observe(lifecycle) {
            livedataEmail.postValue(it)
        }
    }

    fun getPassword(lifecycle: LifecycleOwner) {
        userStore.getPassword.asLiveData().observe(lifecycle) {
            livedataPassword.postValue(it)
        }
    }

    fun getUserData(lifecycle: LifecycleOwner) {
        userStore.getDataUser.asLiveData().observe(lifecycle) {
            dataUser.postValue(it)
        }
    }

    fun checkIsLogin(lifecycle: LifecycleOwner) {
        userStore.getIsLogin.asLiveData().observe(lifecycle) {
            livedataIsLogin.postValue(it)
        }
    }

    fun saveLoginStatus(status: Boolean) {
        GlobalScope.launch {
            userStore.setLogin(status)
        }
    }

    fun removeLoginStatus() {
        GlobalScope.launch {
            userStore.removeLogin()
        }
    }
}