package ro.hibyte.betting.exceptions.types

class EntityNotFoundException(entityType: String, id: Long): RuntimeException("$entityType with id= $id was not found")
