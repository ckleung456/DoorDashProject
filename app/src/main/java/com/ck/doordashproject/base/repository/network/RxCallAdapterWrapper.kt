package com.ck.doordashproject.base.repository.network

import io.reactivex.*
import io.reactivex.functions.Function
import retrofit2.*
import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RxCallAdapterWrapper constructor(private val retrofit: Retrofit, private val wrapped: CallAdapter<Any, Any>?) :
        CallAdapter<Any, Any> {
    override fun adapt(call: Call<Any>): Any {
        val adaptedCall = wrapped!!.adapt(call)
        val responseType = wrapped.responseType()
        if (adaptedCall is Completable) {
            return adaptedCall.onErrorResumeNext(Function { throwable -> Completable.error(asRetrofitException(throwable, responseType)) })
        } else if (adaptedCall is Single<*>) {
            return adaptedCall.doOnSuccess { o -> checkIsSuccessResponse(o) }
                    .onErrorResumeNext { throwable -> Single.error(asRetrofitException(throwable, responseType)) }
        } else if (adaptedCall is Observable<*>) {
            return adaptedCall.doOnNext { o -> checkIsSuccessResponse(o) }
                    .onErrorResumeNext(Function { throwable -> Observable.error(asRetrofitException(throwable, responseType)) })
        } else if (adaptedCall is Flowable<*>) {
            return adaptedCall
                    .doOnNext { o -> checkIsSuccessResponse(o) }
                    .onErrorResumeNext(Function { throwable -> Flowable.error(asRetrofitException(throwable, responseType)) })
        } else if (adaptedCall is Maybe<*>) {
            return adaptedCall
                    .doOnSuccess { o -> checkIsSuccessResponse(o) }
                    .onErrorResumeNext(Function { throwable -> Maybe.error(asRetrofitException(throwable, responseType)) })
        }
        throw RuntimeException("Observable Type not supported")
    }

    override fun responseType(): Type {
        return wrapped!!.responseType()
    }

    private fun asRetrofitException(throwable: Throwable, type: Type): RetrofitException {
        if (throwable is HttpException) {
            val response = throwable.response()
            return response?.let {
                RetrofitException.httpError(response.raw().request().url().toString(),
                    it, retrofit, type)
            } ?: kotlin.run {
                RetrofitException.unexpectedError(throwable)
            }
        }
        if (throwable is ConnectException || throwable is SocketTimeoutException || throwable is UnknownHostException) {
            val ioException = IOException("Network Issues")
            return RetrofitException.networkError(ioException)
        }
        if (throwable is IOException) {
            return RetrofitException.networkError(throwable)
        }
        return RetrofitException.unexpectedError(throwable)
    }

    private fun checkIsSuccessResponse(o: Any) {
        if (o is Response<*>) {
            if (!o.isSuccessful) {
                throw HttpException(o)
            }
        }
    }
}