package com.willor.sentinel_v2.presentation.analysts_rating_changes

import android.util.Log
import androidx.lifecycle.ViewModel
import com.willor.lib_data.domain.usecases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AnalystsChangesViewModel @Inject constructor(
    private val usecases: UseCases
): ViewModel(){

    private val tag: String = AnalystsChangesViewModel::class.java.simpleName


    init {
        Log.d(tag, "Initialized")
    }
}