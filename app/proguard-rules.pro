# Regras para evitar que o Firebase/Firestore falhe ao mapear dados (Reflexão)
# Não ofuscar classes de Domínio e Modelos, pois seus nomes de campo devem bater com o Firestore
-keep class com.eosd.estudio_ancora.domain.** { *; }
-keep class com.eosd.estudio_ancora.models.** { *; }

# Mantém os nomes dos campos originais para qualquer classe que use anotações do Firestore
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName <fields>;
    @com.google.firebase.firestore.IgnoreExtraProperties <fields>;
    @com.google.firebase.firestore.Exclude <fields>;
}
