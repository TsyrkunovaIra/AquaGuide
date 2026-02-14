package com.example.AquaGuide;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CalculationConsumer {
    private final JdbcTemplate jdbcTemplate;
    // Инжектим JdbcTemplate для работы с БД
    public CalculationConsumer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @KafkaListener(topics = "aquarium-updates", groupId = "aquarium-group",autoStartup = "true",
    properties = "spring.json.value.default.type=com.example.AquaGuide.AquariumUpdateEvent")

    public void consume(ConsumerRecord<String, AquariumUpdateEvent> event) {
        System.out.println("Received event for aquarium: " + event.value().getAquariumId());
        recalculateEcosystem(event.value().getAquariumId());;

        recalculateEcosystem(event.value().getAquariumId());
    }
    private void recalculateEcosystem(Long aquariumId) { //логика пересчета совместимости
        try {
            // 1. Проверка температурных конфликтов
            List<String> tempConflicts = getTemperatureConflicts(aquariumId);
            // 2. Проверка совместимости и советы
            List<Map<String, Object>> advices = getAquariumAdvice(aquariumId);
            // 3. Вывод или сохранение результатов
            advices.forEach(a -> System.out.println("[" + a.get("level") + "] " + a.get("advice_text")));
            // 4. Финализация
            finalizeAquarium(aquariumId);

        } catch (Exception e) {
            System.err.println("Ошибка при анализе аквариума " + aquariumId + ": " + e.getMessage());
        }
    }
    private List<String> getTemperatureConflicts(Long id) {
        String sql = "SELECT s.name FROM Aquarium_Contents ac " +
                "JOIN Species s ON ac.species_id = s.species_id WHERE ac.aquarium_id = ? " +
                "AND (s.temp_min > (...) OR s.temp_max < (...))";
        return jdbcTemplate.queryForList(sql, String.class, id);
    }

    private List<Map<String, Object>> getAquariumAdvice(Long id) {
        String sql = "WITH CurrentInhabitants AS (" +
                "  SELECT ac.species_id, s.name, s.is_schooling, s.min_group_size, s.zone, ac.quantity " +
                "  FROM Aquarium_Contents ac JOIN Species s ON ac.species_id = s.species_id WHERE ac.aquarium_id = :id" +
                ") " +
                "SELECT 'Compatibility' AS cat, 'Warning' AS level, 'Конфликт: '||c1.name||' и '||c2.name AS advice_text " +
                "FROM CurrentInhabitants c1 JOIN Compatibility comp ON c1.species_id = comp.species_a_id " +
                "JOIN CurrentInhabitants c2 ON comp.species_b_id = c2.species_id WHERE comp.compatibility_status IN ('Incompatible', 'Warning') " +
                "UNION ALL " +
                "SELECT 'Social', 'Critical', 'Мало особей вида '||name FROM CurrentInhabitants WHERE is_schooling = TRUE AND quantity < min_group_size " +
                "UNION ALL " +
                "SELECT 'Zoning', 'Info', 'Зона '||z.zn||' пуста' FROM (SELECT unnest(ARRAY['Top','Middle','Bottom']) AS zn) z " +
                "LEFT JOIN CurrentInhabitants ci ON ci.zone::text = z.zn WHERE ci.species_id IS NULL";
        return jdbcTemplate.queryForList(sql, Collections.singletonMap("id", id));
    }

    public void updateBioload(Long id, Long sid, Integer qty) {
        // Выполняем блок с UPSERT и проверкой нагрузки
        String sql = "INSERT INTO Aquarium_Contents (aquarium_id, species_id, quantity) " +
                "VALUES (:id, :sid, :qty) ON CONFLICT (aquarium_id, species_id) " +
                "DO UPDATE SET quantity = EXCLUDED.quantity";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id).addValue("sid", sid).addValue("qty", qty));
    }

    private void finalizeAquarium(Long id) {
        String sql = "UPDATE Aquariums SET is_new = FALSE WHERE aquarium_id = :id AND is_new = TRUE";
        jdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }
}

