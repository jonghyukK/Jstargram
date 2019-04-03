package com.trebit.reststudy.ui.main

import android.arch.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.trebit.reststudy.data.remote.ApiService
import com.trebit.reststudy.data.repository.DataRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Rest_study
 * Class: MainViewModel
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

class MainViewModel @Inject constructor(
    apiService: ApiService
): ViewModel(){

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val repository by lazy { DataRepository(apiService) }

    fun getAllContents(){

        compositeDisposable.add(
            repository.getContents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {

                        for(i in 0 until it.size) {
                            Logger.d("""
                                id     : ${it[i].id}
                                title  : ${it[i].title}
                                writer : ${it[i].writer}
                            """.trimIndent())
                        }
                    },
                    {
                        Logger.e(it.message.toString())
                    }
                ))
    }

    fun createContents(){

        compositeDisposable.add(
            repository.createContents("Hello", "World")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Logger.d(it)
                    },
                    {
                        Logger.e(it.message.toString())
                    }
                ))
    }
}