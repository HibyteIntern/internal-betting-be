package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.EventTemplateRequest
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.entity.EventTemplate
import ro.hibyte.betting.exceptions.types.BetTemplateNotFoundException
import ro.hibyte.betting.exceptions.types.EventTemplateNotFoundException
import ro.hibyte.betting.repository.BetTemplateRepository
import ro.hibyte.betting.repository.EventTemplateRepository

@Service
class EventTemplateService(
    private var eventTemplateRepository: EventTemplateRepository,
    private var betTemplateRepository: BetTemplateRepository
) {

    private fun getBetTemplateListFroIdList(templateIds: List<Long>): MutableList<BetTemplate> {
        val betTemplates: MutableList<BetTemplate> = ArrayList()
        templateIds.forEach{templateId ->
            betTemplates.add(
                betTemplateRepository.findById(templateId).orElseThrow{ BetTemplateNotFoundException(templateId)}
            )
        }
        return betTemplates
    }

    fun createEventTemplate(eventTemplateRequest: EventTemplateRequest): EventTemplate {
        val eventTemplate = EventTemplate(
            null,
            eventTemplateRequest.name,
            getBetTemplateListFroIdList(eventTemplateRequest.betTemplates)
        )
        return eventTemplateRepository.save(eventTemplate)
    }

    fun getEventTemplate(id: Long): EventTemplate =
        eventTemplateRepository.findById(id).orElseThrow { EventTemplateNotFoundException(id) }


    fun getEventTemplates(): List<EventTemplate> =
        eventTemplateRepository.findAll()

    fun updateEventTemplate(eventTemplateRequest: EventTemplateRequest, id: Long): EventTemplate {
        val eventTemplateToUpdate = eventTemplateRepository.findById(id).orElseThrow{EventTemplateNotFoundException(id)}
        val eventTemplate = EventTemplate(
            null,
            eventTemplateRequest.name,
            getBetTemplateListFroIdList(eventTemplateRequest.betTemplates)
        )
        eventTemplateToUpdate.update(eventTemplate)
        return eventTemplateRepository.save(eventTemplateToUpdate)
    }

    fun deleteEventTemplate(id: Long) =
        eventTemplateRepository.deleteById(id)
}