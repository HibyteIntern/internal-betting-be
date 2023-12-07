package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Status

data class CompetitionRequest(
    var name: String,
    // TODO: users
    var userGroups: List<String>,
    var userProfiles: List<String>,
    var events: List<Long>,
    var status: Status
) {
}