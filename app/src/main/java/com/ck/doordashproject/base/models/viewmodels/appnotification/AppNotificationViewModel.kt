package com.ck.doordashproject.base.models.viewmodels.appnotification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppNotificationViewModel: ViewModel() {
    private val appNotificationLiveData = MutableLiveData<Int>()

    fun observeErrorNotification(): MutableLiveData<Int> {
        return appNotificationLiveData
    }

    fun setErrorNotification(errorResId: Int) {
        appNotificationLiveData.postValue(errorResId)
    }
}