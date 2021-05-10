package com.ck.doordashproject.base.utils

import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ImageUtils @Inject constructor(
    private val picasso: Picasso
){
    fun loadLogo(imageUrl: String, target: Target) {
        if (imageUrl.isBlank()) {
            return
        }
        picasso
                .load(imageUrl)
                .into(target)
    }
}