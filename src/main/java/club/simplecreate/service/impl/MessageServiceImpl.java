package club.simplecreate.service.impl;

import club.simplecreate.cache.MessageCache;
import club.simplecreate.pojo.Message;
import club.simplecreate.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageCache messageCache;

    @Override
    public Map<String, Object> getFollowMessage(String openId) {
       
        long totalNums=messageCache.getFollowMessageSize(openId);
        List<Object> messages=messageCache.getFollowMessage(openId);
        return  deal(totalNums,messages);
    }

    private long getPage(long size) {
        long pages=size/10;
        if(size%10!=0){
            pages+=1;
        }
        return  pages;
    }
    private Map<String, Object> deal(long totalNums,List<Object> messages){
        Map<String, Object> res=new HashMap<>(3);
        long totalPages=getPage(totalNums);
        res.put("totalNums",totalNums);
        res.put("rows",messages);
        res.put("totalPages",totalPages);
        return res;
    }
    @Override
    public Map<String, Object> getCommentMessage(String openId) {
        long totalNums=messageCache.getCommentMessageSize(openId);
        List<Object> messages=messageCache.getCommentMessage(openId);
        return  deal(totalNums,messages);
    }

    @Override
    public Map<String, Object> getLikeMessage(String openId) {
        long totalNums=messageCache.getLikeMessageSize(openId);
        List<Object> messages=messageCache.getLikeMessage(openId);
        return  deal(totalNums,messages);
    }

    @Override
    public void commentMessageAllRead(String openId,int num) {
        messageCache.commentMessageAllRead(openId,num);
    }

    @Override
    public void likeMessageAllRead(String openId,int num) {
        messageCache.likeMessageAllRead(openId,num);
    }

    @Override
    public void followMessageAllRead(String openId,int num) {
        messageCache.followMessageAllRead(openId,num);
    }

    @Override
    public void clearNewMessageNums(String openId) {
        messageCache.clearNewMessageNums(openId);
    }


    @Override
    public void readFollowMessageByIndex(String openId,  int index) {
        messageCache.readFollowMessageByIndex(openId,index);
    }

    @Override
    public void readCommentMessageByIndex(String openId, int index) {
        messageCache.readCommentMessageByIndex(openId,index);
    }

    @Override
    public void readLikeMessageByIndex(String openId,  int index) {
        messageCache.readLikeMessageByIndex(openId,index);
    }

    @Override
    public int getNewMessageNum(String openId) {
        return messageCache.getNewMessageNum(openId);
    }

}
