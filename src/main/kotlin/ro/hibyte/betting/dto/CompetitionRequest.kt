package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Status

data class CompetitionRequest(
    var name: String,
    var description: String,
    var users: List<Long>,
    var userGroups: List<String>,
    var userProfiles: List<String>,
    var events: List<Long>,
    var status: Status,
)
