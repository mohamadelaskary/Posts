package com.example.posts.Ui.Posts

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exampel.posts.Model.Post
import com.exampel.posts.Model.Status
import com.exampel.posts.Model.StatusWithMessage
import com.exampel.posts.Network.ApiInterface
import com.example.posts.Network.ApiFactory
import com.example.posts.di.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.create
import java.lang.Exception
import javax.inject.Inject
@HiltViewModel
class PostsViewModel @Inject constructor(
    application: MyApplication,
    val apiInterface: ApiInterface
    ) : AndroidViewModel(application) {
    val getPostsLiveData = MutableLiveData<List<Post>>()
    val getPostsStatus   = MutableLiveData<StatusWithMessage>()
    fun getPosts(){
        getPostsStatus.postValue(StatusWithMessage(Status.LOADING))
        viewModelScope.launch {
            try {
                val postsResponse = apiInterface.getPosts()
                if (postsResponse.isSuccessful){
                    getPostsStatus.postValue(StatusWithMessage(Status.SUCCESS))
                    getPostsLiveData.postValue(postsResponse.body())
                    Log.d("PostsViewModel", "getPosts: ${postsResponse.body()}")
                } else {
                    getPostsStatus.postValue(StatusWithMessage(Status.ERROR,postsResponse.message()))
                }
            } catch (ex:Exception){
                getPostsStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,"Error in getting data"))
            }
        }
    }
}