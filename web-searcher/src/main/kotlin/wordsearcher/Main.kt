package wordsearcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import wordsearcher.Service.Companion.VARIABLE_WORK_DIR

@SpringBootApplication
class WordSearcher

fun main(args: Array<String>) {
    // need to specify the working directory variable (VARIABLE_WORK_DIR = texts.dir)
    if (args.contains(VARIABLE_WORK_DIR)) {
        System.setProperty(VARIABLE_WORK_DIR, args[args.indexOf(VARIABLE_WORK_DIR) + 1])
    }
    runApplication<WordSearcher>(args = args)
}