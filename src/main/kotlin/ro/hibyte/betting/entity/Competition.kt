package ro.hibyte.betting.entity

import jakarta.persistence.*
import org.springframework.beans.factory.annotation.Autowired
import ro.hibyte.betting.dto.CompetitionDto
import ro.hibyte.betting.service.CompetitionService
import java.sql.Timestamp

@Entity
data class Competition(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var competitionId: Long = 0,
    var name: String = "",
    var creator: String = "",
    // TODO: users
    @ElementCollection
    var userGroups: List<String> = emptyList(),
    @ElementCollection
    var userProfiles: List<String> = emptyList(),
    @OneToMany
    var events: List<Event> = emptyList(),
    @Temporal(TemporalType.TIMESTAMP)
    var created : Timestamp = Timestamp(System.currentTimeMillis()),
    @Temporal(TemporalType.TIMESTAMP)
    var lastModified : Timestamp = Timestamp(System.currentTimeMillis()),
    var status: Status = Status.Draft
    ) {
    constructor(
        dto: CompetitionDto,
    ): this(
        name = dto.name,
        creator = dto.creator,
        // TODO: users
        userGroups = dto.userGroups,
        userProfiles = dto.userProfiles,
        events = dto.events,
        created = dto.created.let { Timestamp.from(it) },
        lastModified = dto.lastModified.let { Timestamp.from(it) },
        status = dto.status
    )
//    constructor(dto: CompetitionDto): this(
//        id = dto.id,
//        name = dto.name,
//        creator = dto.creator,
//        // TODO: users
//        userGroups = dto.userGroups,
//        userProfiles = dto.userProfiles,
//        events = competitionService.getEventsFromIds(dto.events!!),
//        created = dto.created?.let { Timestamp.from(it) },
//        lastModified = dto.lastModified?.let { Timestamp.from(it) },
//        status = when(dto.status) {
//            "Draft" -> Status.Draft
//            "Open" -> Status.Open
//            "Close" -> Status.Closed
//            else -> null
//        }
//        )

//    fun update(dto: CompetitionDto, competitionService: CompetitionService) {
//        name = dto.name
//        creator = dto.creator
//        // TODO: users
//        userGroups = dto.userGroups
//        userProfiles = dto.userProfiles
//        events =  competitionService.getEventsFromIds(dto.events!!)
//        created = dto.created?.let { Timestamp.from(it) }
//        lastModified = dto.lastModified?.let { Timestamp.from(it) }
//        status = when(dto.status) {
//            "Draft" -> Status.Draft
//            "Open" -> Status.Open
//            "Close" -> Status.Closed
//            else -> null
//        }
//    }
}