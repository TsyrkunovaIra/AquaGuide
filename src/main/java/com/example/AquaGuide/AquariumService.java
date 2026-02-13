package com.example.AquaGuide;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AquariumService {
    private static final String creatAquariumQry = """
            INSERT INTO Aquariums (
                user_id,\s
                name,\s
                volume_liters,\s
                length,\s
                width,\s
                height,\s
                care_level\s
                   )\s
            VALUES (
                :user_id,       
                :name,         
                :volume_liters, 
                :length,        
                :width,         
                :height,
                :care_level     
            )
            RETURNING aquarium_id; 
               """;


    private final NamedParameterJdbcTemplate jdbcTemplate;




    public Integer createAquarium(AquariumUpdateEvent event) {
        var parmMap = Map.of("user_id", event.getUserId(),
                "name", event.getName(),
                "volume_liters", event.getVolumeLiters(),
                "length", event.getLength(),
                "width", event.getWidth(),
                "height", event.getHeight(),
                "care_level", event.getCareLevel()
                );   //////создать методы по примеру юзера
        return
    jdbcTemplate.queryForObject(creatAquariumQry, parmMap, Integer.class);

    }

    private static final String getContents = """
            SELECT\s
                s.species_id,
                s.name,
                s.scientific_name,
                s.care_level,
                s.min_volume_liters,
                s.zone,
                CASE\s
                    WHEN s.care_level = 'Low' THEN 'Green'
                    WHEN s.care_level = 'Medium' THEN 'Yellow'
                    WHEN s.care_level = 'High' THEN 'Red'
                END AS visual_marker
            FROM Species s
            JOIN Aquariums a ON a.aquarium_id = :id
            WHERE\s
                s.min_volume_liters <= a.volume_liters
                AND (
                    (a.care_level = 'Low' AND s.care_level = 'Low')
                    OR\s
                    (a.care_level = 'Medium' AND s.care_level IN ('Low', 'Medium'))
                    OR\s
                    (a.care_level = 'High')
                )
                AND NOT (
                    a.care_level = 'Low'\s
                    AND (
                     (s.scientific_name IN ('Symphysodon discus', 'Hemianthus callitrichoides', 'Pterophyllum altum')
                        OR s.bioload_index > 7 -- Сложная фильтрация не для новичков
                    )
                ))
            ORDER BY s.care_level ASC, s.name ASC;""";
    public List<AquariumContents>aquariumContents(long aquariumId) {
        var parmMap = Map.of("id", aquariumId);   //////создать методы по примеру юзера
        return jdbcTemplate.query( getContents, parmMap, new BeanPropertyRowMapper<>(AquariumContents.class));

    }

}
