package com.taotao.manage.controller;

import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@RequestMapping("item/cat")
@Controller
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;
//    用于战线商品
    @RequestMapping(method = RequestMethod.GET )
    public ResponseEntity<List<ItemCat>> queryItemCat(@RequestParam(value = "id",defaultValue = "0") Long pId){

        try {
           ItemCat record = new ItemCat();
           record.setParentId(pId);
          List<ItemCat> itemCats = this.itemCatService.queryListByWhere(record);
            if (itemCats ==null|| itemCats.isEmpty()){
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemCats);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
