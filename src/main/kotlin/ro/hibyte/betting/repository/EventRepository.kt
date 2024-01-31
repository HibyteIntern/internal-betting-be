package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.hibyte.betting.entity.Event;
import java.sql.Timestamp

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findByName(name:String) : List<Event>
    fun findAllByNameContainsIgnoreCase(name: String) : List<Event>
    fun findByStartsAtGreaterThanEqualAndEndsAtLessThanEqual(
        providedStartDateTime: Timestamp, providedEndDateTime: Timestamp
    ): List<Event>
}
