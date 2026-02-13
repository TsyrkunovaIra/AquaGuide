package com.example.AquaGuide;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
                height\s
                   )\s
            VALUES (
                :user_id,       
                :name,         
                :volume_liters, 
                :length,        
                :width,         
                :height             
            )
            RETURNING aquarium_id; 
               """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AquariumService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    // Инжектим JdbcTemplate для работы с БД


    public Integer createAquarium(AquariumUpdateEvent event) {
        var parmMap = Map.of("user_id", event.getUserId(),
                "name", event.getName(),
                "volume_liters", event.getVolumeLiters(),
                "length", event.getLength(),
                "width", event.getWidth(),
                "height", event.getHeight()
                );   //////создать методы по примеру юзера
        return
    jdbcTemplate.queryForObject(creatAquariumQry, parmMap, Integer.class);

    }



}
