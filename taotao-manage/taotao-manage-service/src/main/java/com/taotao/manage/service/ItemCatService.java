package com.taotao.manage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatData;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.pojo.ItemCat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ItemCatService extends BaseService<ItemCat> {
    /*
        @Autowired
       private ItemCatMapper itemCatMapper;
      *//* public List<ItemCat> queryItemCat(Long pid){
       ItemCat record = new ItemCat();
     record.setIsParent(pid);
       return this.itemCatMapper.select(record);
   }*//*


    *//*@Override
    public Mapper<ItemCat> getMapper() {
        return this.itemCatMapper;
    }*/
    @Autowired
    private RedisServiceOld redisService;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /*
    * 全部查询，并且生成树桩结构
    * */
    public ItemCatResult queryAllToTree() {
        ItemCatResult result = new ItemCatResult();
        // 全部查出，并且在内存中生成树形结构
        List<ItemCat> cats = super.queryAll();
        //先从缓存中命中，如果命中就返回，没有命中继续执行
        String key = "TAOTAO_MANAGE_ITEM_CAT_API";//规则：项目-模块名-业务名
        String cachDate = this.redisService.get(key);
        if (StringUtils.isNotBlank(cachDate)) {
            //命中
            try {
                return MAPPER.readValue(cachDate, ItemCatResult.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 转为map存储，key为父节点ID，value为数据集合
        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
        for (ItemCat itemCat : cats) {
            if (!itemCatMap.containsKey(itemCat.getParentId())) {
                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            itemCatMap.get(itemCat.getParentId()).add(itemCat);
        }

        // 封装一级对象
        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
        for (ItemCat itemCat : itemCatList1) {
            ItemCatData itemCatData = new ItemCatData();
            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
            itemCatData.setNname("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
            result.getItemCats().add(itemCatData);
            if (!itemCat.getIsParent()) {
                continue;
            }

            // 封装二级对象
            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
            List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
            itemCatData.setItems(itemCatData2);
            for (ItemCat itemCat2 : itemCatList2) {
                ItemCatData id2 = new ItemCatData();
                id2.setNname(itemCat2.getName());
                id2.setUrl("/products/" + itemCat2.getId() + ".html");
                itemCatData2.add(id2);
                if (itemCat2.getIsParent()) {
                    // 封装三级对象
                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
                    List<String> itemCatData3 = new ArrayList<String>();
                    id2.setItems(itemCatData3);
                    for (ItemCat itemCat3 : itemCatList3) {
                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size() >= 14) {
                break;
            }
        }
        try {
            //将数据库查询结果集写入的到缓存中
            this.redisService.set(key, MAPPER.writeValueAsString(result), 60 * 60 * 24 * 30 * 3);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
