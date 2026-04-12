package com.eosd.estudio_ancora.services

import android.util.Log
import com.eosd.estudio_ancora.libs.auth
import kotlinx.coroutines.tasks.await

/**
 * Serviço responsável por garantir que o usuário esteja autenticado.
 */
object AuthService {
    private const val TAG = "AuthService"

    /**
     * Garante que o usuário esteja logado (anônimo).
     * Em ambiente de Dev (onde o Auth pode estar desativado), o erro é logado mas o app não trava.
     * 
     * @return O UID do usuário autenticado ou null se houver falha.
     */
    suspend fun ensureAuthenticated(): String? {
        return try {
            val currentUser = auth.currentUser
            
            if (currentUser != null) {
                Log.d(TAG, "Usuário já autenticado: ${currentUser.uid}")
                return currentUser.uid
            }

            Log.d(TAG, "Tentando login anônimo...")
            val result = auth.signInAnonymously().await()
            val uid = result.user?.uid
            
            Log.d(TAG, "Login anônimo realizado com sucesso. UID: $uid")
            uid
        } catch (e: Exception) {
            // Logamos o erro mas permitimos que o app continue (importante para o ambiente de Dev)
            Log.e(TAG, "Falha na autenticação anônima. Verifique se o provedor está ativado no Firebase Console.", e)
            null
        }
    }
}
