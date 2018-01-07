package com.taotao.manage.controller.api;

import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.manage.service.ItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("api/item/param/item")
@Controller
public class ApiItemParamItemController {
    /*
    * 根据商品id查询规格参数数据
    * */
    @Autowired
    private ItemParamItemService itemParamItemService;
    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryByItemId(@PathVariable("itemId") Long itemId){
        try {
            ItemParamItem record  = new ItemParamItem();
            record.setItemId(itemId);
            ItemParamItem itemParamItem = this.itemParamItemService.queryOne(record);
            if (null == itemParamItem){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemParamItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
