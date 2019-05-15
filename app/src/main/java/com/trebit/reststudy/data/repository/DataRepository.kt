package com.trebit.reststudy.data.repository

import com.trebit.reststudy.data.model.CreateUserBody
import com.trebit.reststudy.data.model.RequestLoginBody
import com.trebit.reststudy.data.model.UpdateUserBody
import com.trebit.reststudy.data.remote.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


    /****************************************************************
     *
     *   Login APIs..
     *
     ***************************************************************/
    fun validateEmail(email: String)
            = apiService.validateEmail(email)

    fun requestLogin(email    : String,
                     password : String)
            = apiService.requestLogin(RequestLoginBody(email, password))





    /****************************************************************
     *
     *   SignUp APIs..
     *
     ***************************************************************/
    fun createUser(email    : String,
                   name     : String,
                   password : String)
            = apiService.createUser(CreateUserBody(email, name, password))






    /****************************************************************
     *
     *   about User APIs...
     *
     ***************************************************************/
    fun getUser(email: String)
            = apiService.getUser(email)

    fun updateUser(email      : String,
                   name       : String,
                   introduce  : String?,
                   profile_img: String?)
            = apiService.updateUser(email, UpdateUserBody(name, introduce, profile_img))


    fun updateUserWithProfile(name     : RequestBody,
                              introduce: RequestBody,
                              email    : RequestBody,
                              file     : MultipartBody.Part)
            = apiService.updateUserWithProfile(name, introduce, email, file)




    /****************************************************************
     *
     *   Contetns APIs ...
     *
     ***************************************************************/
    fun uploadContent(content : RequestBody,
                      writer  : RequestBody,
                      file    : MultipartBody.Part)
     = apiService.uploadContent(content, writer, file)

    fun getContents(dataType : String,
                    email    : String? = null) = apiService.getContents(dataType, email)

    fun deleteContent(contentId: String) = apiService.deleteContent(contentId)



}