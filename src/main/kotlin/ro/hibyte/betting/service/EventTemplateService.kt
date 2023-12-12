package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.EventTemplateRequest
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.entity.EventTemplate
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.BetTemplateRepository
import ro.hibyte.betting.repository.EventTemplateRepository

@Service
class EventTemplateService(
    private var eventTemplateRepository: EventTemplateRepository,
    private var betTemplateRepository: BetTemplateRepository
) {

    private fun getBetTemplateListFromIdList(templateIds: List<Long>): MutableList<BetTemplate> {
        val betTemplates: MutableList<BetTemplate> = ArrayList()
        templateIds.forEach{templateId ->
            betTemplates.add(
                betTemplateRepository.findById(templateId).orElseThrow{ EntityNotFoundException("Bet Template", templateId)}
            )
        }
        return betTemplates
    }

    fun create(eventTemplateRequest: EventTemplateRequest): EventTemplate {
        val eventTemplate = EventTemplate(
            null,
            eventTemplateRequest.name,
            getBetTemplateListFromIdList(eventTemplateRequest.betTemplates)
        )
        return eventTemplateRepository.save(eventTemplate)
    }

    fun getById(id: Long): EventTemplate =
        eventTemplateRepository.findById(id).orElseThrow { EntityNotFoundException("Event Template", id) }


    fun getAll(): List<EventTemplate> =
        eventTemplateRepository.findAll()

    fun update(eventTemplateRequest: EventTemplateRequest, id: Long): EventTemplate {
        val eventTemplateToUpdate: EventTemplate = eventTemplateRepository.findById(id).orElseThrow{EntityNotFoundException("Event Template", id)}
        val eventTemplate = EventTemplate(
            null,
            eventTemplateRequest.name,
            getBetTemplateListFromIdList(eventTemplateRequest.betTemplates)
        )
        eventTemplateToUpdate.update(eventTemplate)
        return eventTemplateRepository.save(eventTemplateToUpdate)
    }

    fun delete(id: Long) =
        eventTemplateRepository.deleteById(id)
}
