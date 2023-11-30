package ro.hibyte.betting.exceptions.types

class BetTemplateNotFoundException(id: Long) : RuntimeException("Could not find bet template with id: $id") {}