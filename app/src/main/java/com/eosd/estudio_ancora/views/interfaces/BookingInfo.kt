package com.eosd.estudio_ancora.views.interfaces

import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import java.time.LocalDateTime

interface BookingInfo {
    val dateTime: LocalDateTime;
    val customer: Customer;
    val service: Service;
}