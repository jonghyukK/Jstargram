package com.trebit.reststudy.data.model

import java.io.Serializable

/**
 * Rest_study
 * Class: DataModel
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */


/****************************************************************
 *
 *   Request Body.
 *
 ***************************************************************/

// Create User
data class CreateUserBody(
    val email    : String,
    val name     : String,
    val password : String
)

// Login
data class RequestLoginBody(
    val email    : String,
    val password : String
)

// Update User
data class UpdateUserBody(
    val name        : String,
    val introduce   : String?,
    val profile_img : String?
)






/****************************************************************
 *
 *   Response Vo.
 *
 ***************************************************************/
data class ResponseVo(
    val resCode: String,
    val resMsg : String
)

// Validate Email
data class ValidateEmailVo(
    val resCode    : String,
    val resMsg     : String,
    val isValidate : String
)

// Get User
data class UserVo(
    val resCode       : String,
    val resMsg        : String,
    val email         : String,
    var name          : String,
    var introduce     : String,
    var profile_img   : String,
    val contents_cnt  : Int,
    val follower_cnt  : Int,
    val following_cnt : Int
): Serializable




/****************************************************************
 *
 *   Adapters Model
 *
 ***************************************************************/
data class GalleryItems(val path: String)

data class ContentItems(val contentId       : String,
                        val writer_profile  : String,
                        val writer_email    : String,
                        val image_Path      : String,
                        val content_text    : String,
                        val favorite_cnt    : String)

data class ContentItem(val contents_id    : String,
                       val writer_profile : String,
                       val writer         : String,
                       val image_path     : String,
                       val content        : String,
                       val favorite_cnt   : String,
                       val comment_cnt    : String)
