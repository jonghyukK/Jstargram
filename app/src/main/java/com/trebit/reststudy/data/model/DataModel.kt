package com.trebit.reststudy.data.model

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

