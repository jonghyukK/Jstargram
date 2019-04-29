package com.trebit.reststudy.ui.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.orhanobut.logger.Logger
import com.trebit.reststudy.*
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
            .map { if(checkPermission()) checkAutoLogin() }
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


    // Check Permissions.
    private fun checkPermission(): Boolean {
        var result: Int
        val permissionList = ArrayList<String>()
        for (pm in PERMISSIONS) {
            result = ContextCompat.checkSelfPermission(this, pm)
            if (result != PackageManager.PERMISSION_GRANTED)
                permissionList.add(pm)
        }

        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), REQ_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }


    // Result for Permission Request.
    override fun onRequestPermissionsResult(requestCode  : Int,
                                            permissions  : Array<out String>,
                                            grantResults : IntArray) {
        when (requestCode) {
            REQ_MULTIPLE_PERMISSIONS -> {
                for ( i in 0 until permissions.size) {
                    when ( permissions[i]) {
                        PERMISSIONS[0] -> {
                            if ( grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                toast { getString(R.string.desc_permission)}
                                finish()
                                return
                            }
                        }
                        PERMISSIONS[1] -> {
                            if ( grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                toast { getString(R.string.desc_permission)}
                                finish()
                                return
                            }
                        }
                        PERMISSIONS[2] -> {
                            if ( grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                toast { getString(R.string.desc_permission)}
                                finish()
                                return
                            }
                        }
                    }
                }
                checkAutoLogin()
            }
        }
    }


}