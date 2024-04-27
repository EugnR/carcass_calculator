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

public class Carcass {

    public Carcass(int numberOfFloor, double heightOfFloor, double perimeterOfExternalWalls, double areaOfBase, double thicknessOfExternalWall, double thicknessOfInternalWall, double lengthOfInternalWalls, boolean considerWindowsAndDoors, double windowPerimeter, double windowArea, double perimeterOfExternalDoorway, double areaOfExternalDoorway, double perimeterOfInternalDoorway, boolean addExternalWallSheathing, String osbTypeForExternalWalls, String steamWaterProofingTypeOfExternalWalls, String windProtectionTypeOfExternalWalls, String insulationTypeOfExternalWalls, boolean addInternalWallTrim, String osbTypeForInternalWalls, boolean addOverlaps, double thicknessOfOverlap, String osbTypeForOverlap, String steamWaterProofingTypeOfOverlap, String windProtectionTypeOfOverlap, String insulationTypeOfOverlap) {
        this.numberOfFloor = numberOfFloor;
        this.heightOfFloor = heightOfFloor;
        this.perimeterOfExternalWalls = perimeterOfExternalWalls;
        this.areaOfBase = areaOfBase;
        this.thicknessOfExternalWall = thicknessOfExternalWall;
        this.thicknessOfInternalWall = thicknessOfInternalWall;
        this.lengthOfInternalWalls = lengthOfInternalWalls;
        this.considerWindowsAndDoors = considerWindowsAndDoors;
        this.windowPerimeter = windowPerimeter;
        this.windowArea = windowArea;
        this.perimeterOfExternalDoorway = perimeterOfExternalDoorway;
        this.areaOfExternalDoorway = areaOfExternalDoorway;
        this.perimeterOfInternalDoorway = perimeterOfInternalDoorway;
        this.addExternalWallSheathing = addExternalWallSheathing;
        this.osbTypeForExternalWalls = osbTypeForExternalWalls;
        this.steamWaterProofingTypeOfExternalWalls = steamWaterProofingTypeOfExternalWalls;
        this.windProtectionTypeOfExternalWalls = windProtectionTypeOfExternalWalls;
        this.insulationTypeOfExternalWalls = insulationTypeOfExternalWalls;
        this.addInternalWallTrim = addInternalWallTrim;
        this.osbTypeForInternalWalls = osbTypeForInternalWalls;
        this.addOverlaps = addOverlaps;
        this.thicknessOfOverlap = thicknessOfOverlap;
        this.osbTypeForOverlap = osbTypeForOverlap;
        this.steamWaterProofingTypeOfOverlap = steamWaterProofingTypeOfOverlap;
        this.windProtectionTypeOfOverlap = windProtectionTypeOfOverlap;
        this.insulationTypeOfOverlap = insulationTypeOfOverlap;
    }



    //region <ПАРАМЕТРЫ ЭТАЖА>

    public int numberOfFloor;      //вводится, ПОЛЯ НЕТ, в бд floor_number
    private double heightOfFloor;       //вводится в форме поле в бд floor_height int
    private double areaOfBase;          //вводится, в форме поле в бд base_area float
    private double fullFloorPrice;
    //endregion

    //region <ПАРАМЕТРЫ ВНЕШНИХ СТЕН>
    private double perimeterOfExternalWalls;        //(м) вводится, в форме поле  в бд perimeter_of_external_walls float
    private double areaOfExternalWalls;
    private double thicknessOfExternalWall;     //(мм) вводится, в форме выпад_список в бд external_wall_thickness float
    private double quantityOfPlanksForExternalWalls;
    private double volumeOfPlanksForExternalWalls;
    private double widthOfPlanksForExternalWalls;

    private double quantityOfPlanksForExternalStands;
    private double quantityOfPlanksForExternalOpenings;
    private double quantityOfPlanksForBase;

    private boolean considerWindowsAndDoors;
    private double windowPerimeter;
    private double windowArea;
    private double perimeterOfExternalDoorway;
    private double areaOfExternalDoorway;
    private double perimeterOfInternalDoorway;

    private boolean addExternalWallSheathing;
    private String osbTypeForExternalWalls;     //вводится, в форме выпад_список и мож дублироваться | в бд OSB_external_wall
    private double osbAreaOfExternalWalls;


