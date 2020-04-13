package club.simplecreate.controller;

import club.simplecreate.cache.CosCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cos")
public class CosController {
    @Autowired
    private CosCache cosCache;

    @ResponseBody
    @RequestMapping("/getCosSecret")
    public Map<String,String> getCosSecret(){
        Map<String,String> res=new HashMap<>(2);
        String secretId=cosCache.getSecretId();
        String secretKey=cosCache.getSecretKey();
        res.put("COS_SecretId",secretId);
        res.put("COS_SecretKey",secretKey);
        return res;
    }
}
