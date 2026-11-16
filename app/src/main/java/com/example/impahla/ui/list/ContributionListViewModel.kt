package com.example.impahla.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.impahla.data.local.ContributionEntity
import com.example.impahla.data.repository.ContributionRepository
import com.example.impahla.model.ContributionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class FilterTab {
    ALL,
    PROMISED,
    RECEIVED
}

data class ListUiState(
    val selectedTab: FilterTab = FilterTab.ALL,
    val searchQuery: String = "",
    val contributions: List<ContributionEntity> = emptyList()
)

class ContributionListViewModel(
    private val repository: ContributionRepository
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(FilterTab.ALL)
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<ListUiState> = combine(
        _selectedTab,
        _searchQuery,
        repository.allContributions
    ) { tab, query, allList ->
        val filteredByTab = when (tab) {
            FilterTab.ALL -> allList
            FilterTab.PROMISED -> allList.filter { it.status == ContributionStatus.PROMISED }
            FilterTab.RECEIVED -> allList.filter { it.status == ContributionStatus.RECEIVED }
        }

        val finalFiltered = if (query.isBlank()) {
            filteredByTab
        } else {
            filteredByTab.filter {
                it.contributorName.contains(query, ignoreCase = true) ||
                        it.giftDescription.contains(query, ignoreCase = true) ||
                        (it.notes?.contains(query, ignoreCase = true) == true)
            }
        }

        ListUiState(
            selectedTab = tab,
            searchQuery = query,
            contributions = finalFiltered
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ListUiState()
    )

    fun onTabSelected(tab: FilterTab) {
        _selectedTab.value = tab
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}

class ContributionListViewModelFactory(
    private val repository: ContributionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContributionListViewModel::class.java)) {
            return ContributionListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
