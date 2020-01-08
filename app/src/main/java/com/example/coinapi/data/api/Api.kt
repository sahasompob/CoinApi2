package com.example.coinapi.data.api

import com.example.coinapi.data.vo.CoinBaseResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("coins")
    fun fetchCoins(
        @Query("offset") offset: Int,
        @Query("limit") requestedLoadSize: Int
    ) : Single<CoinBaseResponse>

}