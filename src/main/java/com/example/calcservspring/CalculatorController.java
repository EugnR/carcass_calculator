package com.example.calcservspring;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;

//@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class CalculatorController {

    //@Autowired
    //private RestTemplate restTemplate;
    // Форматирование с использованием DecimalFormat
    DecimalFormat df = new DecimalFormat("#.##"); // Ограничение до 2 знаков после запятой

    @PostMapping("/calculateFoundation")
    public String calculateFoundation(@RequestBody String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);    //содержимое json на уровне "1":{}
            ObjectMapper jsonMapper = new ObjectMapper();       //для сериализации в самом конце

            // Чтение значений userId и address
            String userId = rootNode.get("userId").asText();
            String address = rootNode.get("address").asText();
            double perimeterOfExternalWalls = rootNode.get("perimeterOfExternalWalls").asDouble();
            double lengthOfInternalWalls = rootNode.get("lengthOfInternalWalls").asDouble();
            String typeOfPiles = rootNode.get("typeOfPiles").asText();
            String typeOfBeton = rootNode.get("typeOfBeton").asText();

            Foundation foundation = new Foundation(perimeterOfExternalWalls, lengthOfInternalWalls, typeOfPiles, typeOfBeton);

            foundation.setPricesFromDB();
            foundation.costOfMaterialPiles();
            foundation.costOfMaterialRostverk();
            foundation.costOfMaterialOpalubka();


            Map<String, Object> quantityOfPiles = new HashMap<>();
            quantityOfPiles.put("material", foundation.getTypeOfPiles());
            quantityOfPiles.put("unit", "шт");
            quantityOfPiles.put("count",  df.format(foundation.getQuantityOfPile()));
            quantityOfPiles.put("cost",  df.format(foundation.getCostOfMaterialPiles()));

            Map<String, Object> piles = new HashMap<>();
            piles.put("countOfPiles", quantityOfPiles);
            piles.put("totalCost", df.format(foundation.getCostOfMaterialPiles()));
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            Map<String, Object> beton = new HashMap<>();
            beton.put("material", foundation.getTypeOfBeton());
            beton.put("unit", "м3");
            beton.put("count",  df.format(foundation.getQuantityOfBeton()));
            beton.put("cost",  df.format(foundation.getCostOfBeton()));

            Map<String, Object> armature14 = new HashMap<>();
            armature14.put("material", "Сталь");
            armature14.put("unit", "шт");
            armature14.put("count",  df.format(foundation.getQuantityOfArmature14()));
            armature14.put("cost",  df.format(foundation.getCostOfArmature14()));

            Map<String, Object> armature8 = new HashMap<>();
            armature8.put("material", "Сталь");
            armature8.put("unit", "шт");
            armature8.put("count",  df.format(foundation.getQuantityOfArmature8()));
            armature8.put("cost",  df.format(foundation.getCostOfArmature8()));

            Map<String, Object> rostverk = new HashMap<>();
            rostverk.put("beton", beton);
            rostverk.put("armature14", armature14);
            rostverk.put("armature8", armature8);
            rostverk.put("totalCost", df.format(foundation.getCostOfMaterialRostverk()));
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            Map<String, Object> plank = new HashMap<>();
            plank.put("material", "Доска 30*100*3000");
            plank.put("unit", "м3");
            plank.put("count",  df.format(foundation.getQuantityOfPlank()));
            plank.put("cost",  df.format(foundation.getCostOfPlank()));

            Map<String, Object> balk = new HashMap<>();
            balk.put("material", "Брус 50*50*3000");
            balk.put("unit", "м3");
            balk.put("count",  df.format(foundation.getQuantityOfBalk()));
            balk.put("cost",  df.format(foundation.getCostOfBalk()));

            Map<String, Object> opalubka = new HashMap<>();
            opalubka.put("plank", plank);
            opalubka.put("balk", balk);
            opalubka.put("totalCost", df.format(foundation.getCostOfMaterialOpalubka()));
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



            Map<String, Object> outputJson = new HashMap<>();
            outputJson.put("userId", userId);
            outputJson.put("address", address);

            outputJson.put("piles", piles);
            outputJson.put("rostverk", rostverk);
            outputJson.put("opalubka", opalubka);
            outputJson.put("totalMaterialsCost", df.format(foundation.getCostOfMaterialPiles() + foundation.getCostOfMaterialRostverk() + foundation.getCostOfMaterialOpalubka()));

            // Сериализуем объект в JSON
            // Возвращаем успешный ответ считанными значениями ключей
            return jsonMapper.writeValueAsString(outputJson);




        } catch (Exception e) {
            // В случае ошибки возвращаем ответ с ошибкой
            return "Ошибка обработки JSON: " + e.getMessage();
        }

    }





    @PostMapping("/createCarcass")
    public String calculateCarcass(@RequestBody String json) {
        try {
            // Преобразование JSON в объект JsonNode с помощью ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);    //содержимое json на уровне "1":{}

            // Чтение значений userId и address
             String userId = rootNode.get("userId").asText();
            String address = rootNode.get("address").asText();

            // Цикл для обработки произвольного количества узлов
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
            ArrayList<Carcass> carcassList = new ArrayList<>();
            ObjectMapper jsonMapper = new ObjectMapper();
            // Создаем главный объект Map с вложенным объектом Map
            Map<String, Object> outputJson = new HashMap<>();
            outputJson.put("userId", userId);
            outputJson.put("address", address);


            while (fieldsIterator.hasNext()) {                  //перебор этажей
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                String key = field.getKey();
                JsonNode valuesInFloor = field.getValue();

                double heightOfFloor;
                double perimeterOfExternalWalls;
                double areaOfBase;
                double thicknessOfExternalWall;
                double thicknessOfInternalWall;
                double lengthOfInternalWalls;

                boolean considerWindowsAndDoors = false;
                double windowHeight ;
                double windowWidth ;
                double windowQuantity ;
                double windowPerimeter = 0.0;
                double windowArea = 0.0;

                double heightOfExternalDoorway;
                double widthOfExternalDoorway;
                double quantityOfExternalDoorway;
                double perimeterOfExternalDoorway = 0.0;
                double areaOfExternalDoorway = 0.0;

                double quantityOfInternalDoorway;
                double heightOfInternalDoorway;
                double widthOfInternalDoorway;
                double perimeterOfInternalDoorway = 0.0;

                boolean addExternalWallSheathing = false;
                String osbTypeForExternalWalls = null;
                String steamWaterProofingTypeOfExternalWalls = null;
                String windProtectionTypeOfExternalWalls = null;
                String insulationTypeOfExternalWalls = null;

                boolean addInternalWallTrim = false;
                String osbTypeForInternalWalls = null;

                boolean addOverlaps = false;
                double thicknessOfOverlap = 0.0;
                String osbTypeForOverlap = null;
                String steamWaterProofingTypeOfOverlap = null;
                String windProtectionTypeOfOverlap = null;
                String insulationTypeOfOverlap = null;

                if (!key.equals("userId") && !key.equals("address")) {
                    // Обработка значений внутренних узлов
                    int numberOfFloor = Integer.parseInt(key);
                    // обязательные переменные
                    heightOfFloor = Double.parseDouble(valuesInFloor.get("heightOfFloor").asText());
                    perimeterOfExternalWalls = Double.parseDouble(valuesInFloor.get("perimeterOfExternalWalls").asText());
                    areaOfBase = Double.parseDouble(valuesInFloor.get("areaOfBase").asText());
                    thicknessOfExternalWall = Double.parseDouble(valuesInFloor.get("thicknessOfExternalWall").asText());
                    thicknessOfInternalWall = Double.parseDouble(valuesInFloor.get("thicknessOfInternalWall").asText());
                    lengthOfInternalWalls = Double.parseDouble(valuesInFloor.get("lengthOfInternalWalls").asText());


                    if (valuesInFloor.has("considerWindowsAndDoors")){
                        considerWindowsAndDoors = true;

                        JsonNode windowsArray = valuesInFloor.get("considerWindowsAndDoors").get("window");
                        for (JsonNode windowNode : windowsArray) {
                            windowHeight = windowNode.get("height").asDouble();
                            windowWidth = windowNode.get("width").asDouble();
                            windowQuantity = windowNode.get("quantity").asDouble();
                            windowPerimeter += (windowHeight + windowWidth) * 2 * windowQuantity;
                            windowArea += windowHeight * windowWidth * windowQuantity;
                        }

                        JsonNode externalDoorwaysArray = valuesInFloor.get("considerWindowsAndDoors").get("externalDoorways");
                        for (JsonNode extDoorwayNode : externalDoorwaysArray){
                            heightOfExternalDoorway = extDoorwayNode.get("height").asDouble();
                            widthOfExternalDoorway = extDoorwayNode.get("width").asDouble();
                            quantityOfExternalDoorway = extDoorwayNode.get("quantity").asDouble();
                            perimeterOfExternalDoorway += (heightOfExternalDoorway + widthOfExternalDoorway) * 2 * quantityOfExternalDoorway;
                            areaOfExternalDoorway += heightOfExternalDoorway * widthOfExternalDoorway * quantityOfExternalDoorway;
                        }

                        JsonNode internalDoorwaysArray = valuesInFloor.get("considerWindowsAndDoors").get("doorwaysInternal");
                        for (JsonNode intDoorwayNode : internalDoorwaysArray){
                            heightOfInternalDoorway = intDoorwayNode.get("height").asDouble();
                            widthOfInternalDoorway = intDoorwayNode.get("width").asDouble();
                            quantityOfInternalDoorway = intDoorwayNode.get("quantity").asDouble();
                            perimeterOfInternalDoorway += (heightOfInternalDoorway + widthOfInternalDoorway) * 2 * quantityOfInternalDoorway;
                        }

                    }

                    if (valuesInFloor.has("addExteriorWallSheathing")){
                        addExternalWallSheathing = true;
                        JsonNode extWallParams = valuesInFloor.get("addExteriorWallSheathing");
                        osbTypeForExternalWalls = extWallParams.get("OSB").asText();
                        steamWaterProofingTypeOfExternalWalls = extWallParams.get("vaporAndWaterproofing").asText();
                        windProtectionTypeOfExternalWalls = extWallParams.get("windProtection").asText();
                        insulationTypeOfExternalWalls = extWallParams.get("insulation").asText();
                    }

                    if (valuesInFloor.has("addInteriorWallTrim")){
                        addInternalWallTrim  = true;
                        JsonNode intWallParams = valuesInFloor.get("addInteriorWallTrim");
                        osbTypeForInternalWalls = intWallParams.get("OSB").asText();
                    }

                    if (valuesInFloor.has("addSlabs")){
                        addOverlaps = true;
                        JsonNode extWallParams = valuesInFloor.get("addSlabs");
                        thicknessOfOverlap = extWallParams.get("floorThickness").asDouble(); //вроде в миллиметрах
                        osbTypeForOverlap = extWallParams.get("OSB").asText();
                        steamWaterProofingTypeOfOverlap = extWallParams.get("vaporAndWaterproofing").asText();
                        windProtectionTypeOfOverlap = extWallParams.get("windProtection").asText();
                        insulationTypeOfOverlap = extWallParams.get("insulation").asText();
                    }


                    Carcass carcass = new Carcass(numberOfFloor,
                            heightOfFloor,
                            perimeterOfExternalWalls,
                            areaOfBase,
                            thicknessOfExternalWall,
                            thicknessOfInternalWall,
                            lengthOfInternalWalls,
                            considerWindowsAndDoors,
                            windowPerimeter,
                            windowArea,
                            perimeterOfExternalDoorway,
                            areaOfExternalDoorway,
                            perimeterOfInternalDoorway,
                            addExternalWallSheathing,
                            osbTypeForExternalWalls,
                            steamWaterProofingTypeOfExternalWalls,
                            windProtectionTypeOfExternalWalls,
                            insulationTypeOfExternalWalls,
                            addInternalWallTrim,
                            osbTypeForInternalWalls,
                            addOverlaps,
                            thicknessOfOverlap,
                            osbTypeForOverlap,
                            steamWaterProofingTypeOfOverlap,
                            windProtectionTypeOfOverlap,
                            insulationTypeOfOverlap
                    );
                    carcass.setPricesFromDB();
                    carcass.calculateExternalWalls();
                    carcass.calculateInternalWalls();
                    carcass.calculateFloorOverlaps();
                    carcass.calculateFullFloorPrice();

                    carcassList.add(carcass);
                }

                for (Carcass carcass : carcassList) {
//region <externalWalls>
                    Map<String, Object> plankExternal = new HashMap<>();
                    switch (String.valueOf(carcass.getThicknessOfExternalWall())) {
                        case "100.0" -> plankExternal.put("material", "Доска 50*100*3000");
                        case "150.0" -> plankExternal.put("material", "Доска 50*150*3000");
                        case "200.0" -> plankExternal.put("material", "Доска 50*200*3000");
                        case "250.0" -> plankExternal.put("material", "Доска 50*250*3000");
                    }
                    plankExternal.put("unit", "м3");
                    plankExternal.put("count", df.format(carcass.getVolumeOfPlanksForExternalWalls()));
                    plankExternal.put("cost", df.format(carcass.getPriceOfPlanksForExternalWalls()));

                    Map<String, Object> insulationExternal = new HashMap<>();
                    if (carcass.isAddExternalWallSheathing()){
                        insulationExternal.put("material", carcass.getInsulationTypeOfExternalWalls());
                        insulationExternal.put("unit", "м3");
                        insulationExternal.put("count", df.format(carcass.getInsulationVolumeOfExternalWalls()));
                        insulationExternal.put("cost", df.format(carcass.getPriceOfInsulationOfExternalWalls()));
                    } else{
                        insulationExternal.put("material", "None");
                        insulationExternal.put("unit", "м3");
                        insulationExternal.put("count", "None");
                        insulationExternal.put("cost", "None");
                    }

                    Map<String, Object> osbExternal = new HashMap<>();
                    if (carcass.isAddExternalWallSheathing()){
                        osbExternal.put("material", carcass.getOsbTypeForExternalWalls());
                        osbExternal.put("unit", "м2");
                        osbExternal.put("count", df.format(carcass.getOsbAreaOfExternalWalls()));
                        osbExternal.put("cost", df.format(carcass.getPriceOfOsbForExternalWalls()));
                    } else{
                        osbExternal.put("material", "None");
                        osbExternal.put("unit", "м2");
                        osbExternal.put("count", "None");
                        osbExternal.put("cost", "None");
                    }

                    Map<String, Object> vaporAndWaterproofingExternal = new HashMap<>();
                    if (carcass.isAddExternalWallSheathing()){
                        vaporAndWaterproofingExternal.put("material", carcass.getSteamWaterProofingTypeOfExternalWalls());
                        vaporAndWaterproofingExternal.put("unit", "м2");
                        vaporAndWaterproofingExternal.put("count", df.format(carcass.getSteamWaterProofingAreaOfExternalWalls()));
                        vaporAndWaterproofingExternal.put("cost", df.format(carcass.getPriceOfSteamWaterProofingOfExternalWalls()));
                    } else{
                        vaporAndWaterproofingExternal.put("material", "None");
                        vaporAndWaterproofingExternal.put("unit", "м2");
                        vaporAndWaterproofingExternal.put("count", "None");
                        vaporAndWaterproofingExternal.put("cost", "None");
                    }

                    Map<String, Object> windProtectionExternal = new HashMap<>();
                    if (carcass.isAddExternalWallSheathing()){
                        windProtectionExternal.put("material", carcass.getWindProtectionTypeOfExternalWalls());
                        windProtectionExternal.put("unit", "м2");
                        windProtectionExternal.put("count", df.format(carcass.getWindProtectionAreaOfExternalWalls()));
                        windProtectionExternal.put("cost", df.format(carcass.getPriceOfWindProtectionOfExternalWalls()));
                    } else{
                        windProtectionExternal.put("material", "None");
                        windProtectionExternal.put("unit", "м2");
                        windProtectionExternal.put("count", "None");
                        windProtectionExternal.put("cost", "None");
                    }

                    Map<String, Object> externalWalls = new HashMap<>();
                    externalWalls.put("plank", plankExternal);
                    externalWalls.put("insulation", insulationExternal);
                    externalWalls.put("osb", osbExternal);
                    externalWalls.put("vaporAndWaterproofing", vaporAndWaterproofingExternal);
                    externalWalls.put("windProtection", windProtectionExternal);
                    externalWalls.put("totalCost", df.format(carcass.getFullPriceOfExternalWalls()));
//endregion

//region <internalWalls>
                    Map<String, Object> plankInternal = new HashMap<>();
                    switch (String.valueOf(carcass.getThicknessOfInternalWall())) {
                        case "100.0" -> plankInternal.put("material", "Доска 50*100*3000");
                        case "150.0" -> plankInternal.put("material", "Доска 50*150*3000");
                        case "200.0" -> plankInternal.put("material", "Доска 50*200*3000");
                        case "250.0" -> plankInternal.put("material", "Доска 50*250*3000");
                    }
                    plankInternal.put("unit", "м3");
                    plankInternal.put("count", df.format(carcass.getVolumeOfPlanksForInternalStands()));
                    plankInternal.put("cost", df.format(carcass.getPriceOfPlanksForInternalStands()));

                    Map<String, Object> osbInternal = new HashMap<>();
                    if (carcass.isAddInternalWallTrim()){
                        osbInternal.put("material", carcass.getOsbTypeForInternalWalls());
                        osbInternal.put("unit", "м2");
                        osbInternal.put("count", df.format(carcass.getOsbAreaOfInternalWalls()));
                        osbInternal.put("cost", df.format(carcass.getPriceOfOsbForInternalWalls()));
                    } else{
                        osbInternal.put("material", "None");
                        osbInternal.put("unit", "м2");
                        osbInternal.put("count", "None");
                        osbInternal.put("cost", "None");
                    }

                    Map<String, Object> internalWalls = new HashMap<>();
                    internalWalls.put("plank", plankInternal);
                    internalWalls.put("osb", osbInternal);
                    internalWalls.put("totalCost", df.format(carcass.getFullPriceOfInternalWalls()));
//endregion

//region <floors>
                    Map<String, Object> plankOverlap = new HashMap<>();
                    if (carcass.isAddOverlaps()) {
                        switch (String.valueOf(carcass.getThicknessOfOverlap())) {
                            case "200.0" -> plankOverlap.put("material", "Доска 50*200*6000");
                            case "250.0" -> plankOverlap.put("material", "Доска 50*250*6000");
                        }
                        plankOverlap.put("unit", "м3");
                        plankOverlap.put("count", df.format(carcass.getVolumeOfPlanksForOverlap()));
                        plankOverlap.put("cost", df.format(carcass.getPriceOfPlanksForOverlaps()));
                    } else {
                        plankOverlap.put("material", "None");
                        plankOverlap.put("unit", "м3");
                        plankOverlap.put("count", "None");
                        plankOverlap.put("cost","None");
                    }



                    Map<String, Object> insulationOverlap = new HashMap<>();
                    if (carcass.isAddOverlaps()){
                        insulationOverlap.put("material", carcass.getInsulationTypeOfOverlap());
                        insulationOverlap.put("unit", "м3");
                        insulationOverlap.put("count", df.format(carcass.getInsulationVolumeForOverlap()));
                        insulationOverlap.put("cost", df.format(carcass.getPriceOfInsulationForOverlaps()));
                    } else{
                        insulationOverlap.put("material", "None");
                        insulationOverlap.put("unit", "м3");
                        insulationOverlap.put("count", "None");
                        insulationOverlap.put("cost", "None");
                    }

                    Map<String, Object> osbOverlap = new HashMap<>();
                    if (carcass.isAddOverlaps()){
                        osbOverlap.put("material", carcass.getOsbTypeForOverlap());
                        osbOverlap.put("unit", "м2");
                        osbOverlap.put("count", df.format(carcass.getOsbAreaForOverlap()));
                        osbOverlap.put("cost", df.format(carcass.getPriceOfOsbForOverlaps()));
                    } else{
                        osbOverlap.put("material", "None");
                        osbOverlap.put("unit", "м2");
                        osbOverlap.put("count", "None");
                        osbOverlap.put("cost", "None");
                    }

                    Map<String, Object> vaporAndWaterproofingOverlap = new HashMap<>();
                    if (carcass.isAddOverlaps()){
                        vaporAndWaterproofingOverlap.put("material", carcass.getSteamWaterProofingTypeOfOverlap());
                        vaporAndWaterproofingOverlap.put("unit", "м2");
                        vaporAndWaterproofingOverlap.put("count", df.format(carcass.getSteamWaterProofingAreaForOverlap()));
                        vaporAndWaterproofingOverlap.put("cost", df.format(carcass.getPriceOfSteamWaterProofingForOverlaps()));
                    } else{
                        vaporAndWaterproofingOverlap.put("material", "None");
                        vaporAndWaterproofingOverlap.put("unit", "м2");
                        vaporAndWaterproofingOverlap.put("count", "None");
                        vaporAndWaterproofingOverlap.put("cost", "None");
                    }

                    Map<String, Object> windProtectionOverlap = new HashMap<>();
                    if (carcass.isAddOverlaps()){
                        windProtectionOverlap.put("material", carcass.getWindProtectionTypeOfOverlap());
                        windProtectionOverlap.put("unit", "м2");
                        windProtectionOverlap.put("count", df.format(carcass.getWindProtectionAreaForOverlap()));
                        windProtectionOverlap.put("cost", df.format(carcass.getPriceOfWindProtectionForOverlaps()));
                    } else{
                        windProtectionOverlap.put("material", "None");
                        windProtectionOverlap.put("unit", "м2");
                        windProtectionOverlap.put("count", "None");
                        windProtectionOverlap.put("cost", "None");
                    }

                    Map<String, Object> floors = new HashMap<>();
                    floors.put("plank", plankOverlap);
                    floors.put("insulation", insulationOverlap);
                    floors.put("osb", osbOverlap);
                    floors.put("vaporAndWaterproofing", vaporAndWaterproofingOverlap);
                    floors.put("windProtection", windProtectionOverlap);
                    floors.put("totalCost", df.format(carcass.getFullPriceOfOverlaps()));

//endregion

                    Map<String, Object> results = new HashMap<>();
                    results.put("externalWalls", externalWalls);
                    results.put("internalWalls", internalWalls);
                    results.put("floors", floors);
                    results.put("totalMaterialsCost", df.format(carcass.getFullFloorPrice()));

                    Map<String, Object> numberOfFloor = new HashMap<>();
                    numberOfFloor.put("results", results);

                    outputJson.put(String.valueOf(carcass.getNumberOfFloor()), numberOfFloor);
                }
            }
            // Сериализуем объект в JSON
            // Возвращаем успешный ответ считанными значениями ключей
            return jsonMapper.writeValueAsString(outputJson);
        } catch (Exception e) {
            // В случае ошибки возвращаем ответ с ошибкой
            return "Ошибка обработки JSON: " + e.getMessage();
        }
    }












}
