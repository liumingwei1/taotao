package com.taotao.manage.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.service.ItemCatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("api/item/cat")
@Controller
public class ApiItemCatController {
    @Autowired
    private ItemCatService itemCatService;
    private static  final ObjectMapper MAPPER = new ObjectMapper();
    /*
    * 对外提供接口服务，查询所有类目数据
    * */
   /* @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> queryItemCatList(@RequestParam(value = "callback",required = false) String callback){
        try {
            ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
            String json = MAPPER.writeValueAsString(itemCatResult);
            if (StringUtils.isEmpty(callback)){
                return ResponseEntity.ok(json);
            }
            return ResponseEntity.ok(callback +"("+json+");");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }*/
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCatList(@RequestParam(value = "callback",required = false) String callback){
        try {
            ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
                return ResponseEntity.ok(itemCatResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
