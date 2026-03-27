package com.eosd.estudio_ancora.services.utils

import com.eosd.estudio_ancora.libs.firestore

fun generateFirestoreId(): String {
    return firestore.collection("none").document().id
}