package com.ck.doordashproject.base.usecase

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.rx2.awaitFirst

interface UseCase<in INPUT, out RESULT> {
    operator fun invoke(input: INPUT): RESULT
}

interface CoroutineUseCase<INPUT, RESULT> {
    suspend operator fun invoke(input: INPUT, dispatchersContext: DispatchersContract = CoroutineDispatchers): RESULT
}

interface SingleUseCase<INPUT, RESULT>: CoroutineUseCase<INPUT, RESULT> {
    fun getSingle(input: INPUT): Single<RESULT>

    override suspend fun invoke(input: INPUT, dispatchersContext: DispatchersContract): RESULT = getSingle(input).await()
}

interface ObservableUseCase<INPUT, RESULT>: CoroutineUseCase<INPUT, RESULT> {
    fun getObservable(input: INPUT): Observable<RESULT>

    override suspend fun invoke(input: INPUT, dispatchersContext: DispatchersContract): RESULT = getObservable(input).awaitFirst()
}

interface FlowableUseCase<INPUT, RESULT>: CoroutineUseCase<INPUT, RESULT> {
    fun getFlowable(input: INPUT): Flowable<RESULT>

    override suspend fun invoke(input: INPUT, dispatchersContext: DispatchersContract): RESULT = getFlowable(input).awaitFirst()
}


interface DispatchersContract {
    val IO: CoroutineDispatcher
    val MAIN: CoroutineDispatcher
    val DEFAULT: CoroutineDispatcher
}

object CoroutineDispatchers: DispatchersContract {
    override val IO = Dispatchers.IO
    override val MAIN = Dispatchers.Main
    override val DEFAULT = Dispatchers.Default
}