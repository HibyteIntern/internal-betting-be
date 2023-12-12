package ro.hibyte.betting.mapper

import org.springframework.stereotype.Component
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.CompleteBetTypeDto
import ro.hibyte.betting.dto.EventRequest
import ro.hibyte.betting.dto.EventResponse
import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.BetType
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.service.BetTypeService
import ro.hibyte.betting.service.UserProfileService
import java.sql.Timestamp
import java.util.stream.Collectors

@Component
class EventMapper(private val betTypeMapper: BetTypeMapper, private val userProfileService: UserProfileService,
    private val betMapper: BetMapper) {
    fun mapEventRequestToEvent(eventRequest: EventRequest, betTypeService: BetTypeService): Event {
        val defaultCreator = ""
        val defaultUserGroups = emptyList<String>()
        val defaultUserProfiles = emptyList<Long?>()
        val defaultTimestamp = Timestamp(System.currentTimeMillis())
        // Extract words starting with '#' from the description to populate tags
        val tags = Regex("#\\w+").findAll(eventRequest.description)
            .map { it.value }
            .toList()

        val completeBetTypeDtoList: List<CompleteBetTypeDto> = eventRequest.completeBetTypeDtoList

        val betTypes:List<BetType> =  completeBetTypeDtoList.stream()
            .map(betTypeService::createBetType)
            .collect(Collectors.toList())

        val users: List<UserProfile> = eventRequest.userProfileIdList.stream()
            .map(userProfileService::get)
            .collect(Collectors.toList())

        val userProfiles: List<Long?> = users.stream()
            .map(userProfileService::getId)
            .collect(Collectors.toList())
        return Event(
            name = eventRequest.name,
            description = eventRequest.description,
            betTypes = betTypes,
            creator = defaultCreator,
            tags = tags,
            users = users,
            bets = arrayListOf(),
            userGroups = defaultUserGroups,
            userProfiles = userProfiles,
            created = defaultTimestamp,
            lastModified = defaultTimestamp,
            startsAt = Timestamp.from(eventRequest.startsAt),
            endsAt = Timestamp.from(eventRequest.endsAt),
            status = eventRequest.status
        )
    }
    fun mapEventToEventResponse(event: Event): EventResponse {
        val completeBetTypeDtoList:List<CompleteBetTypeDto> = event.betTypes.stream()
            .map(betTypeMapper::betTypeToCompleteBetTypeDto)
            .collect(Collectors.toList())

        val betList:List<BetDTO> = event.bets.stream()
            .map(betMapper::mapBetToBetDto)
            .collect(Collectors.toList())

        return EventResponse(
            eventId = event.eventId,
            name = event.name,
            description = event.description,
            creator = event.creator,
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
}