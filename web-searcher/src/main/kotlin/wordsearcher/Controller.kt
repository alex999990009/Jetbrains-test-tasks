package wordsearcher

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val service: Service
) {
    @GetMapping("/texts/search")
    fun getTextsByWords(@RequestBody request: Request): Response {
        return Response(service.getTextsByWords(request.words))
    }
}