package com.eosd.estudio_ancora.services

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.models.day.DayModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class DayWorkflowTest {

    @Test
    fun getAvailableTimesFromSeededData() = runBlocking {
        // Cenário: Uma segunda-feira que sabemos que existe no banco (pelo Seeder)
        val date = LocalDate.of(2026, 3, 9) // Uma segunda-feira (Monday)
        
        // Ação: Chamar o serviço
        val availableTimes = DayService.getDayAvailableTimes(date)
        
        // Verificação: O resultado não deve ser nulo e deve conter horários (já que o Seeder cria 13 horários)
        assertNotNull("Os horários disponíveis não devem ser nulos", availableTimes)
        assertTrue("Deve retornar uma lista de horários para um dia aberto", availableTimes.isNotEmpty())
        
        println("=== TESTE SUCESSO: Foram encontrados ${availableTimes.size} horários para $date ===")
        availableTimes.forEach { println("Horário: $it") }
    }
}
