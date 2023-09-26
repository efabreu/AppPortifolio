package br.com.efabreu.portifolioefabreu.data

import br.com.efabreu.portifolioefabreu.model.Owner
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OwnerApi {

    @GET("/users/{user}")
    fun getUser(
        @Path("user") user :String
    ): Call<Owner>

}