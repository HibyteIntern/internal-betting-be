package ro.hibyte.betting.exceptions.types

class EventNotFoundException(id: Long): RuntimeException("Event with id=${id} was not found")