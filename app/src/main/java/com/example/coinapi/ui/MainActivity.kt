package com.example.coinapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinapi.R
import com.example.coinapi.data.api.Api
import com.example.coinapi.data.api.CoinsClient
import com.example.coinapi.data.repository.NetworkState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinsViewModel
    lateinit var coinsRepository: CoinsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService : Api = CoinsClient.getClient()

        coinsRepository = CoinsRepository(apiService)

        viewModel = getViewModel()

        val coinsPageAdapter = CoinsPageAdapter(this)
        val linearLayout = LinearLayoutManager(this)
        rv_movie_list.layoutManager = linearLayout
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = coinsPageAdapter

        viewModel.coinsPagedList.observe(this, Observer {
            coinsPageAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                coinsPageAdapter.setNetworkState(it)
            }
        })
    }


    private fun getViewModel(): CoinsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CoinsViewModel(coinsRepository) as T
            }
        })[CoinsViewModel::class.java]
    }
}