    private String steamWaterProofingTypeOfExternalWalls;   //вводится, в форме выпад_список и мож дублироваться | в бд steam_waterproofing_external_wall
    private double steamWaterProofingAreaOfExternalWalls;


    private String windProtectionTypeOfExternalWalls;   //вводится, в форме выпад_список и мож дублироваться | в бд windscreen_external_wall
    private double windProtectionAreaOfExternalWalls;


    private String insulationTypeOfExternalWalls;   //вводится, в форме выпад_список и мож дублироваться | в бд insulation_external_wall
    private double insulationAreaForExternalWalls;

    private double insulationThicknessOfExternalWalls;  //вводится
    private double insulationVolumeOfExternalWalls;

    //endregion

    //region <ПАРАМЕТРЫ ВНУТРЕННИХ СТЕН>
    private double quantityOfPlanksForInternalStands;
    private double areaOfInternalWalls;
    private double lengthOfInternalWalls;   //вводится, в форме поле в бд internal_wall_length float
    private double quantityOfPlanksForInternalDoorways;
    private double quantityOfInternalDoorways;  //в форме поле в бд НЕТ? отсутствует связь сущности каркаса и проёмов
    private double heightOfInternalDoorway;     //в форме поле в бд НЕТ? отсутствует связь сущности каркаса и проёмов
    private double widthOfInternalDoorway;      //в форме поле в бд НЕТ? отсутствует связь сущности каркаса и проёмов
    private double quantityOfPlanksForInternalWalls;
    private double widthOfPlanksForInternalStands;
    private double thicknessOfInternalWall;
    private double volumeOfPlanksForInternalStands;

    private boolean addInternalWallTrim;
    private String osbTypeForInternalWalls;     //вводится, в форме выпад_список и мож дублироваться | в бд OSB_internal_wall
    private double osbAreaOfInternalWalls;

    //endregion

    //region <ПАРАМЕТРЫ ПЕРЕКРЫТИЙ>
    private boolean addOverlaps;
    private double quantityOfBeamsOfOverlap;
    private double widthOfPlanksForOverlapBeams;
    private double thicknessOfOverlap;      //вводится 200/250, в форме поле и мож дублироваться | в бд overlap_thickness
    private final String osbTypeForOverlap;     //вводится, в форме выпад_список и мож дублироваться | в бд OSB_thickness
    private final String steamWaterProofingTypeOfOverlap;    //вводится, в форме выпад_список и мож дублироваться | в бд steam_waterproofing__thickness
    private final String windProtectionTypeOfOverlap;    //вводится, в форме выпад_список и мож дублироваться | в бд windscreen_thickness
    private final String insulationTypeOfOverlap;   //вводится, в форме выпад_список и мож дублироваться | в бд insulation_thickness
    private double volumeOfPlanksForOverlap;
    private double osbAreaForOverlap;


    private double steamWaterProofingAreaForOverlap;

    private double windProtectionAreaForOverlap;

    private double insulationAreaForOverlap;

    private double insulationThicknessForOverlap;
    private double insulationVolumeForOverlap;

    private double fullPriceOfOverlaps;
    //endregion



    //region <Цены для внешних стен>
    private double priceOfPlanksForExternalWallsInDB;

    private double priceOfOsbForExternalWalls;
    private double priceOfOsbForExternalWallsInDB;
    private double priceOfSteamWaterProofingOfExternalWalls;
    private double priceOfSteamWaterProofingOfExternalWallsInDB;

    private double priceOfWindProtectionOfExternalWalls;
    private double priceOfWindProtectionOfExternalWallsInDB;

    private double priceOfInsulationOfExternalWalls;
    private double priceOfInsulationOfExternalWallsInDB;

    private double fullPriceOfExternalWalls;

    private double priceOfPlanksForExternalWalls;

    //endregion

    //region <Цены для внутренних стен>
    private double priceOfPlanksForInternalWallsInDB;
    private double priceOfOsbForInternalWallsInDB;
    private double priceOfPlanksForInternalStands = 0;
    private double priceOfOsbForInternalWalls = 0;
    private double fullPriceOfInternalWalls = 0;

    //endregion

