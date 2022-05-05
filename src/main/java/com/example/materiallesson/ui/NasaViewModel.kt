package com.example.materiallesson.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.materiallesson.domain.NasaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class NasaViewModel(private val repository: NasaRepository): ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading:Flow<Boolean> = _loading

    private val _image: MutableStateFlow<String?> = MutableStateFlow(null)
    val image: Flow<String?> = _image

    private val _titleImg: MutableStateFlow<String?> = MutableStateFlow(null)
    val titleImg: Flow<String?> = _titleImg

    private val _error: MutableSharedFlow<String> = MutableSharedFlow()
    val error: Flow<String> = _error

    fun requestPicture() {
        _loading.value = true

        viewModelScope.launch {

            try {
                val nasaResponse = repository.getNasaPicture()
                _image.emit(nasaResponse.url)
                _titleImg.emit(nasaResponse.title)
            } catch (e:IOException) {
                _error.emit(e.message.toString())
            }

            _loading.emit(false)
        }
    }

}

class NasaViewModelFactory(private val repository: NasaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = NasaViewModel(repository) as T
}