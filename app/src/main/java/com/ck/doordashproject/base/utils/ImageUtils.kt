package com.ck.doordashproject.base.utils

import android.text.TextUtils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ImageUtils @Inject constructor(){
    fun loadLogo(imageUrl: String, target: Target) {
        if (TextUtils.isEmpty(imageUrl)) {
            return
        }
        Picasso.get()
                .load(imageUrl)
                .into(target)
    }
}