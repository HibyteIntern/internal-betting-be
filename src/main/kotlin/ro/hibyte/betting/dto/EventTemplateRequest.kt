package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.BetTemplate

data class EventTemplateRequest (
    var name: String,
    var betTemplates: List<BetTemplate> = ArrayList()
)
