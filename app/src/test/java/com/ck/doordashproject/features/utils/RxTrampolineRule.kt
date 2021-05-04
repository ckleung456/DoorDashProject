package com.ck.doordashproject.features.utils

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxTrampolineRule : TestRule {
    override fun apply(base: Statement, description: Description?): Statement = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

            try {
                base.evaluate()
            } finally {
                RxAndroidPlugins.reset()
                RxJavaPlugins.reset()
            }
        }
    }
}