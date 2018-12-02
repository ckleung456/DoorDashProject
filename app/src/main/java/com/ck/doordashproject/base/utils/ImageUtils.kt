package com.ck.doordashproject.base.utils

import android.text.TextUtils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class ImageUtils private constructor(){
    object Holder {
        val obj = ImageUtils()
    }
    companion object {
        val INSTANCE: ImageUtils by lazy { Holder.obj }
    }

    fun loadLogo(imageUrl: String, target: Target) {
        if (TextUtils.isEmpty(imageUrl)) {
            return
        }
        Picasso.get()
                .load(imageUrl)
                .into(target)
    }
}