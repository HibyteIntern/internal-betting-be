package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.Status

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findByName(name:String) : List<Event>
    fun findAllByStatus(status: Status) : List<Event>
    fun findAllByNameContainsIgnoreCase(name: String) : List<Event>

    fun findAllByEventIdIn (eventIds: List<Long>) : List<Event>
}
