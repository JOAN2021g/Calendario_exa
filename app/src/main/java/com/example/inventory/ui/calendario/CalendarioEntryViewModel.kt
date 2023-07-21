/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.calendario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Calendario
import com.example.inventory.data.CalendariosRepository

/**
 * ViewModel to validate and insert items in the Room database.
 */
class ItemEntryViewModel
    (private val itemsRepository: CalendariosRepository) :
    ViewModel() {

    /**
     * Holds current calendario ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(calendarioDetails: CalendarioDetails) {
        itemUiState =
            ItemUiState(calendarioDetails = calendarioDetails, isEntryValid = validateInput(calendarioDetails))
    }

    /**
     * Inserts an [calendario] in the Room database
     */
    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertCalendario(itemUiState.calendarioDetails.toItem())
        }
    }

    private fun validateInput(uiState: CalendarioDetails = itemUiState.calendarioDetails): Boolean {
        return with(uiState) {
            mes.isNotBlank() && dias.isNotBlank() && semanas.isNotBlank() && festividad.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Calendario.
 */
data class ItemUiState(
    val calendarioDetails: CalendarioDetails = CalendarioDetails(),
    val isEntryValid: Boolean = false
)

data class CalendarioDetails(
    val id: Int = 0,
    val mes: String = "",
    val dias: String = "",
    val semanas: String = "",
    val festividad: String = "",
)

/**
 * Extension function to convert [ItemUiState] to [Calendario]. If the value of [CalendarioDetails.semanas] is
 * not a valid [int], then the address will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun CalendarioDetails.toItem(): Calendario = Calendario(
    id = id,
    mes = mes,
    dias = dias.toIntOrNull() ?: 0,
    semanas = semanas.toIntOrNull() ?: 0,
    festividad = festividad
)




/**
 * Extension function to convert [Calendario] to [ItemUiState]
 */
fun Calendario.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    calendarioDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Calendario] to [CalendarioDetails]
 */
fun Calendario.toItemDetails(): CalendarioDetails = CalendarioDetails(
    id = id,
    mes = mes,
    dias = dias.toString(),
    semanas = semanas.toString(),
    festividad = festividad
)
