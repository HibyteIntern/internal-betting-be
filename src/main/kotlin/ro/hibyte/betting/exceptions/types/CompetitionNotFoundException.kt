package ro.hibyte.betting.exceptions.types

class CompetitionNotFoundException(id: Long): RuntimeException("Competition with id=${id} was not found")