package com.trebit.reststudy.di.module

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * TrebitM-AOS
 * Class: ViewModelKey
 * Created by kangjonghyuk on 05/03/2019.
 *
 * Description:
 */

@Target(AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)