package com.test

import com.github.yeriomin.playstoreapi.OkHttpClientAdapter
import com.github.yeriomin.playstoreapi.PlayStoreApiBuilder
import com.github.yeriomin.playstoreapi.PropertiesDeviceInfoProvider
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*


// This program requires a properties file "credentials.properties" that contains
// email=[EMAIL]
// token=[TOKEN]

fun main() {
    val credentials = Properties()
    FileInputStream(File("credentials.properties")).use {
        credentials.load(it)
    }
    val email = credentials["email"] as String
    val token = credentials["token"] as String
    check(!email.isNullOrEmpty()) { "credentials.properties should have 'email' set" }
    check(!token.isNullOrEmpty()) { "credentials.properties should have 'token' set" }

    // A device definition is required to log in
    // See resources for a list of available devices
    val properties = Properties()
    try {
        properties.load(ClassLoader.getSystemResourceAsStream("device-honami.properties"))
    } catch (e: IOException) {
        println("device-honami.properties not found")
        return
    }
    val deviceInfoProvider = PropertiesDeviceInfoProvider()
    deviceInfoProvider.setProperties(properties)
    deviceInfoProvider.setLocaleString(Locale.ENGLISH.toString())

    // Provide valid google account info
    val builder = PlayStoreApiBuilder()
            .setHttpClient(OkHttpClientAdapter())
            .setDeviceInfoProvider(deviceInfoProvider)
            .setEmail(email)
            .setToken(token)
    val api = builder.build()

    println("Token was $token (now ${api.token})")
    println("gsfId is ${api.gsfId}")

    val response = api.details("com.cpuid.cpu_z")
    println("Response: $response")
}