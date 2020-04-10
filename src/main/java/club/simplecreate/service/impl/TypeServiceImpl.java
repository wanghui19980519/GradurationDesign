package club.simplecreate.service.impl;

import club.simplecreate.cache.TypeCache;
import club.simplecreate.dao.TypeMapper;
import club.simplecreate.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    TypeCache typeCache;
    @Autowired
    TypeMapper typeMapper;
    @Override
    public List<Object> getTypeList() {
        List<Object> res= typeCache.getTypeList();
        if(res.size()==0){
            //缓存中不存在，从数据库中获取
            res=typeMapper.getTypeList();
            System.out.println(res.size());
            typeCache.insertTypeList(res);
        }
        return res;
    }
}
