package com.eosd.estudio_ancora.libs

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/**
 * Instância centralizada do Firebase Authentication.
 */
val auth get() = Firebase.auth
