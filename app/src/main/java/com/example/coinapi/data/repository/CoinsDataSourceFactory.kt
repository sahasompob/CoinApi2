package com.example.coinapi.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.coinapi.data.api.Api
import com.example.coinapi.data.vo.Coin
import io.reactivex.disposables.CompositeDisposable

class CoinsDataSourceFactory(
    private val apiService: Api,
    private val disposables: CompositeDisposable
) : DataSource.Factory<Int, Coin>() {

    val sourceLiveData = MutableLiveData<CoinsDataSource>()

    override fun create(): DataSource<Int, Coin> {
        val source = CoinsDataSource(apiService, disposables)
        sourceLiveData.postValue(source)
        return source
    }
}