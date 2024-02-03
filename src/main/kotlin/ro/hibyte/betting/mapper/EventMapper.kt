package ro.hibyte.betting.mapper

import org.springframework.stereotype.Component
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.CompleteBetTypeDto
import ro.hibyte.betting.dto.EventDTO
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.BetType
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.Status
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.service.BetTypeService
import ro.hibyte.betting.service.UserProfileService
import java.sql.Timestamp
import java.util.stream.Collectors

@Component
class EventMapper(
    private val betTypeMapper: BetTypeMapper,
    private val userProfileService: UserProfileService,
    private val betMapper: BetMapper,
    private val betTypeService: BetTypeService
) {
    fun mapEventRequestToEvent(eventRequest: EventDTO, creator: UserProfile): Event {
        val defaultUserGroups = emptyList<String>()
        val defaultTimestamp = Timestamp(System.currentTimeMillis())
        // Extract words starting with '#' from the description to populate tags
        val tags = eventRequest.description?.let {
            Regex("#\\w+").findAll(it)
                .map { it.value }
                .toList()
        }

        val completeBetTypeDtoList: List<CompleteBetTypeDto>? = eventRequest.completeBetTypeDtoList

        val betTypes: List<BetType> = completeBetTypeDtoList
            ?.let { dtoList ->
                dtoList.mapNotNull { betTypeService.create(it) }
            }
            ?: emptyList()

        val users: List<UserProfile> = eventRequest.userProfiles
            ?.mapNotNull { it?.let { userProfileService.get(it) } }
            ?: emptyList()

        val userProfiles: List<Long?> = users.stream()
            .map(userProfileService::getId)
            .collect(Collectors.toList())
        return Event(
            name = eventRequest.name?: "",
            description = eventRequest.description?:"",
            betTypes = betTypes,
            creator = creator,
            tags = tags?: emptyList(),
            users = users,
            bets = arrayListOf(),
            userGroups = defaultUserGroups,
            userProfiles = userProfiles,
            created = defaultTimestamp,
            lastModified = defaultTimestamp,
            startsAt = Timestamp.from(eventRequest.startsAt),
            endsAt = Timestamp.from(eventRequest.endsAt),
            status = eventRequest.status?: Status.DRAFT
        )
    }
    fun mapEventToEventResponse(event: Event): EventDTO {
        val completeBetTypeDtoList:List<CompleteBetTypeDto> = event.betTypes.stream()
            .map(betTypeMapper::betTypeToCompleteBetTypeDto)
            .collect(Collectors.toList())

        val betList:List<BetDTO> = event.bets.stream()
            .map(betMapper::mapBetToBetDto)
            .collect(Collectors.toList())

        var creator: UserProfileDTO? = null
        if(event.creator != null) {
            creator = UserProfileDTO(event.creator!!)
        }

        return EventDTO(
            eventId = event.eventId,
            name = event.name,
            description = event.description,
            creator = creator,
            tags = event.tags,
            completeBetTypeDtoList = completeBetTypeDtoList,
            userGroups = event.userGroups,
            userProfiles = event.userProfiles,
            created = event.created.toInstant(),
            lastModified = event.lastModified.toInstant(),
            startsAt = event.startsAt.toInstant(),
            endsAt = event.endsAt.toInstant(),
            status = event.status,
            bets = betList,
        )
    }

    fun mapToTags(event: Event): List<String> {
        return event.tags
    }
}
