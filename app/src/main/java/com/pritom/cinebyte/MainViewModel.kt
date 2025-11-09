package com.pritom.cinebyte

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.usecase.GetAllMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllMoviesUseCase: GetAllMoviesUseCase
): ViewModel() {

    private val _moviesByCategory = MutableStateFlow<Map<MovieCategory, List<Movie>>>(emptyMap())
    val moviesByCategory: StateFlow<Map<MovieCategory, List<Movie>>> = _moviesByCategory

    fun loadAllMovies() {
        viewModelScope.launch {
            MovieCategory.entries.forEach { category ->
                getAllMoviesUseCase.invoke(category)
                    .catch { emit(emptyList()) }
                    .collect { movies ->
                        _moviesByCategory.update { current ->
                            current + (category to movies)
                        }
                    }
            }
        }
    }



}