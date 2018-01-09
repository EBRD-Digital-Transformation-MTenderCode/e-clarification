package com.procurement.clarification.service;

import org.springframework.stereotype.Service;

@Service
public interface RulesService {

    int getOffset(String country, String method);

    int getInterval(String country, String method);

}
