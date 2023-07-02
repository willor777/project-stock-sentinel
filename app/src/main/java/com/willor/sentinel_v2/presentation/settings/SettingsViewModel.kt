package com.willor.sentinel_v2.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(

): ViewModel(){

    val tag = SettingsViewModel::class.java.simpleName.toString()
}