package com.trebit.reststudy.data.repository

import com.trebit.reststudy.data.model.RequestLoginBody
import com.trebit.reststudy.data.model.CreateUserBody
import com.trebit.reststudy.data.remote.ApiService
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

    fun createUser(email    : String,
                   name     : String,
                   password : String)
            = apiService.createUser(CreateUserBody(email, name, password))

    fun validateEmail(email: String)
            = apiService.validateEmail(email)

    fun requestLogin(email    : String,
                     password : String)
            = apiService.requestLogin(RequestLoginBody(email, password))
}