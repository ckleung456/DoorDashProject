package com.ck.doordashproject.base.network

import com.google.gson.Gson
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

class RetrofitException private constructor(message: String?, url: String, response: Response<*>?, successType: Type?, kind: Kind,
                                             exception: Throwable?, retrofit: Retrofit?, gson: Gson): RuntimeException(message, exception) {
    companion object {
        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(exception.message, "", null, null, Kind.NETWORK, exception, null, Gson())
        }

        fun httpError(url: String, response: Response<*>, retrofit: Retrofit, successType: Type): RetrofitException {
            val message = response.code().toString() + " " + response.message()
            return RetrofitException(message, url, response, successType, Kind.HTTP, null, retrofit, Gson())
        }

        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(exception.message!!, "", null, null, Kind.UNEXPECTED, exception, null, Gson())
        }
    }

    /**
     * Identifies the event mKind which triggered a [RetrofitException].
     */
    enum class Kind {
        /**
         * An [IOException] occurred while communicating to the server.
         */
        NETWORK,
        /**
         * An exception was thrown while (de)serializing a body.
         */
        CONVERSION,
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    private val mResponse = response
    private val mGson = gson
    private val mSuccessType = successType
    private val mKind = kind

    private var errorBody: Any? = null


    /**
     * HTTP mResponse body converted to the type declared by either the interface method return type
     * or the generic type of the supplied [Callback] parameter. `null` if there is no
     * mResponse.
     *
     * @Note error body will be flushed out once it is being accessed one time
     *
     * @throws RuntimeException if unable to convert the body to the [success][.getSuccessType].
     */
    fun getBody(): Any? {
        if (errorBody == null) {
            val body = mResponse!!.errorBody() ?: return null
            errorBody = mGson.fromJson<Any>(body.charStream(), mSuccessType)
        }
        return errorBody
    }

    fun getKind(): Kind {
        return mKind
    }
}