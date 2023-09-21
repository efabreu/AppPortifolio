package br.com.efabreu.portifolioefabreu.data

import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    lateinit var retrofit :Retrofit

    constructor(){
        initializeRetrofit()
    }
    private fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}