package ro.hibyte.betting.entity

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import ro.hibyte.betting.dto.CompetitionDto
import java.sql.Timestamp

@Entity
data class Competition(
    @Id
    @GeneratedValue
    var id: Long? = null,
    var name: String? = null,
    var creator: String? = null,
    // TODO: users
    @ElementCollection
    var userGroups: List<String>? = null,
    @ElementCollection
    var userProfiles: List<String>? = null,
    // TODO: events
    @Temporal(TemporalType.TIMESTAMP)
    var created : Timestamp? = Timestamp(System.currentTimeMillis()),
    @Temporal(TemporalType.TIMESTAMP)
    var lastModified : Timestamp? = Timestamp(System.currentTimeMillis()),
    var status: Status? = null
    ) {

    constructor(dto: CompetitionDto): this(
        id = dto.id,
        name = dto.name,
        creator = dto.creator,
        // TODO: users
        userGroups = dto.userGroups,
        userProfiles = dto.userProfiles,
        // TODO: events
        created = dto.created?.let { Timestamp.from(it) },
        lastModified = dto.lastModified?.let { Timestamp.from(it) },
        status = when(dto.status) {
            "Draft" -> Status.Draft
            "Open" -> Status.Open
            "Close" -> Status.Closed
            else -> null
        }
        )

    fun update(dto: CompetitionDto) {
        name = dto.name
        creator = dto.creator
        // TODO: users
        userGroups = dto.userGroups
        userProfiles = dto.userProfiles
        // TODO: events
        created = dto.created?.let { Timestamp.from(it) }
        lastModified = dto.lastModified?.let { Timestamp.from(it) }
        status = when(dto.status) {
            "Draft" -> Status.Draft
            "Open" -> Status.Open
            "Close" -> Status.Closed
            else -> null
        }
    }
}