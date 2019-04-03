package com.trebit.reststudy.data.model

/**
 * Rest_study
 * Class: DataModel
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

data class ContentVo(val id        : Int,
                     val title     : String,
                     val writer    : String)

data class CreateBody(val title: String,
                      val writer: String)