    //region <Цены для перекрытий>
    private double priceOfPlanksForOverlapsInDB;
    private double priceOfOsbForOverlapsInDB;
    private double priceOfSteamWaterProofingForOverlapsInDB;
    private double priceOfWindProtectionForOverlapsInDB;
    private double priceOfInsulationForOverlapsInDB;
    private double priceOfPlanksForOverlaps = 0;
    private double priceOfOsbForOverlaps = 0;

    private double priceOfSteamWaterProofingForOverlaps = 0;
    private double priceOfWindProtectionForOverlaps = 0;
    private double priceOfInsulationForOverlaps = 0;
    //endregion

    public void setPricesFromDB(){
        switch (String.valueOf(thicknessOfExternalWall)) {
            case "100.0" -> setPrice("priceOfPlanksForExternalWallsInDB", 97);                       //Доска 50*100/150/200/250*3000
            case "150.0" -> setPrice("priceOfPlanksForExternalWallsInDB", 98);
            case "200.0" -> setPrice("priceOfPlanksForExternalWallsInDB", 99);
            case "250.0" -> setPrice("priceOfPlanksForExternalWallsInDB", 100);
        }

        if (addExternalWallSheathing) {
            setOsbPrice("priceOfOsbForExternalWallsInDB", this.osbTypeForExternalWalls);
            setSteamWaterProofingPrice("priceOfSteamWaterProofingOfExternalWallsInDB", this.steamWaterProofingTypeOfExternalWalls);
            setWindProtectionPrice("priceOfWindProtectionOfExternalWallsInDB", this.windProtectionTypeOfExternalWalls);
            setInsulationPrice("priceOfInsulationOfExternalWallsInDB", this.insulationTypeOfExternalWalls);
        }

        switch (String.valueOf(thicknessOfInternalWall)) {
            case "100.0" -> setPrice("priceOfPlanksForInternalWallsInDB", 97);                       //Доска 50*100/150/200/250*3000
            case "150.0" -> setPrice("priceOfPlanksForInternalWallsInDB", 98);
            case "200.0" -> setPrice("priceOfPlanksForInternalWallsInDB", 99);
            case "250.0" -> setPrice("priceOfPlanksForInternalWallsInDB", 100);
        }

        if (addInternalWallTrim) {
            setOsbPrice("priceOfOsbForInternalWallsInDB", this.osbTypeForInternalWalls);
        }

        if (addOverlaps) {
            switch (String.valueOf(thicknessOfOverlap)) {
                case "200.0" -> setPrice("priceOfPlanksForOverlapsInDB", 104);                       //Доска 50*200/250*6000
                case "250.0" -> setPrice("priceOfPlanksForOverlapsInDB", 105);                       //Доска 50*200/250*6000
            }
            setOsbPrice("priceOfOsbForOverlapsInDB", this.osbTypeForOverlap);
            setSteamWaterProofingPrice("priceOfSteamWaterProofingForOverlapsInDB", this.steamWaterProofingTypeOfOverlap);
            setWindProtectionPrice("priceOfWindProtectionForOverlapsInDB", this.windProtectionTypeOfOverlap);
            setInsulationPrice("priceOfInsulationForOverlapsInDB", this.insulationTypeOfOverlap);
        }
    }



