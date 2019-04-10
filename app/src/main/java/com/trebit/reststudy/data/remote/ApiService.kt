package com.trebit.reststudy.data.remote

import com.trebit.reststudy.data.model.*
import io.reactivex.Single
import retrofit2.http.*

/**
 * Rest_study
 * Class: ApiService
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

interface ApiService {


    /****************************************************************
     *
     *   Login, Sign Up APIs ...
     *
     ***************************************************************/
    @POST("user/sign_up")
    fun createUser(@Body body: CreateUserBody)
            : Single<ResponseVo>

    @GET("user/validateEmail/{email}")
    fun validateEmail(@Path("email") email: String)
            : Single<ValidateEmailVo>

    @POST("user/login")
    fun requestLogin(@Body body: RequestLoginBody)
            : Single<ResponseVo>



    /****************************************************************
     *
     *   Main APIs ...
     *
     ***************************************************************/
    @GET("user/getUser/{email}")
    fun getUser(@Path("email") email: String)
            : Single<UserVo>


    @PUT("user/updateUser/{email}")
    fun updateUser(@Path("email") email: String,
                   @Body body: UpdateUserBody) : Single<UserVo>


}
