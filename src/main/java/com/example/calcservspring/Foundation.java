package com.example.calcservspring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

@Getter
@Setter
public class Foundation {
    private double costOfMaterialPiles;
    private double quantityOfPile;
    private double costOfPilesInDB;

    private double perimeterOfExternalWalls;
    private double lengthOfInternalWalls;
    private String typeOfPiles;
    private String typeOfBeton;


    private double costOfBeton;
    private double costOfBetonInDB;
    private double quantityOfBeton;

    private double costOfArmature14;
    private double costOfArmature14InDB;
    private double quantityOfArmature14;

    private double costOfArmature8;
    private double costOfArmature8InDB;
    private double quantityOfArmature8;


    private double costOfPlank;
    private double costOfPlankInDB;
    private double quantityOfPlank;

    private double costOfBalk;
    private double costOfBalkInDB;
    private double quantityOfBalk;

    private double costOfMaterialRostverk;
    private double costOfMaterialOpalubka;

    public Foundation(double perimeterOfExternalWalls, double lengthOfInternalWalls, String typeOfPiles, String typeOfBeton) {
        this.perimeterOfExternalWalls = perimeterOfExternalWalls;
        this.lengthOfInternalWalls = lengthOfInternalWalls;
        this.typeOfPiles = typeOfPiles;
        this.typeOfBeton = typeOfBeton;
    }


    public void setPricesFromDB(){
        setPilesPrice("costOfPilesInDB", this.typeOfPiles);
        setBetonPrice("costOfBetonInDB", this.typeOfBeton);
        setPrice("costOfArmature14InDB", 84);
        setPrice("costOfArmature8InDB", 83);
        setPrice("costOfPlankInDB", 85);            //Доска  30*100*3000
        setPrice("costOfBalkInDB", 86);             //Брус 50*50*3000
    }



    public void costOfMaterialPiles(){
        //<Количество свай> = ОКРУГЛВВЕРХ((<Периметр внешних стен> + <Длина внутренних стен>) / 2)
        this.quantityOfPile = Math.ceil((this.perimeterOfExternalWalls + this.lengthOfInternalWalls) / 2);
        //<Стоимость Свай> = <Количество свай> * <Стоимость свай в бд>
        this.costOfMaterialPiles = this.quantityOfPile * this.costOfPilesInDB;
        //<Стоимость материалов Сваи>  = <Стоимость Свай>
        //double costOfPile = this.costOfPile;

    }

    public void costOfMaterialRostverk() {
        //<Количество Арматуры 8 мм> =ОКРУГЛВВЕРХ((<Периметр внешних стен> + <Длина внутренних стен>) / 0,3 * (0,2 + 0,3) * 2 / 6)
        this.quantityOfArmature8 = Math.ceil((this.perimeterOfExternalWalls + this.lengthOfInternalWalls) / 0.3 * (0.2 + 0.3) * 2 / 6);
        //<Стоимость Арматуры 8 мм> =<Стоимость Арматуры 8 мм в бд> * <Количество Арматуры 8 мм>
        this.costOfArmature8 = this.costOfArmature8InDB * this.quantityOfArmature8;

        //<Количество Арматуры 14 мм> =ОКРУГЛВВЕРХ((<Периметр внешних стен> + <Длина внутренних стен>) * 4 / 6)
        this.quantityOfArmature14 = Math.ceil((this.perimeterOfExternalWalls + this.lengthOfInternalWalls) * 4 / 6);
        //<Стоимость Арматуры 14 мм> = <Стоимость Арматуры 14 мм в бд> * <Количество Арматуры 14 мм>
        this.costOfArmature14 = this.costOfArmature14InDB * this.quantityOfArmature14;

        //<Количество бетона> = (<Периметр внешних стен> + <Длина внутренних стен>) * 0,3 * 0,4 * 1,15
        this.quantityOfBeton = (this.perimeterOfExternalWalls + this.lengthOfInternalWalls) * 0.3 * 0.4 * 1.15;
        //<Стоимость Бетона> = <Стоимость бетона в бд> * <Количество бетона>
        this.costOfBeton = this.costOfBetonInDB * this.quantityOfBeton;

        //<Стоимость материалов Ростверк> = <Стоимость Бетона> + <Стоимость Арматуры 14 мм> + <Стоимость Арматуры 8 мм>
        this.costOfMaterialRostverk = this.costOfBeton + this.costOfArmature14 + this.costOfArmature8;

    }

