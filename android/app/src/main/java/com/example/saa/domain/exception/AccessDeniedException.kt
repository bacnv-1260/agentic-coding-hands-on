package com.example.saa.domain.exception

class AccessDeniedException(
    message: String = "Account domain not allowed",
) : Exception(message)
