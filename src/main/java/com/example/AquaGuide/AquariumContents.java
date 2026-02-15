package com.example.AquaGuide;

import lombok.*;
@Builder
@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AquariumContents {
    private Long VolumeLiters;
    private Long speciesId;
    private String name;
    private String scientificName;
    private String careLevel;
    private Integer minVolumeLiters;
    private String zone;}