    public void calculateExternalWalls(){
        //<Площадь внешних стен> = <Кол-во этажей> * <Высота этажа> * <Периметр этажа>
        this.areaOfExternalWalls = this.heightOfFloor * this.perimeterOfExternalWalls;
        //<Площадь внутренних стен> = <Кол-во этажей> * <Высота этажа> * <длина внутренних стен>
        this.areaOfInternalWalls = this.heightOfFloor * this.lengthOfInternalWalls;

        //<Количество досок на внешние стойки> = Округляем_Вверх(<Периметр внешних стен> / 0,6 + 1)
        this.quantityOfPlanksForExternalStands = Math.ceil(this.perimeterOfExternalWalls / 0.6 + 1);
        //this.priceOfPlanksForExternalStands = this.quantityOfPlanksForExternalStands * this.priceOfPlanksForInternalStandsInDB;

        //<Количество досок для основания> = <Периметр внешних стен> * 2 / 3
        this.quantityOfPlanksForBase = this.perimeterOfExternalWalls * 2 / 3;
        //this.priceOfPlanksForBase = this.quantityOfPlanksForBase * this.priceOfPlanksForBaseInDB;

        //<Количество досок на внеш проемы> = Округляем_Вверх(((<высота окна> + <ширина окна>) * 2 * <кол-во окон> +(<Высота внешнего дверного проема> + <Ширина внешнего дверного проема>) * <Кол-во внешних дверных проемов>) / 3)
        //периметр проемов умнож на колво и делим на длину доски (3м)
        if (considerWindowsAndDoors){
            this.quantityOfPlanksForExternalOpenings = Math.ceil( ( this.windowPerimeter + this.perimeterOfExternalDoorway) / 3);
        } else {
            this.quantityOfPlanksForExternalOpenings = 0.0;
        }

        //<Итого количество досок на внешние стены> = <Количество досок на внешние стойки> + <Количество досок для основания> + <Количество досок на проемы>
        this.quantityOfPlanksForExternalWalls = this.quantityOfPlanksForExternalStands + this.quantityOfPlanksForBase + this.quantityOfPlanksForExternalOpenings;

        //<Ширина доски на внешние стены> = <Толщина внешних стен>
        this.widthOfPlanksForExternalWalls = this.thicknessOfExternalWall;
        //<Объем досок на внешние стены> = <Итого количество досок на внешние стены> * (<Ширина доски на внешние стены>/ 1000) * 0,05 * 3
        this.volumeOfPlanksForExternalWalls = this.quantityOfPlanksForExternalWalls * (this.widthOfPlanksForExternalWalls / 1000) * 0.05 * 3;       //ширина доски дб в мм

        this.priceOfPlanksForExternalWalls = this.volumeOfPlanksForExternalWalls * this.priceOfPlanksForExternalWallsInDB;

        if (addExternalWallSheathing){
            //<площадь ОСБ для внеш стен> = <площадь внешних стен> * 2 * 1,15
            this.osbAreaOfExternalWalls = this.areaOfExternalWalls * 2 * 1.15;
            this.priceOfOsbForExternalWalls = this.osbAreaOfExternalWalls * this.priceOfOsbForExternalWallsInDB;

            //<Площадь парогидроизоляции> = <площадь внешних стен> * 1,15
            this.steamWaterProofingAreaOfExternalWalls = this.areaOfExternalWalls * 1.15;
            this.priceOfSteamWaterProofingOfExternalWalls = this.steamWaterProofingAreaOfExternalWalls * this.priceOfSteamWaterProofingOfExternalWallsInDB;

            //<Площадь ветрозащиты> = <площадь внешних стен> * 1,15
            this.windProtectionAreaOfExternalWalls = this.areaOfExternalWalls * 1.15;
            this.priceOfWindProtectionOfExternalWalls = this.windProtectionAreaOfExternalWalls * this.priceOfWindProtectionOfExternalWallsInDB;

            this.insulationThicknessOfExternalWalls = this.thicknessOfExternalWall;
            if(considerWindowsAndDoors){
                this.insulationAreaForExternalWalls = this.areaOfExternalWalls * 1.1 - ( this.windowArea) - (this.areaOfExternalDoorway);
            } else {
                this.insulationAreaForExternalWalls = this.areaOfExternalWalls * 1.1;
            }
            this.insulationVolumeOfExternalWalls = this.insulationAreaForExternalWalls * this.insulationThicknessOfExternalWalls / 1000;
            this.priceOfInsulationOfExternalWalls = this.insulationVolumeOfExternalWalls * this.priceOfInsulationOfExternalWallsInDB;
        }
        else {
            this.insulationThicknessOfExternalWalls = 0.0;
            this.insulationAreaForExternalWalls = 0.0;
            this.insulationVolumeOfExternalWalls = 0.0;
            this.priceOfInsulationOfExternalWalls = 0.0;
        }
    }



