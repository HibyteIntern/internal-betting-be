package ro.hibyte.betting.exceptions.types

class BetTypeNotFoundException(id: Long): RuntimeException("Could not find bet type with id: $id")