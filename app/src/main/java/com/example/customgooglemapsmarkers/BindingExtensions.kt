package com.example.customgooglemapsmarkers

import android.view.View
import androidx.databinding.BindingAdapter

class BindingExtensions {
    companion object {
        @JvmStatic
        @BindingAdapter("is_visible")
        fun setVisibility(view: View, visible: Boolean) {
            view.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }
}