    public void calculateInternalWalls(){
        //РАССЧЁТ ВНУТРЕННИХ СТЕН
        //<Количество досок на внутренние стойки> = ОКРУГЛВВЕРХ(<Длина внутренних стен> / 0,6)
        this.quantityOfPlanksForInternalStands  = Math.ceil(this.lengthOfInternalWalls / 0.6);
        //<Количество досок на внутр двер проемы> = ОКРУГЛВВЕРХ((<Высота внутренних дверных проемов> + <Ширина внутренних дверных проемов>) * 2 * <Количество внутренних дверных проемов> / 3)
        this.quantityOfPlanksForInternalDoorways = Math.ceil( (this.heightOfInternalDoorway + this.widthOfInternalDoorway) * 2 * this.quantityOfInternalDoorways / 3);
        //<Итого количество досок на внутренние стены> = <Количество досок на внутренние стойки> + <Количество досок на внутренние проемы>
        this.quantityOfPlanksForInternalWalls = this.quantityOfPlanksForInternalStands + this.quantityOfPlanksForInternalDoorways;
        //<Ширина доски на внутренние стойки> = <Толщина внутренних стен>
        this.widthOfPlanksForInternalStands = this.thicknessOfInternalWall;
        //<Объем досок на внутренние стойки> = <Итого количество досок на внутренние стены> * <Ширина доски на внутренние стойки> / 1000 * 3 * 0,05
        this.volumeOfPlanksForInternalStands = this.quantityOfPlanksForInternalWalls * this.widthOfPlanksForInternalStands / 1000 * 3 * 0.05;       //толщина дб в мм
        this.priceOfPlanksForInternalStands = this.priceOfPlanksForInternalWallsInDB * this.volumeOfPlanksForInternalStands;
//        //<площадь ОСБ внутр> = <площадь внутренних стен> * 2 * 1,15
        if(addInternalWallTrim){
            this.osbAreaOfInternalWalls = this.areaOfInternalWalls * 2 * 1.15;
        } else {
            this.osbAreaOfInternalWalls = 0.0;
        }
        this.priceOfOsbForInternalWalls = this.priceOfOsbForInternalWallsInDB * this.osbAreaOfInternalWalls;
    }



    public void calculateFloorOverlaps(){
        if(!addOverlaps){return;}

        //<Кол-во балок перекрытий> = ОКРУГЛВВЕРХ(<Площадь снования> * 0,7)
        this.quantityOfBeamsOfOverlap = Math.ceil(this.areaOfBase * 0.7);
        //<Ширина доски на балки перекрытия> = <толщина перекрытия>
        this.widthOfPlanksForOverlapBeams = this.thicknessOfOverlap;
        //<Объем досок на перекрытия> = <Кол-во балок перекрытий> * 0,05 * <Ширина доски на балки перекрытия> / 1000 * 6
        this.volumeOfPlanksForOverlap = this.quantityOfBeamsOfOverlap * 0.05 * this.widthOfPlanksForOverlapBeams / 1000 * 6;    //ширина дб в мм
        this.priceOfPlanksForOverlaps = this.volumeOfPlanksForOverlap * this.priceOfPlanksForOverlapsInDB;


        //<площадь ОСБ> = <Площадь снования> * 1,15 * 2 * 2
        this.osbAreaForOverlap = this.areaOfBase * 1.15 * 2 * 2;
        this.priceOfOsbForOverlaps = this.osbAreaForOverlap * this.priceOfOsbForOverlapsInDB;

        //<Площадь парогидроизоляции> = <Площадь снования> * 1,15
        this.steamWaterProofingAreaForOverlap = this.areaOfBase * 1.15;
        this.priceOfSteamWaterProofingForOverlaps = this.steamWaterProofingAreaForOverlap * this.priceOfSteamWaterProofingForOverlapsInDB;
        //<Площадь ветрозащиты> = <Площадь снования> * 1,15
        this.windProtectionAreaForOverlap = this.areaOfBase * 1.15;
        this.priceOfWindProtectionForOverlaps = this.windProtectionAreaForOverlap * this.priceOfWindProtectionForOverlapsInDB;


        //<Площадь утеплителя перекрытия> = <Площадь снования> * 2 * 1,1
        this.insulationAreaForOverlap = this.areaOfBase * 2 * 1.1;
        //<Толщина утеплителя> = <толщина перекрытия>
        this.insulationThicknessForOverlap = this.thicknessOfOverlap;
        //<Объем утеплителя> = <Площадь утеплителя перекрытия> * <Толщина утеплителя> / 1000
        this.insulationVolumeForOverlap = this.insulationAreaForOverlap * this.insulationThicknessForOverlap / 1000;
        this.priceOfInsulationForOverlaps = this.insulationVolumeForOverlap * this.priceOfInsulationForOverlapsInDB;
        if (numberOfFloor == 1){
            this.priceOfOsbForOverlaps *= 2;
            this.priceOfSteamWaterProofingForOverlaps *= 2;
            this.windProtectionAreaForOverlap *= 2;
            this.priceOfInsulationForOverlaps *= 2;
        }
    }

