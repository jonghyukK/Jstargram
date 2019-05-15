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
// Default Response Vo.
data class ResponseVo(
    val resCode: String,
    val resMsg : String
)


// User Info Vo.
data class UserVo(
    val resCode       : String,
    val resMsg        : String,
    val email         : String,
    val name          : String,
    val introduce     : String,
    val profile_img   : String,
    val contents_cnt  : Int,
    val follower_cnt  : Int,
    val following_cnt : Int
): Serializable

data class DeleteContentsVo(
    val resCode     : String,
    val resMsg      : String,
    val contentsCnt : Int
)



/****************************************************************
 *
 *   Adapters Model
 *
 ***************************************************************/
// Local Picture Model.
data class GalleryItems(val path: String)

// Contents Model.
data class ContentItem(val contents_id    : String,
                       val writer_profile : String,
                       val writer         : String,
                       val image_path     : String,
                       val content        : String,
                       val favorite_cnt   : Int,
                       val comment_cnt    : Int,
                       val createdAt      : String)
