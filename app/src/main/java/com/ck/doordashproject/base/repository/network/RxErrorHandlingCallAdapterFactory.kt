package com.ck.doordashproject.base.repository.network

import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory private constructor(val or: RxJava2CallAdapterFactory): CallAdapter.Factory() {
    companion object {
        private val TAG = RxErrorHandlingCallAdapterFactory::class.java.name

        fun create(): CallAdapter.Factory {
            return RxErrorHandlingCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }
    }

    private val original = or

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val wrapped = original.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>
        return RxCallAdapterWrapper(retrofit, wrapped)
    }
}