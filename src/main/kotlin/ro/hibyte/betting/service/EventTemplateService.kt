package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.EventTemplateRequest
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.entity.EventTemplate
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.EventTemplateRepository

@Service
class EventTemplateService(
    private var eventTemplateRepository: EventTemplateRepository,
    private var betTemplateService: BetTemplateService,
) {

    private fun createBetTemplates(betTemplates: List<BetTemplate>): MutableList<BetTemplate> {
        val betTemplatesFromRepo: MutableList<BetTemplate> = ArrayList()
        betTemplates.forEach{template ->
            betTemplatesFromRepo.add(
               betTemplateService.create(template)
            )
        }
        return betTemplatesFromRepo
    }

    fun create(eventTemplateRequest: EventTemplateRequest): EventTemplate {
        val eventTemplate = EventTemplate(
            null,
            eventTemplateRequest.name,
            createBetTemplates(eventTemplateRequest.betTemplates)
        )
        return eventTemplateRepository.save(eventTemplate)
    }

    fun getById(id: Long): EventTemplate =
        eventTemplateRepository.findById(id).orElseThrow { EntityNotFoundException("Event Template", id) }


    fun getAll(): List<EventTemplate> =
        eventTemplateRepository.findAll()

    fun searchByName(name: String) =
        eventTemplateRepository.findAllByNameContainsIgnoreCase(name)

    fun update(eventTemplateRequest: EventTemplateRequest, id: Long): EventTemplate {
        val eventTemplateToUpdate: EventTemplate = eventTemplateRepository.findById(id).orElseThrow{EntityNotFoundException("Event Template", id)}
        val eventTemplate = EventTemplate(
            null,
            eventTemplateRequest.name,
            createBetTemplates(eventTemplateRequest.betTemplates)
        )
        eventTemplateToUpdate.update(eventTemplate)
        return eventTemplateRepository.save(eventTemplateToUpdate)
    }

    fun delete(id: Long) {
        eventTemplateRepository.findById(id).orElseThrow{EntityNotFoundException("Event Template", id)}
        eventTemplateRepository.deleteById(id)
    }
}
