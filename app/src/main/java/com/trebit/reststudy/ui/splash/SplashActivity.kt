package com.trebit.reststudy.ui.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.orhanobut.logger.Logger
import com.trebit.reststudy.LOGIN_EMAIL
import com.trebit.reststudy.PREF_CHECKED_AUTO_LOGIN
import com.trebit.reststudy.PREF_EMAIL
import com.trebit.reststudy.ui.BaseActivity
import com.trebit.reststudy.ui.login.activity.LoginActivity
import com.trebit.reststudy.ui.main.activity.MainActivity
import com.trebit.reststudy.utils.SharedPref
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS

/**
 * Jstargram
 * Class: SpalshActivity
 * Created by kangjonghyuk on 09/04/2019.
 *
 * Description:
 */

class SplashActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashTimerSet()
    }

    private fun splashTimerSet() {
        Observable.timer(1, TimeUnit.SECONDS)
            .map { checkAutoLogin() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun checkAutoLogin() {

        // 자동 로그인 (o)
        if ( mPref.isAutoLogin(PREF_CHECKED_AUTO_LOGIN)) {
            val loginEmail = mPref.getPrefEmail(PREF_EMAIL)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(LOGIN_EMAIL, loginEmail)
            startActivity(intent)
            finish()
        } else {
            // 자동 로그인 (x)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}