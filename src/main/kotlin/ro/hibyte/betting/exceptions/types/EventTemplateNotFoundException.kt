package ro.hibyte.betting.exceptions.types

class EventTemplateNotFoundException(id: Long): RuntimeException("Event template with id=${id} was not found")