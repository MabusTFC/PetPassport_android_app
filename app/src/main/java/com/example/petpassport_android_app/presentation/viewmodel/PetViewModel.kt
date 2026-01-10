package com.example.petpassport_android_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.petpassport_android_app.domain.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val repository: PetRepository
) : ViewModel() {

}