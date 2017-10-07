package com.hyurumi.fb_bot_boilerplate;

import com.google.gson.Gson;
import com.hyurumi.fb_bot_boilerplate.models.common.Recipient;
import com.hyurumi.fb_bot_boilerplate.models.send.Message;
import com.hyurumi.fb_bot_boilerplate.models.send.MessageWrapper;
import com.hyurumi.fb_bot_boilerplate.models.send.Response;
import com.ksa.myanmarlottery.controller.ResultController;
import com.ksa.myanmarlottery.util.ConstantUtil;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class FBMessageSender {
    
    private Gson gson;
    private OkHttpClient httpClient;
    private String accessToken;
    private String endPoint;

    private Log log = LogFactory.getLog(FBMessageSender.class);
    
    public FBMessageSender() {}

    public FBMessageSender(Gson gson, OkHttpClient httpClient, String accessToken, String endPoint) {
        this.gson = gson;
        this.httpClient = httpClient;
        this.accessToken = accessToken;
        this.endPoint = endPoint;
    }
    
    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
    
    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    public Response sendWelcomeMessage(String welcomeMessage) throws IOException {
        log.debug("before sent "+ welcomeMessage);
        
        Request request = new Request.Builder()
                .url("https://graph.facebook.com/v2.6/me/thread_settings" + "?access_token=" + accessToken)
                .header("Content-Type", "application/json; charset=UTF-8")
                .post(RequestBody.create(ConstantUtil.JSON, welcomeMessage))
                .build();
        okhttp3.Response response = httpClient.newCall(request).execute();
        final int code = response.code();
        Response returnValue = null;
        if (code == 200) {
            log.info("Facebook response " + 200);
            returnValue = gson.fromJson(response.body().string(), Response.class);
        }else {
            System.out.println("ERROR: " + response.body().string());
        }
        response.body().close();
        return returnValue;
    }
    
    public Response sendActionTo(String recipientId, String senderAction) throws IOException {
        MessageWrapper mw = new MessageWrapper(recipientId, senderAction);
        log.debug("before sent "+ gson.toJson(mw));
        log.info("sendActionTo " + recipientId);
        return sendTo(RequestBody.create(ConstantUtil.JSON, gson.toJson(mw)));
    }

    public Response sendMessageTo(String recipientId, Message message) throws IOException {
        MessageWrapper mw = new MessageWrapper(recipientId, message);
        log.debug("before sent "+ gson.toJson(mw));
        log.info("sendMessageTo " + recipientId);
        return sendTo(RequestBody.create(ConstantUtil.JSON, gson.toJson(mw)));
    }
    
    private Response sendTo(RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(endPoint + "?access_token=" + accessToken)
                .header("Content-Type", "application/json; charset=UTF-8")
                .post(body)
                .build();
        okhttp3.Response response = httpClient.newCall(request).execute();
        final int code = response.code();
        Response returnValue = null;
        if (code == 200) {
            log.info("Facebook response 200");
            // no need to convert cos I don't need it.
//            returnValue = gson.fromJson(response.body().string(), Response.class);
        }else {
            System.out.println("ERROR: " + response.body().string());
        }
        response.body().close();
        return returnValue;
    }

//    class MessageWrapper {
//        private final Recipient recipient;
//        private final Message message;
//        MessageWrapper(String recipientId, Message message) {
//            this.recipient = new Recipient(recipientId);
//            this.message = message;
//        }
//    }
}
