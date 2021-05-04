package com.ck.doordashproject.base.models

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ck.doordashproject.base.utils.Event
import com.ck.doordashproject.base.utils.fireEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppBaseViewModel @Inject constructor(): ViewModel() {
    private val _appNotificationLiveData = MutableLiveData<Event<NotificationMessage>>()
    val appNotificationLiveData: LiveData<Event<NotificationMessage>> = _appNotificationLiveData

    fun setErrorNotification(@StringRes errorResId: Int?, errorMsg: String?) {
        _appNotificationLiveData.fireEvent(
            NotificationMessage(
                resId = errorResId,
                msg = errorMsg
            )
        )
    }
}

data class NotificationMessage(
    val resId: Int? = null,
    val msg: String? = null
)