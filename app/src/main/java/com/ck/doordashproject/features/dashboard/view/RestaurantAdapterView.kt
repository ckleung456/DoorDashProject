package com.ck.doordashproject.features.dashboard.view

import androidx.recyclerview.widget.DiffUtil

interface RestaurantAdapterView {
    fun updateContents(diffResult: DiffUtil.DiffResult)
}