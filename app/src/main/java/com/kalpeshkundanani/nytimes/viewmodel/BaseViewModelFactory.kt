package com.kalpeshkundanani.nytimes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Base VM Factory for creation of different VM's
 *
 * Created by Kalpesh Kundanani on 16/11/20.
 */
class BaseViewModelFactory :
        ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            NewsViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Not configured") as Throwable
        }
    }
}