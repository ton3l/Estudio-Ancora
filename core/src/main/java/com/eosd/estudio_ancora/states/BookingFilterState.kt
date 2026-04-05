package com.eosd.estudio_ancora.states

import com.eosd.estudio_ancora.domain.Service
import java.time.LocalDate
import java.time.LocalTime

data class BookingFilterState(
    val customerName: String = "",
    val service: Service? = null,
    val date: LocalDate? = null,
    val time: String = ""
)
