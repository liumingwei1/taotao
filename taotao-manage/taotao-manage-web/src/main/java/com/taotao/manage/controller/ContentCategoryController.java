package com.taotao.manage.controller;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("content/category")
@Controller
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    /*
    * 根据父节点id查询内容分类列表
    * */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryListByParentId(
            @RequestParam(value = "id", defaultValue = "0")Long pid){
        try {
            ContentCategory record = new ContentCategory();
            //record.setIsParent(pid);
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
            if (null ==list|| list.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    //新增节点
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContenCategory(ContentCategory contentCategory){
        try {
            this.contentCategoryService.saveContentCategory(contentCategory);
            //判断该节点的父节点的isparent是否为true，不是，需要修改
            return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    /*
    * 重命名
    * */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> rename(
            @RequestParam("id") Long id,@RequestParam("name")String name){
        try {
            ContentCategory category = new ContentCategory();
            category.setId(id);
            category.setName(name);
            this.contentCategoryService.updateSelective(category);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    /*
    * 删除节点，包含他的所有子节点都被删除
    * */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(ContentCategory contentCategory){
        try {
            this.contentCategoryService.deleteAll(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
