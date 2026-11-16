package com.example.impahla.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.impahla.data.local.ContributionEntity
import com.example.impahla.data.repository.ContributionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ContributionDetailViewModel(
    private val repository: ContributionRepository
) : ViewModel() {

    private val _contribution = MutableStateFlow<ContributionEntity?>(null)
    val contribution: StateFlow<ContributionEntity?> = _contribution.asStateFlow()

    fun loadContribution(id: Long) {
        viewModelScope.launch {
            repository.getContributionById(id).collectLatest {
                _contribution.value = it
            }
        }
    }

    fun markAsReceived() {
        val current = _contribution.value ?: return
        viewModelScope.launch {
            repository.markAsReceived(current)
        }
    }

    fun deleteContribution(onDeleted: () -> Unit) {
        val current = _contribution.value ?: return
        viewModelScope.launch {
            repository.deleteContribution(current)
            onDeleted()
        }
    }
}

class ContributionDetailViewModelFactory(
    private val repository: ContributionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContributionDetailViewModel::class.java)) {
            return ContributionDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
