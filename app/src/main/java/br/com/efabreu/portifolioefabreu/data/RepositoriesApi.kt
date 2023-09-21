package br.com.efabreu.portifolioefabreu.data

import br.com.efabreu.portifolioefabreu.model.Repository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RepositoriesApi {

    @GET("/users/{user}/repos")
    fun getUser(
        @Path("user") user :String
    ): Call<List<Repository>>

}