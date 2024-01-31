package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Status

data class CompetitionRequest(
    var name: String,
    var description: String,
    var userGroups: List<String>,
    var userProfiles: List<String>,
    var events: List<String>,
    var status: Status,
)
