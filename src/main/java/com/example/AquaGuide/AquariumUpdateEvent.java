package com.example.AquaGuide;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AquariumUpdateEvent {
//    @JsonProperty("care_level")
//    private String careLevel;
    private String careLevel;
    private Long aquariumId;
    private Integer height;
    private Long speciesId;
    private Integer quantity;
    private String action;
    private Long userId;
    private String name;
    private Integer volumeLiters;
    private Integer length;
    private Integer width;// "ADD", "REMOVE", "UPDATE"

    public AquariumUpdateEvent(Long aquariumId, Long speciesId, Integer quantity, String action) {
        this.aquariumId = aquariumId;
        this.speciesId = speciesId;
        this.quantity = quantity;
        this.action = action;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public Long getAquariumId() {
        return aquariumId;
    }

    public void setAquariumId(Long aquariumId) {
        this.aquariumId = aquariumId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVolumeLiters() {
        return volumeLiters;
    }

    public void setVolumeLiters(Integer volumeLiters) {
        this.volumeLiters = volumeLiters;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCareLevel() {
        return careLevel;
    }

    public void setCareLevel(String careLevel) {
        this.careLevel = careLevel;
    }

//    public String getCareLevel() {
//        return careLevel;
//    }
//
//    public void setCareLevel(String careLevel) {
//        this.careLevel = careLevel;
//    }
}
