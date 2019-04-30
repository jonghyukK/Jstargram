package com.trebit.reststudy.data.remote

import com.trebit.reststudy.data.model.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
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
    // Create User.
    @POST("user/sign_up")
    fun createUser(@Body body: CreateUserBody)
            : Single<ResponseVo>


    // Check Email Validation.
    @GET("user/validateEmail/{email}")
    fun validateEmail(@Path("email") email: String)
            : Single<ResponseVo>

    // Check Login Validation.
    @POST("user/login")
    fun requestLogin(@Body body: RequestLoginBody)
            : Single<ResponseVo>




    /****************************************************************
     *
     *   Main APIs ...
     *
     ***************************************************************/

    // Get User Information by Email.
    @GET("user/getUser/{email}")
    fun getUser(@Path("email") email: String)
            : Single<UserVo>


    // Update User without Profile.
    @PUT("user/updateUser/{email}")
    fun updateUser(@Path("email") email: String,
                   @Body body: UpdateUserBody) : Single<UserVo>


    // Update User with Profile.
    @Multipart
    @POST("user/updateUser")
    fun updateUserWithProfile(
        @Part("name")      name     : RequestBody,
        @Part("introduce") introduce: RequestBody,
        @Part("email")     email    : RequestBody,
        @Part file: MultipartBody.Part
    ): Single<UserVo>


    @Multipart
    @POST("upload/uploadContent")
    fun uploadContent(
        @Part("content") content : RequestBody,
        @Part("writer")   writer : RequestBody,
        @Part file: MultipartBody.Part
    ): Single<ResponseVo>

    @GET("upload/getContents")
    fun getContents(): Single<List<ContentItem>>

}
