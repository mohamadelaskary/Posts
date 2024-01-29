package com.example.posts.Ui.ViewPost

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exampel.posts.Model.Post
import com.exampel.posts.Model.Status
import com.exampel.posts.Model.StatusWithMessage
import com.exampel.posts.Network.ApiInterface
import com.example.posts.di.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ViewPostDetailsViewModel @Inject constructor(
    application: MyApplication,
    val apiInterface: ApiInterface
) : AndroidViewModel(application) {
    val getPostLiveData = MutableLiveData<Post>()
    val getPostStatus   = MutableLiveData<StatusWithMessage>()
    fun getPost(id:Int){
        getPostStatus.postValue(StatusWithMessage(Status.LOADING))
        viewModelScope.launch {
            try {
                val postsResponse = apiInterface.getPostById(id)
                if (postsResponse.isSuccessful){
                    getPostStatus.postValue(StatusWithMessage(Status.SUCCESS))
                    getPostLiveData.postValue(postsResponse.body())
                    Log.d("PostsViewModel", "getPosts: ${postsResponse.body()}")
                } else {
                    getPostStatus.postValue(StatusWithMessage(Status.ERROR,postsResponse.message()))
                }
            } catch (ex: Exception){
                getPostStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,"Error in getting data"))
            }
        }
    }
}