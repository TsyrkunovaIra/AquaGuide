package com.example.AquaGuide;

import lombok.*;

@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AquariumCompatibility {
    private Long existingSpeciesId;
    private String existingSpeciesName;
    private String compatibilityStatus;
    private String reason;

}
