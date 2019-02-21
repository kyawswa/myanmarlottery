package com.hyurumi.fb_bot_boilerplate;

import com.google.gson.Gson;
import com.hyurumi.fb_bot_boilerplate.models.common.Action;
import com.hyurumi.fb_bot_boilerplate.models.send.Button;
import com.hyurumi.fb_bot_boilerplate.models.send.Element;
import com.hyurumi.fb_bot_boilerplate.models.send.Message;
import com.hyurumi.fb_bot_boilerplate.models.send.QuickReply;
import com.hyurumi.fb_bot_boilerplate.models.webhook.Messaging;
import com.hyurumi.fb_bot_boilerplate.models.webhook.ReceivedMessage;
import okhttp3.*;

import java.util.List;
import java.util.Random;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.port;

public class KsaMain {
    public static  String sAccessToken;
    private static String sValidationToken;
    public static final String END_POINT;
    public static final MediaType JSON;
    private static final Random sRandom;
    private static final Gson GSON;
    private static final String PORT = "8181";

    static {
        JSON = MediaType.parse("application/json; charset=utf-8");
        END_POINT = "https://graph.facebook.com/v2.6/me/messages";
        GSON = new Gson();
        sRandom = new Random();
        sAccessToken = "EAAC6CoQsmOQBALDgvNuFAuEytTOZBjrUJVUodqGZBF620fInZByyBDqfx4A6XvDgnxIgXGZAX1nv2WJJSKVxolgPeNF2kdbGqvdp9h6wtGMCwecryr5bL4G7SPPbBWmbl1kWruiPIWJkdoed9cdgTyO5BrfZAzgWIdsmFK9dZCWgZDZD";//System.getenv("ACCESS_TOKEN");
        sValidationToken = "EAAC6CoQsmOQBALDgvNuFAuEytTOZBjrUJVUodqGZBF620fInZByyBDqfx4A6XvDgnxIgXGZAX1nv2WJJSKVxolgPeNF2kdbGqvdp9h6wtGMCwecryr5bL4G7SPPbBWmbl1kWruiPIWJkdoed9cdgTyO5BrfZAzgWIdsmFK9dZCWgZDZD";//System.getenv("VALIDATION_TOKEN");
    }

    public static void main(String[] args) {

        port(Integer.valueOf(PORT)); //System.getenv("PORT")

        get("/webhook", (request, response) -> {
            if (request.queryMap("hub.verify_token").value().equals(sValidationToken)) {
                return request.queryMap("hub.challenge").value();
            }
            return "Error, wrong validation token";
        });

        post("/webhook", (request, response) -> {
            ReceivedMessage receivedMessage = GSON.fromJson(request.body(), ReceivedMessage.class);
            List<Messaging> messagings = receivedMessage.entry.get(0).messaging;
            for (Messaging messaging : messagings) {
                String senderId = messaging.sender.id;
                if (messaging.message !=null) {
                    if (messaging.message.text != null) {
                        String param = messaging.message.text;
                        System.out.println("Param : "+ param);
                        switch(param) {
                            case "10,000":
                                Message.Text("You choose 10,000.").sendTo(senderId);
                                break;
                            case "1500":
                                Message.Text("You choose 1500.").sendTo(senderId);
                                break;
                            default:
                                String[] arr = param.split("-");
                                if(arr.length == 2) {
                                    Message.QuickReply("Pick up One.", 
                                            new QuickReply("text", "10,000", "10,000", null),
                                            new QuickReply("text", "1500", "1500", null))
                                            .sendTo(senderId);
                                } else {
                                    Message.Text("your text is worng format.").sendTo(senderId);
                                }
                                break;
                        }
                    }
                    
                    
                } else if (messaging.postback != null) {
                    // Receiving postback message
                    if (messaging.postback.payload == Action.ACTION_A) {
                        Message.Text("Action A").sendTo(senderId);
                    }else {
                        Message.Text("Action B").sendTo(senderId);
                    }
                } else if (messaging.delivery != null) {
                    // when the message is delivered, this webhook will be triggered.
                } else {
                    // sticker may not be supported for now.
                    System.out.println(request.body());
                }
            }
            System.out.println(request.body());
            return "";
        });
    }

    static private void sendSamplePostBackMessage(String senderId) throws Exception {
        Message message = Message.Button("This is a postback message; please choose the action below");
        message.addButton(Button.Postback("action A", Action.ACTION_A));
        message.addButton(Button.Postback("action B", Action.ACTION_B));
        message.addButton(Button.Url("open Google", "https://google.com"));
        message.sendTo(senderId);
    }

    static private void sendSampleGenericMessage(String senderId) throws Exception {
        Message message = Message.Generic();
        Element element = new Element("Generic Message Sample", "https://unsplash.it/764/400?image=400", "subtitle");
        message.addElement(element);
        element = new Element("Yay Yay", "https://unsplash.it/764/400?image=500", "subtitle");
        element.addButton(Button.Postback("action A", Action.ACTION_A));
        element.addButton(Button.Url("jump to FB", "https://facebook.com/"));
        message.addElement(element);
        message.sendTo(senderId);
    }
}
