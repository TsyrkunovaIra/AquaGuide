package com.example.AquaGuide;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/v1/aquariums")
@RequiredArgsConstructor
public class AquariumController {

    private final AquariumService aquariumService;

      @PostMapping
    public ResponseEntity<String> create(@RequestBody AquariumUpdateEvent event) {
         Integer id = aquariumService.createAquarium(event);
        return ResponseEntity.ok(id.toString());
       /// return processEvent(null, event, "CREATE_AQUARIUM", "Запрос на создание отправлен.");
    }
    @PostMapping("/{id}/contents")
    public ResponseEntity<String> addSpecies(
            @PathVariable Long id,
            @RequestBody AquariumUpdateEvent event) {
        return processEvent(id, event, "ADD_SPECIES", "Событие отпралено в кафку. Подбор обитателей запущен.");

    }
    @GetMapping (value = "/{id}/available-species", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    List<AquariumContents> addContents(@PathVariable Long id) {
        List<AquariumContents>aquariumContents = aquariumService.aquariumContents(id);
        return aquariumContents;
    }
    private ResponseEntity<String> processEvent(Long id, AquariumUpdateEvent event, String action, String message) {
        event.setAquariumId(id);
        event.setAction(action);
      //  producer.sendUpdate(event);
        return ResponseEntity.ok(message);
    }
}

//    public AquariumController(AquariumProducer producer) {
//
//        this.producer = producer;
//    }
//    @PostMapping
//    public ResponseEntity<String> createAquarium(@RequestBody AquariumUpdateEvent event) {
//
//        event.setAction("CREATE_AQUARIUM");
//
//        // Отправляем событие на создание в Kafka
//        producer.sendUpdate(event);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body("Запрос на создание аквариума отправлен в Kafka.");
//    }
//    @PostMapping("/{id}/contents")
//    public ResponseEntity<String> updateContents(
//            @PathVariable Long id,
//            @RequestBody AquariumUpdateEvent event) {
//
//        //  ID из пути и помечаем действие
//        event.setAquariumId(id);
//        event.setAction("ADD_SPECIES");
//
//        // Отправляем в Kafka (Продюсер)
//        producer.sendUpdate(event);
//
//        return ResponseEntity.ok("Событие отправлено в Kafka. Расчет совместимости запущен.");
////    }
//
//}
//    @PatchMapping("/{id}/level")
//    public ResponseEntity<Map<String, Object>> updateCareLevel(
//            @PathVariable Long id,
//            @RequestBody AquariumUpdateEvent event) {
//
//        // Устанавливаем ID из пути
//        event.setAquariumId(id);
//
//        // Помечаем действие для Kafka
//        event.setAction("UPDATE_CARE_LEVEL");
//
//        // Отправляем событие в Kafka
//        producer.sendUpdate(event);
//
//        // Формируем тело ответа согласно спецификации
//        Map<String, Object> response = new HashMap<>();
//        response.put("aquarium_id", id);
//        response.put("care_level", event.getCareLevel());
//
//        return ResponseEntity.ok(response);
//    }



