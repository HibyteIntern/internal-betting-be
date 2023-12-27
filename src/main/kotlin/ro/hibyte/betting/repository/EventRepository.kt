package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.hibyte.betting.entity.Event;

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findByName(name:String) : List<Event>
}
