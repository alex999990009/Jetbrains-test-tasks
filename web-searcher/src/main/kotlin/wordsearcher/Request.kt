package wordsearcher

import com.fasterxml.jackson.annotation.JsonProperty

data class Request(
    @JsonProperty("words") val words: List<String>
)