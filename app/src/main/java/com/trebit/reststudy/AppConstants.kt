package com.trebit.reststudy

import android.Manifest
import android.os.Environment

/**
 * TrebitM-AOS
 * Class: AppConstants
 * Created by kangjonghyuk on 05/03/2019.
 *
 * Description:
 */

const val BASE_API_URL = "http://192.168.0.93:3000/"

const val RES_SUCCESS  = "200"
const val RES_FAILED   = "444"

const val VIEW_TYPE_GRID     = 0
const val VIEW_TYPE_VERTICAL = 1

const val DEFAULT_S   = ""
const val DEFAULT_B   = false
const val DEFAULT_I   = 0
const val LOGIN_EMAIL = "LOGIN_EMAIL"

const val INTENT_PROFILE_DATA      = "INTENT_PROFILE_DATA"
const val INTENT_PROFILE           = 2000
const val INTENT_PICTURE           = 2001
const val REQ_MULTIPLE_PERMISSIONS = 3000

const val FILE_AUTHORITY     = "com.trebit.reststudy.provider"
const val INTENT_CROP_ACTION = "com.android.camera.action.CROP"

const val PREF_EMAIL      = "PREF_EMAIL"
const val PREF_PW         = "PREF_PW"
const val PREF_AUTO_LOGIN = "PREF_AUTO_LOGIN"

const val KEY_VIEW_TYPE = "KEY_VIEW_TYPE"
const val KEY_EMAIL     = "KET_EMAIL"
const val KEY_QUERY     = "KEY_QUERY"

const val REQ_MULTI_CONTENTS  = "multi"
const val REQ_SINGLE_CONTENTS = "single"
const val REQ_MY_CONTENTS     = "mine"

val PERMISSIONS = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.CAMERA)

val TEMP_FORDER_PATH = "${Environment.getExternalStorageDirectory()}/Jstagram/"

