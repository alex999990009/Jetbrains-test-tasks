package wordsearcher

import org.springframework.stereotype.Service
import java.io.File

@Service
class Service {
    private val workPath = System.getProperty(VARIABLE_WORK_DIR)

    fun getTextsByWords(words: List<String>): List<String> {
        val listOfFiles = File(workPath).listFiles()?.asList() ?: return emptyList()
        return listOfFiles.filter { file ->
            file.bufferedReader().use {
                val text = it.readText()
                words.forEach { word ->
                    if (text.contains(word)) {
                        return@use true
                    }
                }
                return@use false
            }
        }.map { it.name }
    }

    companion object {
        const val VARIABLE_WORK_DIR = "texts.dir"
    }
}