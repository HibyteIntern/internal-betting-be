package ro.hibyte.betting.mapper

import org.springframework.stereotype.Component
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.CompleteBetTypeDTO
import ro.hibyte.betting.dto.EventDTO
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.*
import ro.hibyte.betting.repository.UserGroupRepository
import ro.hibyte.betting.service.BetTypeService
import ro.hibyte.betting.service.UserProfileService
import java.sql.Timestamp
import java.util.stream.Collectors

@Component
class EventMapper(
    private val betTypeMapper: BetTypeMapper,
    private val userProfileService: UserProfileService,
    private val betMapper: BetMapper,
    private val betTypeService: BetTypeService,
    private val userGroupRepository: UserGroupRepository
) {
    fun mapEventRequestToEvent(eventRequest: EventDTO, creator: UserProfile): Event {
        val defaultTimestamp = Timestamp(System.currentTimeMillis())
        // Extract words starting with '#' from the description to populate tags
        val tags = eventRequest.description?.let {
            Regex("#\\w+").findAll(it)
                .map { it.value }
                .toList()
        }

        val completeBetTypeDtoList: List<CompleteBetTypeDTO>? = eventRequest.completeBetTypeDtoList

        val betTypes: List<BetType> = completeBetTypeDtoList
            ?.let { dtoList ->
                dtoList.map { betTypeService.create(it) }
            }
            ?: emptyList()

        val userGroupIds: Set<Long> = eventRequest.userGroupIds?: emptySet()

        val usersFromGroups: Set<UserProfile> = userGroupIds
            .mapNotNull { it.let { userGroupRepository.findById(it).orElse(null)} }
            .toSet()
            .flatMap { it.users.orEmpty() }
            .toSet()


        val users: Set<UserProfile> = eventRequest.userProfileIds
            ?.mapNotNull { it?.let { userProfileService.findById(it) } }
            ?.toSet()
            ?: emptySet()

        val userProfileIds: Set<Long?> = users.map { it.userId }.toSet()

        val allUsers: Set<UserProfile> = usersFromGroups + users

        return Event(
            name = eventRequest.name?: "",
            description = eventRequest.description?:"",
            betTypes = betTypes,
            creator = creator,
            tags = tags?: emptyList(),
            userProfiles = allUsers,
            userGroupIds = userGroupIds,
            userProfileIds = userProfileIds,
            created = defaultTimestamp,
            lastModified = defaultTimestamp,
            startsAt = Timestamp.from(eventRequest.startsAt),
            endsAt = Timestamp.from(eventRequest.endsAt),
            status = eventRequest.status?: Status.DRAFT
        )
    }
    fun mapEventToEventResponse(event: Event): EventDTO {
        val completeBetTypeDtoList:List<CompleteBetTypeDTO> = event.betTypes.stream()
            .map(betTypeMapper::betTypeToCompleteBetTypeDto)
            .collect(Collectors.toList())

        val betList:List<BetDTO> = event.betTypes.flatMap { betType -> betType.bets.map { betMapper.mapBetToBetDto(it) } }

        val allUsers: Set<Long?> = event.userProfiles.map { it.userId }.toSet()

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
            userProfiles = allUsers,
            userGroupIds = event.userGroupIds,
            userProfileIds = event.userProfileIds,
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
