package com.trebit.reststudy.data.remote

import com.trebit.reststudy.data.model.UserDataVo
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Rest_study
 * Class: ApiService
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

interface ApiService {

    @POST("user/sign_up")
    fun createUser(@Body body: UserDataVo): Single<UserDataVo>

    @GET("user/getUser/{email}")
    fun getUser(@Path("email") email: String): Single<UserDataVo>

}
