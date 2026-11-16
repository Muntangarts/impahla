package com.example.impahla.ui.add_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impahla.model.ContributionStatus
import com.example.impahla.model.ContributionType
import com.example.impahla.ui.components.PhotoPickerSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContributionScreen(
    viewModel: AddEditViewModel,
    contributionId: Long?,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(contributionId) {
        if (contributionId != null && contributionId > 0) {
            viewModel.loadContribution(contributionId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.isEditMode) "Edit Contribution" else "Add New Contribution",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            // Contributor Name
            OutlinedTextField(
                value = uiState.contributorName,
                onValueChange = { viewModel.onNameChanged(it) },
                label = { Text("Contributor Name *") },
                isError = uiState.nameError != null,
                supportingText = uiState.nameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Contribution Type Segmented Row
            Text(
                text = "Contribution Type",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                ContributionType.entries.forEachIndexed { index, type ->
                    SegmentedButton(
                        selected = uiState.type == type,
                        onClick = { viewModel.onTypeChanged(type) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = ContributionType.entries.size)
                    ) {
                        Text(type.name)
                    }
                }
            }

            // Cash Amount Field (If CASH or BOTH)
            if (uiState.type == ContributionType.CASH || uiState.type == ContributionType.BOTH) {
                OutlinedTextField(
                    value = uiState.cashAmount,
                    onValueChange = { viewModel.onCashAmountChanged(it) },
                    label = { Text("Cash Amount (ZMW) *") },
                    isError = uiState.amountError != null,
                    supportingText = uiState.amountError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    prefix = { Text("ZMW ") },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Gift Description Field (If GIFT or BOTH)
            if (uiState.type == ContributionType.GIFT || uiState.type == ContributionType.BOTH) {
                OutlinedTextField(
                    value = uiState.giftDescription,
                    onValueChange = { viewModel.onGiftDescriptionChanged(it) },
                    label = { Text("Physical Gift Description *") },
                    isError = uiState.giftError != null,
                    supportingText = uiState.giftError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Dinner set, Stand fan, Microwave...") },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Status Selector
            Text(
                text = "Fulfillment Status",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = uiState.status == ContributionStatus.RECEIVED,
                    onClick = { viewModel.onStatusChanged(ContributionStatus.RECEIVED) },
                    label = { Text("RECEIVED (Handed over)") },
                    leadingIcon = if (uiState.status == ContributionStatus.RECEIVED) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )

                FilterChip(
                    selected = uiState.status == ContributionStatus.PROMISED,
                    onClick = { viewModel.onStatusChanged(ContributionStatus.PROMISED) },
                    label = { Text("PROMISED (Pledged)") },
                    leadingIcon = if (uiState.status == ContributionStatus.PROMISED) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )
            }

            // Photo Capture / Picker Section
            PhotoPickerSection(
                currentPhotoPath = uiState.photoPath,
                onPhotoPathChanged = { viewModel.onPhotoPathChanged(it) }
            )

            // Additional Notes Field
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = { viewModel.onNotesChanged(it) },
                label = { Text("Additional Notes (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Save Button
            Button(
                onClick = {
                    viewModel.saveContribution(onSuccess = onNavigateBack)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = if (uiState.isEditMode) "Update Contribution Record" else "Save Contribution Record",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
