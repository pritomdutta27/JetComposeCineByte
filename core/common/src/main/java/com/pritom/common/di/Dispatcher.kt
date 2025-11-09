package com.pritom.common.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val niaDispatcher: CineByteDispatchers)


enum class CineByteDispatchers {
    Default,
    IO,
}