package com.orhanobut.loggersample;

/**
 * @author Orhan Obut
 */
public class Dummy {

    static final String JSON_WITH_LINE_BREAK = "{\"widget\": {\n" +
            "    \"debug\": \"on\",\n" +
            "    \"window\": {\n" +
            "        \"title\": \"Sample Konfabulator Widget\",\n" +
            "        \"name\": \"main_window\",\n" +
            "        \"width\": 500,\n" +
            "        \"height\": 500\n" +
            "    },\n" +
            "    \"image\": { \n" +
            "        \"src\": \"Images/Sun.png\",\n" +
            "        \"name\": \"sun1\",\n" +
            "        \"hOffset\": 250,\n" +
            "        \"vOffset\": 250,\n" +
            "        \"alignment\": \"center\"\n" +
            "    },\n" +
            "    \"text\": {\n" +
            "        \"data\": \"Click Here\",\n" +
            "        \"size\": 36,\n" +
            "        \"style\": \"bold\",\n" +
            "        \"name\": \"text1\",\n" +
            "        \"hOffset\": 250,\n" +
            "        \"vOffset\": 100,\n" +
            "        \"alignment\": \"center\",\n" +
            "        \"onMouseUp\": \"sun1.opacity = (sun1.opacity / 100) * 90;\"\n" +
            "    }\n" +
            "}} ";

    static final String JSON_WITH_NO_LINE_BREAK = "{\"widget\": {" +
            "    \"debug\": \"on\"," +
            "    \"window\": {" +
            "        \"title\": \"Sample Konfabulator Widget\"," +
            "        \"name\": \"main_window\"," +
            "        \"width\": 500," +
            "        \"height\": 500" +
            "    },\n" +
            "    \"image\": { " +
            "        \"src\": \"Images/Sun.png\"," +
            "        \"name\": \"sun1\"," +
            "        \"hOffset\": 250," +
            "        \"vOffset\": 250," +
            "        \"alignment\": \"center\"" +
            "    },\n" +
            "    \"text\": {" +
            "        \"data\": \"Click Here\"," +
            "        \"size\": 36," +
            "        \"style\": \"bold\"," +
            "        \"name\": \"text1\"," +
            "        \"hOffset\": 250," +
            "        \"vOffset\": 100," +
            "        \"alignment\": \"center\"," +
            "        \"onMouseUp\": \"sun1.opacity = (sun1.opacity / 100) * 90;\"" +
            "    }" +
            "}}    ";

    static final String SMALL_SON_WITH_NO_LINE_BREAK = "{\"widget\": {" +
            "    \"debug\": \"on\"," +
            "    \"window\": {" +
            "        \"title\": \"Sample Konfabulator Widget\"," +
            "        \"name\": \"main_window\"," +
            "        \"width\": 500," +
            "        \"height\": 500" +
            "    }" +
            "}}    ";
}
