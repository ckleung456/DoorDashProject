package com.ck.doordashproject.base.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ck.doordashproject.base.models.AppBaseViewModel
import com.ck.doordashproject.base.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    internal val appBaseViewModel: AppBaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appBaseViewModel.appNotificationLiveData.observeEvent(lifecycleOwner = this) {
            it.resId?.let { resId ->
                Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
            } ?: kotlin.run {
                it.msg?.let { msg ->
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}