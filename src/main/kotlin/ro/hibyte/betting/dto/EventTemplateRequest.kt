package ro.hibyte.betting.dto

data class EventTemplateRequest (
    var name: String,
    var betTemplates: List<Long> = ArrayList()
)
