package club.simplecreate.controller;


import club.simplecreate.cache.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;



/**
 * @ServerEndpoint 这个注解有什么作用？
 * 这个注解用于标识作用在类上，它的主要功能是把当前类标识成一个WebSocket的服务端
 * 注解的值用户客户端连接访问的URL地址
 */
@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocket {

    private static TokenCache tokenCache;

    @Autowired
    public void setChatService(TokenCache tokenCache) {
        WebSocket.tokenCache = tokenCache;
    }
    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 标识当前连接客户端的用户名
     */
    private String openId;

    /**
     *  用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    public static ConcurrentHashMap<String,WebSocket> webSocketSet = new ConcurrentHashMap<>();


    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "token") String token){

        this.session = session;
        String openid=tokenCache.getToken(token);

        //不存在则失败
        if(openid==null) {
            //返回错误信息
            return ;
        }
        //存在则通过,并且将openid加入request
        else {
            this.openId=openid;
        }
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        webSocketSet.put(openid,this);
    }
    @OnClose
    public void OnClose(){
        webSocketSet.remove(this.openId);
    }

    public void sendMessage(int newMessageNum){
        if(session.isOpen()) {
            session.getAsyncRemote().sendObject(newMessageNum);
        }
    }
}