    public void costOfMaterialOpalubka(){
        //<Количество Бруса> = ((<Периметр внешних стен> + <Длина внутренних стен>) * 2) / 0,7 * 0,5 * 0,05 * 0,05
        this.quantityOfBalk = ((this.perimeterOfExternalWalls + this.lengthOfInternalWalls) * 2) / 0.7 * 0.5 * 0.05 * 0.05;
        //<Стоимость Бруса> = <Стоимость Бруса из БД> * <Количество Бруса>
        this.costOfBalk = this.costOfBalkInDB * this.quantityOfBalk;
        //<Количество Досок> = ((<Периметр внешних стен> + <Длина внутренних стен>) * 2 * (0,4 + 0,1) * 0,03)
        this.quantityOfPlank = (this.perimeterOfExternalWalls + this.lengthOfInternalWalls)  * 2 * (0.4 + 0.1) * 0.03;
        //<Стоимость Досок> = <Стоимость Досок из БД> * <Количество Досок>
        this.costOfPlank = this.costOfPlankInDB * this.quantityOfPlank;
        //<Стоимость материалов Опалубка> = <Стоимость Досок> + <Стоимость Бруса>
        this.costOfMaterialOpalubka = this.costOfPlank + this.costOfBalk;
    }


    private void setPilesPrice(String fieldName, String type){
        switch (type) {
            case "Бетонные сваи 150*150*3000":
                setPrice(fieldName, 66);
                break;
            case "Бетонные сваи 200*200*3000":
                setPrice(fieldName, 67);
                break;
            case "Бетонные сваи 300*300*3000":
                setPrice(fieldName, 68);
                break;
            case "Бетонные сваи 300*300*4000":
                setPrice(fieldName, 69);
                break;
            case "Бетонные сваи 300*300*5000":
                setPrice(fieldName, 70);
                break;
            default:
                try {
                    Field field = getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.setDouble(this, 0);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setBetonPrice(String fieldName, String type){
        switch (type) {
            case "М150(В10)":
                setPrice(fieldName, 71);
                break;
            case "М200(В15)":
                setPrice(fieldName, 72);
                break;
            case "М250(В20)":
                setPrice(fieldName, 73);
                break;
            case "М300 (В22.5)":
                setPrice(fieldName, 74);
                break;
            case "М350(В25)":
                setPrice(fieldName, 75);
                break;
            case "М400(В30)":
                setPrice(fieldName, 76);
                break;
            case "М450(В35)":
                setPrice(fieldName, 77);
                break;
            case "М500(В40)":
                setPrice(fieldName, 78);
                break;
            case "М550(В45)":
                setPrice(fieldName, 79);
                break;
            case "М600(В50)":
                setPrice(fieldName, 80);
                break;
            case "М700(В55)":
                setPrice(fieldName, 81);
                break;
            case "М800(В65)":
                setPrice(fieldName, 82);
                break;
            default:
                try {
                    Field field = getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.setDouble(this, 0);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
        }
    }



    public void setPrice(String fieldName, int ID){
        // Создание объекта RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // URL для GET-запроса
        String url = "http://176.57.217.223:5000/api/v1/getMaterialsInfo/" + ID;

        // Отправка GET-запроса и получение ответа в виде ResponseEntity
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if (response.getStatusCode().is3xxRedirection()) {
            String redirectUrl = response.getHeaders().getLocation().toString();
            response = restTemplate.getForEntity(redirectUrl, String.class);
        }

        // Получение тела ответа
        String responseBody = response.getBody();

        try {
            // Создаем объект ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Преобразуем JSON строку в JsonNode
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // Извлекаем значение "256.0"
            double value = jsonNode.get(String.valueOf(ID)).get("price").asDouble();

            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.setDouble(this, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
