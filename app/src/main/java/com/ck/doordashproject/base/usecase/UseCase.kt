package com.ck.doordashproject.base.usecase

import com.ck.doordashproject.base.repository.network.RetrofitException
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.CoroutineDispatcher

interface UseCase<in INPUT, out RESULT> {
    operator fun invoke(
        input: INPUT,
        onResultFn: (RESULT) -> Unit = {}
    )
}

interface RxUseCase<in INPUT, out RESULT> {
    operator fun invoke(
        input: INPUT,
        onResultFn: (RESULT) -> Unit = {}
    ): Disposable
}

/**
 * Use this in worker in most of the case
 * Use other use cases if possible
 */
interface RxSingleUseCase<in INPUT, RESULT> {
    operator fun invoke(input: INPUT): Single<RESULT>
}

interface CoroutineUseCase<in INPUT, out RESULT> {
    suspend operator fun invoke(
        input: INPUT,
        onResultFn: (RESULT) -> Unit = {}
    )
}

abstract class FlowUseCase<in INPUT, out RESULT>(private val dispatcherMain: CoroutineDispatcher) {
    /**
     * Implement this in use case
     */
    protected abstract suspend fun getFlow(
        input: INPUT
    ): Flow<RESULT>

    /**
     * Implement this method if you need more logic to handle your error and results different RESULT type
     */
    protected open suspend fun errorResult(error: Throwable): RESULT? = null

    /**
     * Call this in view model
     */
    suspend operator fun invoke(
        input: INPUT,
        onResultFn: (UseCaseOutputWithStatus<RESULT>) -> Unit
    ) = getFlow(input)
        .onStart {
            onResultFn(UseCaseOutputWithStatus.Progress())
        }
        .onEach { result ->
            onResultFn(UseCaseOutputWithStatus.Success(result = result))
        }
        .catch { e ->
            onResultFn(UseCaseOutputWithStatus.Failed(
                error = if (e is RetrofitException) e else RetrofitException.unexpectedError(exception = e),
                failedResult = errorResult(error = e)
            ))
        }
        .flowOn(dispatcherMain)
        .collect()
}

sealed class UseCaseOutputWithStatus<out RESULT> {
    data class Progress<out RESULT>(val data: Any? = null): UseCaseOutputWithStatus<RESULT>()
    data class Success<out RESULT>(val result: RESULT): UseCaseOutputWithStatus<RESULT>()
    data class Failed<out RESULT>(val error: RetrofitException, val failedResult: RESULT? = null): UseCaseOutputWithStatus<RESULT>()
}