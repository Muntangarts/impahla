package com.example.impahla.ui.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.impahla.data.local.ContributionEntity
import com.example.impahla.data.repository.ContributionRepository
import com.example.impahla.model.ContributionStatus
import com.example.impahla.model.ContributionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditUiState(
    val id: Long = 0,
    val contributorName: String = "",
    val type: ContributionType = ContributionType.CASH,
    val cashAmount: String = "",
    val giftDescription: String = "",
    val status: ContributionStatus = ContributionStatus.RECEIVED,
    val photoPath: String? = null,
    val notes: String = "",
    val nameError: String? = null,
    val amountError: String? = null,
    val giftError: String? = null,
    val isEditMode: Boolean = false,
    val isSaved: Boolean = false
)

class AddEditViewModel(
    private val repository: ContributionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    fun loadContribution(id: Long) {
        if (id <= 0) return
        viewModelScope.launch {
            val existing = repository.getContributionById(id).firstOrNull()
            if (existing != null) {
                _uiState.update {
                    it.copy(
                        id = existing.id,
                        contributorName = existing.contributorName,
                        type = existing.type,
                        cashAmount = if (existing.cashAmount > 0) existing.cashAmount.toString() else "",
                        giftDescription = existing.giftDescription,
                        status = existing.status,
                        photoPath = existing.photoPath,
                        notes = existing.notes ?: "",
                        isEditMode = true
                    )
                }
            }
        }
    }

    fun onNameChanged(name: String) {
        val filtered = name.filter { it.isLetter() || it.isWhitespace() || it == '-' || it == '\'' || it == '.' }
            .replace(Regex("\\s+"), " ")
        _uiState.update { it.copy(contributorName = filtered, nameError = null) }
    }

    fun onTypeChanged(type: ContributionType) {
        _uiState.update { it.copy(type = type, amountError = null, giftError = null) }
    }

    fun onCashAmountChanged(amount: String) {
        _uiState.update { it.copy(cashAmount = amount, amountError = null) }
    }

    fun onGiftDescriptionChanged(description: String) {
        _uiState.update { it.copy(giftDescription = description, giftError = null) }
    }

    fun onStatusChanged(status: ContributionStatus) {
        _uiState.update { it.copy(status = status) }
    }

    fun onPhotoPathChanged(path: String?) {
        _uiState.update { it.copy(photoPath = path) }
    }

    fun onNotesChanged(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun saveContribution(onSuccess: () -> Unit) {
        val state = _uiState.value

        var hasError = false
        var nameErr: String? = null
        var amountErr: String? = null
        var giftErr: String? = null

        val trimmedName = state.contributorName.trim()
        if (trimmedName.isEmpty()) {
            nameErr = "Contributor name is required"
            hasError = true
        } else if (trimmedName.length < 2) {
            nameErr = "Name must be at least 2 characters long"
            hasError = true
        } else if (!trimmedName.any { it.isLetter() }) {
            nameErr = "Name must contain letters"
            hasError = true
        } else {
            val nameRegex = "^[\\p{L}\\s\\-'.]+$".toRegex()
            if (!trimmedName.matches(nameRegex)) {
                nameErr = "Name can only contain letters, spaces, hyphens, and apostrophes"
                hasError = true
            }
        }

        var parsedCash = 0.0
        if (state.type == ContributionType.CASH || state.type == ContributionType.BOTH) {
            val amountStr = state.cashAmount.trim()
            if (amountStr.isEmpty()) {
                amountErr = "Cash amount is required"
                hasError = true
            } else {
                val parsed = amountStr.toDoubleOrNull()
                if (parsed == null || parsed < 0) {
                    amountErr = "Enter a valid positive cash amount"
                    hasError = true
                } else {
                    parsedCash = parsed
                }
            }
        }

        if (state.type == ContributionType.GIFT || state.type == ContributionType.BOTH) {
            if (state.giftDescription.trim().isEmpty()) {
                giftErr = "Gift description is required"
                hasError = true
            }
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    nameError = nameErr,
                    amountError = amountErr,
                    giftError = giftErr
                )
            }
            return
        }

        viewModelScope.launch {
            val entity = ContributionEntity(
                id = state.id,
                contributorName = state.contributorName.trim(),
                type = state.type,
                cashAmount = parsedCash,
                giftDescription = state.giftDescription.trim(),
                status = state.status,
                photoPath = state.photoPath,
                notes = state.notes.trim().ifEmpty { null }
            )

            if (state.isEditMode) {
                repository.updateContribution(entity)
            } else {
                repository.insertContribution(entity)
            }

            _uiState.update { it.copy(isSaved = true) }
            onSuccess()
        }
    }
}

class AddEditViewModelFactory(
    private val repository: ContributionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditViewModel::class.java)) {
            return AddEditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
