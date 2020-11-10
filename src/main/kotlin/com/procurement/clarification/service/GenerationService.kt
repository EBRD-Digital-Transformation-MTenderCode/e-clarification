package com.procurement.clarification.service

import org.springframework.stereotype.Service
import java.util.*

@Service
class GenerationService {

    fun generateEnquiryId(): UUID = UUID.randomUUID()

    fun generateToken(): UUID = UUID.randomUUID()
}
