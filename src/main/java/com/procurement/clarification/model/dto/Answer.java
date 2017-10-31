package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class Answer {

        @JsonProperty("answer")
        private final String answer;
        @JsonProperty("date")
        private final String date;

        @JsonCreator
        public Answer(
            @JsonProperty("answer")final String answer,
            @JsonProperty("date") final String date) {
            this.answer = answer;
            this.date = date;
        }

}
