package com.procurement.clarification.json

import com.procurement.access.json.exception.JsonFileNotFoundException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

typealias JSON = String

fun loadJson(fileName: String): JSON {
    return ClassPathResource.getFilePath(fileName)?.let { pathToFile ->
        val path = Paths.get(pathToFile)
        val buffer = Files.readAllBytes(path)
        String(buffer, Charset.defaultCharset())
    } ?: throw JsonFileNotFoundException("Error loading JSON. File by path: $fileName not found.")
}

private object ClassPathResource {
    fun getFilePath(fileName: String): String? = javaClass.classLoader.getResource(fileName)?.path
}
