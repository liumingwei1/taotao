package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Content;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexService {
    @Autowired
    private  ApiService apiService;
    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;
    @Value("${INDEX_ADl_URL}")
    private String INDEX_ADl_URL;
    @Value("${INDEX_AD2_URL}")
    private String INDEX_AD2_URL;
    private static final ObjectMapper MAPPER = new ObjectMapper();

        public String queryIndexADl() {
            try {
                String url = TAOTAO_MANAGE_URL + INDEX_ADl_URL ;
                String jsonDate = this.apiService.doGet(url);
                if (StringUtils.isEmpty(jsonDate)){
                    return null;
                }
                //解析json数据，封装成前端所需的结构
                EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonDate, Content.class);
                List<Content> contents = (List<Content>) easyUIResult.getRows();
                List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
                for (Content content : contents){
                    Map<String,Object> map = new LinkedHashMap<String, Object>();
                    map.put("srcB",content.getPic());
                    map.put("height",240);
                    map.put("alt",content.getTitle());
                    map.put("width",670);
                    map.put("src",content.getPic());
                    map.put("widthB",550);
                    map.put("href",content.getUrl());
                    map.put("heightB",240);
                    result.add(map);
                }
                return MAPPER.writeValueAsString(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    public String queryIndexAD2() {
        try {
            String url = TAOTAO_MANAGE_URL + INDEX_AD2_URL ;
            String jsonDate = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonDate)){
                return null;
            }
            //解析json数据，封装成前端所需的结构
            JsonNode jsonNode = MAPPER.readTree(jsonDate);
            ArrayNode rows = (ArrayNode) jsonNode.get("rows");
            List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
            for (JsonNode row : rows){
                Map<String,Object> map = new LinkedHashMap<String, Object>();
                map.put("width",310);
                map.put("height",70);
                map.put("src",row.get("pic").asText());
                map.put("href",row.get("url").asText());
                map.put("alt",row.get("pic").asText());
                map.put("widthB",210);
                map.put("heightB",70);
                map.put("srcB",row.get("pic").asText());
                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}