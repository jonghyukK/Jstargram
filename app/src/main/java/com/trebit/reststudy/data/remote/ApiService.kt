package com.trebit.reststudy.data.remote

import com.trebit.reststudy.data.model.ContentVo
import com.trebit.reststudy.data.model.CreateBody
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Rest_study
 * Class: ApiService
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

interface ApiService {

    @GET("show")
    fun getContents(): Single<List<ContentVo>>

    @POST("create")
    fun createContents(@Body body: CreateBody) : Single<ContentVo>
}
