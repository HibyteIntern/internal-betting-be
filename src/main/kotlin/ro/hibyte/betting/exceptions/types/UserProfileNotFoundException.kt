package ro.hibyte.betting.exceptions.types

class UserProfileNotFoundException(id: Long): RuntimeException("User Profile with id=${id} was not found")