package com.trebit.reststudy.data.repository

import com.trebit.reststudy.data.model.ContentVo
import com.trebit.reststudy.data.model.CreateBody
import com.trebit.reststudy.data.remote.ApiService
import io.reactivex.Single
import javax.inject.Singleton

/**
 * Rest_study
 * Class: DataRepository
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

@Singleton
class DataRepository(private val apiService: ApiService){

    fun getContents(): Single<List<ContentVo>> = apiService.getContents()

    fun createContents(title : String, writer: String): Single<ContentVo>
            = apiService.createContents(CreateBody(title, writer))
}