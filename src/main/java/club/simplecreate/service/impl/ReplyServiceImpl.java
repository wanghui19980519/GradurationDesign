package club.simplecreate.service.impl;

import club.simplecreate.cache.ReplyCache;
import club.simplecreate.dao.ReplyMapper;
import club.simplecreate.pojo.Reply;
import club.simplecreate.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    ReplyCache replyCache;
    @Autowired
    ReplyMapper replyMapper;
    @Override
    public boolean insertReply(Reply reply) {
        //加入数据库
        if(replyMapper.insertReply(reply)==1){
            //加入缓存
            replyCache.insertReply(reply);
            return true;
        }else{
            return false;
        }
    }
}
