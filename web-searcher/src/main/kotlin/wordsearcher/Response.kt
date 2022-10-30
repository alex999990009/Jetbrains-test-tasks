package wordsearcher

import com.fasterxml.jackson.annotation.JsonProperty

data class Response(
    @JsonProperty("texts") val texts: List<String>?
)