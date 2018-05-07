package com.laf.mall.api.repository;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JsonPathTest {

    @Test
    public void testJson() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//
//        Map<String, String> testObject = new HashMap<>();
//        testObject.put("name", "laf");
//        testObject.put("age", "12");
//        testObject.put("sex", "1");
//
//        String jsonInString = mapper.writeValueAsString(testObject);
//
//        log.info(jsonInString);

//        String json = "{\n" +
//                "    \"store\": {\n" +
//                "        \"book\": [\n" +
//                "            {\n" +
//                "                \"category\": \"reference\",\n" +
//                "                \"author\": \"Nigel Rees\",\n" +
//                "                \"title\": \"Sayings of the Century\",\n" +
//                "                \"price\": 8.95\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"category\": \"fiction\",\n" +
//                "                \"author\": \"Evelyn Waugh\",\n" +
//                "                \"title\": \"Sword of Honour\",\n" +
//                "                \"price\": 12.99\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"category\": \"fiction\",\n" +
//                "                \"author\": \"Herman Melville\",\n" +
//                "                \"title\": \"Moby Dick\",\n" +
//                "                \"isbn\": \"0-553-21311-3\",\n" +
//                "                \"price\": 8.99\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"category\": \"fiction\",\n" +
//                "                \"author\": \"J. R. R. Tolkien\",\n" +
//                "                \"title\": \"The Lord of the Rings\",\n" +
//                "                \"isbn\": \"0-395-19395-8\",\n" +
//                "                \"price\": 22.99\n" +
//                "            }\n" +
//                "        ],\n" +
//                "        \"bicycle\": {\n" +
//                "            \"color\": \"red\",\n" +
//                "            \"price\": 19.95\n" +
//                "        }\n" +
//                "    },\n" +
//                "    \"expensive\": 10\n" +
//                "}";

        String json = "{\n" +
                "    \"industries\":\n" +
                "        [{\"industry_id\":1,\"industry_name\":\"餐饮\"},{\"industry_id\":2,\"industry_name\":\"服饰\"}],\n" +
                "    \"shops\":\n" +
                "        [{\"shop_id\":\"1\",\"shop_name\":\"kfc\"},{\"shop_id\":\"2\",\"shop_name\":\"m\"}]\n" +
                "}";
//        String author = ((net.minidev.json.JSONArray)JsonPath.read(json, "$..book.length()")).size()+"";
//        String author = ((net.minidev.json.JSONArray)JsonPath.parse(json).read("$..industries")).size()+"";
//        log.info(author);

//        TypeRef<List<Map<String, Object>>> typeRef = new TypeRef<List<Map<String, Object>>>() {};

        List<Map<String, Object>> list = (List<Map<String, Object>>) JsonPath.parse(json).read("$.industries", List.class);

        if (list == null) {
            log.info("null");
        } else {
            log.info("yes");
        }

//        log.info(list.get(1).get("industry_name")+"");
    }
}
