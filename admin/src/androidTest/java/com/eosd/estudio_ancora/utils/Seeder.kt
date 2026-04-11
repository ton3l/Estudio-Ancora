package com.eosd.estudio_ancora.utils

/**
 * Annotation used to mark tests that are actually Seeders (database populators).
 * These tests are excluded from the default global test run to prevent accidental
 * data modification or performance issues.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Seeder
