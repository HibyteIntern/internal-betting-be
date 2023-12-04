package ro.hibyte.betting.mapper

import org.springframework.stereotype.Component
import ro.hibyte.betting.dto.EventRequest
import ro.hibyte.betting.dto.EventResponse
import ro.hibyte.betting.entity.Event
import java.sql.Timestamp
@Component
class EventMapper {
    fun mapEventRequestToEvent(eventRequest: EventRequest): Event {
        val defaultCreator = ""
        val defaultUserGroups = emptyList<String>()
        val defaultUserProfiles = emptyList<String>()
        val defaultTimestamp = Timestamp(System.currentTimeMillis())

        // Extract words starting with '#' from the description to populate tags
        val tags = Regex("#\\w+").findAll(eventRequest.description)
            .map { it.value }
            .toList()

        return Event(
            name = eventRequest.name,
            description = eventRequest.description,
            creator = defaultCreator,
            template = eventRequest.template,
            tags = tags,
            userGroups = defaultUserGroups,
            userProfiles = defaultUserProfiles,
            created = defaultTimestamp,
            lastModified = defaultTimestamp,
            startsAt = Timestamp.from(eventRequest.startsAt),
            endsAt = Timestamp.from(eventRequest.endsAt),
            status = eventRequest.status
        )
    }
    fun mapEventToEventResponse(event: Event): EventResponse {
        return EventResponse(
            eventId = event.eventId,
            name = event.name,
            description = event.description,
            creator = event.creator,
            tags = event.tags,
            userGroups = event.userGroups,
            userProfiles = event.userProfiles,
            created = event.created.toInstant(),
            lastModified = event.lastModified.toInstant(),
            startsAt = event.startsAt.toInstant(),
            endsAt = event.endsAt.toInstant(),
            status = event.status
        )
    }
}