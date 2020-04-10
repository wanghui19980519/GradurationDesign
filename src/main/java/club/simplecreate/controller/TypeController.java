package club.simplecreate.controller;

import club.simplecreate.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/type")
public class TypeController {
    @Autowired
    TypeService typeService;

     @ResponseBody
     @RequestMapping("/getTypeList")
    public List<Object> getTypeList(){
        return typeService.getTypeList();
    }
}
