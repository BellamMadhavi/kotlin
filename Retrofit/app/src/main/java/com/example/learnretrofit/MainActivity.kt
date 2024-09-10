package com.example.learnretrofit

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.learnretrofit.databinding.MainActivityBinding
import com.example.learnretrofit.ui.theme.LearnRetrofitTheme
import retrofit2.Response
import retrofit2.create

class MainActivity : ComponentActivity() {
    private lateinit var binding: MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitInstance.getRetrofitInstance().create(AlbumService::class.java)

        val responseLiveData : LiveData<Response<Albums>> =
            liveData {
                val  response = retrofitService.getAlbums()
                emit(response)
            }
        responseLiveData.observe(this,{
            val albumList = it.body()?.listIterator()
            if(albumList != null){
                while (albumList.hasNext()){
                    val albumItem = albumList.next()
                    val albumTitle = "Album title: ${albumItem.title} \n"
                    binding.titleTextView.append(albumTitle)
                }
            }
        })
    }
}
