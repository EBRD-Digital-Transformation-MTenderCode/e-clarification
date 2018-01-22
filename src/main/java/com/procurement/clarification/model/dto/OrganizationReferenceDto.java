package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "id",
        "identifier",
        "address",
        "additionalIdentifiers",
        "contactPoint"
})
public class OrganizationReferenceDto {
    @JsonProperty("name")
    @JsonPropertyDescription("The name of the party being referenced. This must match the name of an entry in the " +
            "parties section.")
    private final String name;
    @JsonProperty("identifier")
    private final IdentifierDto identifier;
    @JsonProperty("address")
    private final AddressDto address;
    @JsonProperty("additionalIdentifiers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("(Deprecated outside the parties section) A list of additional / supplemental " +
            "identifiers for the organization, using the [organization identifier guidance](http://standard" +
            ".open-contracting.org/latest/en/schema/identifiers/). This could be used to provide an internally used " +
            "identifier for this organization in addition to the primary legal entity identifier.")
    private final Set<IdentifierDto> additionalIdentifiers;
    @JsonProperty("contactPoint")
    private final ContactPointDto contactPoint;
    @JsonProperty("id")
    @JsonPropertyDescription("The id of the party being referenced. This must match the id of an entry in the parties" +
            " section.")
    private String id;

    @JsonCreator
    public OrganizationReferenceDto(@JsonProperty("name") final String name,
                                    @JsonProperty("id") final String id,
                                    @JsonProperty("identifier") final IdentifierDto identifier,
                                    @JsonProperty("address") final AddressDto address,
                                    @JsonProperty("additionalIdentifiers") final LinkedHashSet<IdentifierDto>
                                         additionalIdentifiers,
                                    @JsonProperty("contactPoint") final ContactPointDto contactPoint) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.address = address;
        this.additionalIdentifiers = additionalIdentifiers;
        this.contactPoint = contactPoint;
    }
}
