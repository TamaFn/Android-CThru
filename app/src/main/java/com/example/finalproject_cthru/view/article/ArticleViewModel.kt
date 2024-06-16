package com.example.finalproject_cthru.view.article

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject_cthru.data.remote.response.ArticleResponse
import com.example.finalproject_cthru.data.remote.response.ArticlesItem
import com.example.finalproject_cthru.data.remote.response.Source
import com.example.finalproject_cthru.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ArticleViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<ArticlesItem>?>()
    val articles: MutableLiveData<List<ArticlesItem>?> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var allArticles: List<ArticlesItem> = listOf()

    fun setMockData(context: Context) {
        _isLoading.value = false

        val articlesList = ArticleResponse.setMockData(context)
        allArticles = articlesList ?: listOf()
        _articles.value = allArticles
    }

    fun searchArticles(query: String) {
        val filteredArticles = if (query.isBlank()) {
            allArticles
        } else {
            allArticles.filter {
                it.title?.contains(query, ignoreCase = true) == true ||
                        it.description?.contains(query, ignoreCase = true) == true
            }
        }
        _articles.value = filteredArticles
    }

    companion object {
        private const val TAG = "ArticleViewModel"
    }
}


