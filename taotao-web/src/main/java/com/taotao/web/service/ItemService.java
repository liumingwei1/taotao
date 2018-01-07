package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.web.bean.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemService {
    @Autowired
    private ApiService apiService;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;
    @Autowired
    private RedisService  redisService;
    private static final String REDIS_KEY = "TAOTAO_WEB_ITEM_DETAIL";
    private static final Integer REDIS_TIME= 60 * 60 * 24;
    //根据商品id查询商品数据
    public Item queryById(Long itemId) {
        // 从缓存中命中
        String key = REDIS_KEY+itemId;
        try {
            String cachDate = this.redisService.get(key);
            if (StringUtils.isNotBlank(cachDate)){
                return MAPPER.readValue(cachDate,Item.class);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = TAOTAO_MANAGE_URL+"/rest/api/item/"+itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)){
                return null;
            }
            try {
                // 将数据写入到缓存中
                this.redisService.set(key,jsonData,REDIS_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将json数据反序列为Item对象
            return MAPPER.readValue(jsonData,Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*查询商品描述
    *
    * */
    public ItemDesc queryDescById(Long itemId) {
            String url = TAOTAO_MANAGE_URL+"/rest/api/item/desc/"+itemId;
            try {
                String jsonData = this.apiService.doGet(url);
                if (StringUtils.isEmpty(jsonData)){
                    return null;
                }
                //将json数据反序列为Item对象
                return MAPPER.readValue(jsonData,ItemDesc.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    public String queryItemParamItemByItemId(Long itemId) {
        String url = TAOTAO_MANAGE_URL+"/rest/api/item/param/item"+itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)){
                return null;
            }
            //将json数据反序列
            ItemParamItem itemParamItem = MAPPER.readValue(jsonData,ItemParamItem.class);
            String paramData = itemParamItem.getParamData();
            //解析json
            ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(paramData);
            //stringBuilder放到方法内部不会有线程安全问题，放到全局变量的话就有线程安全问题
            StringBuilder sb = new StringBuilder();
            sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");

            for(JsonNode param:arrayNode){
                sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">"+param.get("group").asText()
                        +"</th></tr>");
                ArrayNode params = (ArrayNode) param.get("params");
                for(JsonNode p : params){
                    sb.append("<tr><td class=\"tdTitle\">"+p.get("k").asText()+"/td><td>"
                            +p.get("v").asText()+"</td></tr>");
                }
            }
            sb.append("</tbody></table>");
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
