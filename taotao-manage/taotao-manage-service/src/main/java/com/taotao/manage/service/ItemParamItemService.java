package com.taotao.manage.service;

import com.github.abel533.entity.Example;
import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.pojo.ItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ItemParamItemService extends BaseService<ItemParamItem> {

        @Autowired
        private ItemParamItemMapper itemParamItemMapper;
    public Integer updateItemParamItem(Long itemId,String itemParams) {
        //更行数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setParamData(itemParams);
        itemParamItem.setUpdated(new Date());
        //更行的条件
        Example example = new Example(ItemParamItem.class);
        example.createCriteria().andEqualTo("itemId",itemId);

        return  this.itemParamItemMapper.updateByExampleSelective(itemParamItem,example);
    }
}
