package com.example.coinapi.data.vo


import com.google.gson.annotations.SerializedName

data class CoinBaseResponse(
    val status: String,
    val data: Data
)

data class Data(
    val stats: Stats,
    val base: Base,
    val coin: Coin,
    val coins: MutableList<Coin>
)

data class Stats(
    val total: Int,
    val offset: Int,
    val limit: Int

)

data class Base(
    val symbol: String,
    val sign: String
)

data class Coin(
    val id: Int,
    val symbol: String?,
    val name: String?,
    val description: String?,
    val iconUrl: String?

)