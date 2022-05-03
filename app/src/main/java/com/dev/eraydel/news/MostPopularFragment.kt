package com.dev.eraydel.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.eraydel.news.adapters.ArticlesAdapter
import com.dev.eraydel.news.apis.APIService
import com.dev.eraydel.news.databinding.FragmentMostPopularBinding
import com.dev.eraydel.news.listeners.OnItemClickListener
import com.dev.eraydel.news.models.Article
import com.dev.eraydel.news.models.NYTResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.thread

class MostPopularFragment : Fragment() , OnItemClickListener {
    private lateinit var adapter: ArticlesAdapter
    private lateinit var binding: FragmentMostPopularBinding
    private val news = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMostPopularBinding.inflate(inflater,container,false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Most Popular News"
        initRecyclerView()
        getArticles()
        return binding.root
    }

    private fun getArticles()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<NYTResponse> = getRetrofit().create(APIService::class.java).getMostPopular("7.json?api-key=FzYDCMjO0IVaMB4l41OzMGVnuOT57rwV")
            val nYTResponse: NYTResponse? =  call.body()
            requireActivity().runOnUiThread {
                if(call.isSuccessful)
                {
                    val articles: List<Article> = nYTResponse?.results ?: emptyList()
                    if(!articles.isEmpty())
                    {
                        news.clear()
                        news.addAll(articles)
                        adapter.notifyDataSetChanged()
                        binding.browser1.loadUrl(articles[0].url)
                    }
                }
            }
        }

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/mostpopular/v2/emailed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun initRecyclerView(){

        adapter = ArticlesAdapter(news,this)

        binding.barraNoticias.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        binding.barraNoticias.adapter = adapter

    }

     override fun onItemClick(noticia: Article) {
        binding.browser1.loadUrl(noticia.url)
    }
}