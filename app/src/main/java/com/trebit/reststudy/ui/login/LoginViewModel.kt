package com.trebit.reststudy.ui.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.trebit.reststudy.data.remote.ApiService
import javax.inject.Inject

/**
 * Rest_study
 * Class: LoginViewModel
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

class LoginViewModel @Inject constructor(
    apiService: ApiService
): ViewModel() {

    val inputEmail : MutableLiveData<String> = MutableLiveData()
    val inputPw    : MutableLiveData<String> = MutableLiveData()
    val inputValue : MutableLiveData<String> = MutableLiveData()
    val inputName  : MutableLiveData<String> = MutableLiveData()


}