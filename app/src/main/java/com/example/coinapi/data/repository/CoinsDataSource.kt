package com.example.coinapi.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.coinapi.data.api.Api
import com.example.coinapi.data.vo.Coin
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CoinsDataSource (private val apiService : Api, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Coin>(){


    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Coin>) {

        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.fetchCoins(0,10)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        networkState.postValue(NetworkState.LOADED)
                        callback.onResult(it.data.coins, 0, 10)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.fetchCoins(params.key,params.requestedLoadSize)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        networkState.postValue(NetworkState.LOADED)
                        callback.onResult(it.data.coins, params.key + 10)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Coin>) {

    }
}