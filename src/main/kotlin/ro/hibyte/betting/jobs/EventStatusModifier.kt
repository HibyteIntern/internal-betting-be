package ro.hibyte.betting.jobs

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import ro.hibyte.betting.service.EventService

@Configuration
@EnableScheduling
@EnableAsync
class EventStatusModifier(private val eventService: EventService) {
    @Scheduled(cron = "0 0 * * * ?")
    fun updateEventStatusChange() {
        eventService.checkEventsEndDate()
    }
}
