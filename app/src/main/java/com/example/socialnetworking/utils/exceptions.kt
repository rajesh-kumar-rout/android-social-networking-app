package com.example.socialnetworking.utils


open class ServerException(override val message: String): Exception()

class EmailExistException: ServerException("Email is already taken")

class UnAuthenticatedException: ServerException("Please sign in again")

class AuthenticationFailedException: ServerException("Invalid email or password")

class InvalidRequestException: ServerException("Invalid Request")

class UnKnownException: Exception("Sorry, An unknown error occur")

class NoInternetException: Exception("No Internet")

class RequestTimeOutException: Exception("Sorry, timeout")

class CommunicationException: Exception("Sorry, Can not communicate with server")