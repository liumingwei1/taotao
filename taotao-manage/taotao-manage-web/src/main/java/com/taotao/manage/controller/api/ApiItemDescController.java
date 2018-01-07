package com.taotao.manage.controller.api;


import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("api/item/")
public class ApiItemDescController {
    @Autowired
    private ItemDescService itemDescService;
    /*
     * 根据商品id查询商品数据
    *
     */

//    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    @RequestMapping(value="desc/{itemId}",method = RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryById(@PathVariable("itemId") Long itemId
    ) {
        try {
            ItemDesc itemDesc = this.itemDescService.queryById(itemId);
            if (null == itemDesc) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

//@Controller
//@RequestMapping("api/item/")
//public class ApiItemDescController {
//    @Autowired
//    private ItemDescService itemDescService;
//
//
//    @RequestMapping(value="desc/{itemId}",method = RequestMethod.GET)
//    public ResponseEntity<ItemDesc> queryById(@PathVariable("itemId" ) Long itemId){
//        try {
//            ItemDesc itemDesc = this.itemDescService.queryById(itemId);
//            if(null==itemDesc){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//            return ResponseEntity.ok(itemDesc);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//    }
//}
