package com.example.coinapi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.coinapi.data.api.Api
import com.example.coinapi.data.repository.CoinsDataSource
import com.example.coinapi.data.repository.CoinsDataSourceFactory
import com.example.coinapi.data.repository.NetworkState
import com.example.coinapi.data.vo.Coin
import io.reactivex.disposables.CompositeDisposable

class CoinsRepository (private val apiService : Api) {

    lateinit var coinsPagedList: LiveData<PagedList<Coin>>
    lateinit var coinsDataSourceFactory:  CoinsDataSourceFactory

    fun fetchLiveCoinsPagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Coin>> {
        coinsDataSourceFactory = CoinsDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()

        coinsPagedList = LivePagedListBuilder(coinsDataSourceFactory, config).build()

        return coinsPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<CoinsDataSource, NetworkState>(
            coinsDataSourceFactory.sourceLiveData, CoinsDataSource::networkState)
    }
}