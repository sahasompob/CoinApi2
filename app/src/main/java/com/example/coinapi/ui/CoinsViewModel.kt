package com.example.coinapi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.coinapi.data.repository.NetworkState
import com.example.coinapi.data.vo.Coin
import io.reactivex.disposables.CompositeDisposable

class CoinsViewModel(private val coinsRepository : CoinsRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  coinsPagedList : LiveData<PagedList<Coin>> by lazy {
        coinsRepository.fetchLiveCoinsPagedList(compositeDisposable)

    }

    val  networkState : LiveData<NetworkState> by lazy {
        coinsRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return coinsPagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}