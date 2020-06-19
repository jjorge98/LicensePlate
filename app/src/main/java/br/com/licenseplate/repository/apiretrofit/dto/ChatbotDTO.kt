package br.com.licenseplate.repository.apiretrofit.dto

data class ChatbotDTO(
    val queryResult: QueryResult? = null
)

data class QueryResult(
    val fulfillmentText: String? = null
)