    public void calculateFullFloorPrice(){
        this.fullPriceOfExternalWalls = this.priceOfPlanksForExternalWalls + this.priceOfOsbForExternalWalls + this.priceOfSteamWaterProofingOfExternalWalls + this.priceOfWindProtectionOfExternalWalls + this.priceOfInsulationOfExternalWalls;
        this.fullPriceOfInternalWalls = this.priceOfPlanksForInternalStands + this.priceOfOsbForInternalWalls;
        this.fullPriceOfOverlaps = this.priceOfPlanksForOverlaps + this.priceOfOsbForOverlaps + this.priceOfSteamWaterProofingForOverlaps + this.priceOfWindProtectionForOverlaps + this.priceOfInsulationForOverlaps;
        this.fullFloorPrice = this.priceOfPlanksForExternalWalls + this.priceOfOsbForExternalWalls + this.priceOfSteamWaterProofingOfExternalWalls + this.priceOfWindProtectionOfExternalWalls + this.priceOfInsulationOfExternalWalls +
                this.priceOfPlanksForInternalStands + this.priceOfOsbForInternalWalls +
                this.priceOfPlanksForOverlaps + this.priceOfOsbForOverlaps + this.priceOfSteamWaterProofingForOverlaps + this.priceOfWindProtectionForOverlaps + this.priceOfInsulationForOverlaps;

    }


    private void setOsbPrice(String fieldName, String type){
        try {
            switch (type) {
                case "OSB 9 мм":
                    setPrice(fieldName, 107);
                    break;
                case "OSB 10 мм":
                    setPrice(fieldName, 108);
                    break;
                case "OSB 15 мм":
                    setPrice(fieldName, 110);
                    break;
                case "OSB 18 мм":
                    setPrice(fieldName, 109);
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
        } catch (Exception e){
            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.setDouble(this, 0);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                e.printStackTrace();
            }
        }
    }

    private void setInsulationPrice(String fieldName, String type){
        try {
            switch (type) {
                case "Кнауф ТеплоКнауф 100 мм":
                    setPrice(fieldName, 111);
                    break;
                case "Технониколь 100 мм":
                    setPrice(fieldName, 112);
                    break;
                case "Эковер 100 мм":
                    setPrice(fieldName, 117);
                    break;
                case "Эковер 150 мм":
                    setPrice(fieldName, 113);
                    break;
                case "Эковер 200 мм":
                    setPrice(fieldName, 114);
                    break;
                case "Эковер 250 мм":
                    setPrice(fieldName, 115);
                    break;
                case "Фасад 200 мм":
                    setPrice(fieldName, 116);
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
        } catch (Exception e){
            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.setDouble(this, 0);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                e.printStackTrace();
            }
        }
    }

    private void setSteamWaterProofingPrice(String fieldName, String type){
        try {
            switch (type) {
                case "Ондутис":
                    setPrice(fieldName, 118);
                    break;
                case "Пароизоляция Axton (b)":
                    setPrice(fieldName, 119);
                    break;
                case "Пароизоляционная пленка Ютафол Н 96 Сильвер":
                    setPrice(fieldName, 120);
                    break;
                case "Пароизоляция В":
                    setPrice(fieldName, 121);
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
        } catch (Exception e){
            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.setDouble(this, 0);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                e.printStackTrace();
            }
        }
    }

    private void setWindProtectionPrice(String fieldName, String type){
        try {
            switch (type) {
                case "Ветро-влагозащитная мембрана Brane А":
                    setPrice(fieldName, 122);
                    break;
                case "Паропроницаемая ветро-влагозащита A Optima":
                    setPrice(fieldName, 123);
                    break;
                case "Гидро-ветрозащита Тип А":
                    setPrice(fieldName, 124);
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
        } catch (Exception e){
            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.setDouble(this, 0);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                e.printStackTrace();
            }
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
            // Извлекаем значение
            double value = jsonNode.get( String.valueOf(ID)).get("price").asDouble();
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
