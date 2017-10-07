/** 
 * The MIT License
 *
 * Copyright 2017 kyawswaraung
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ksa.myanmarlottery.service.fb;

import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import com.hyurumi.fb_bot_boilerplate.FBMessageSender;
import com.hyurumi.fb_bot_boilerplate.models.send.Button;
import com.hyurumi.fb_bot_boilerplate.models.send.Message;
import com.hyurumi.fb_bot_boilerplate.models.send.QuickReply;
import com.hyurumi.fb_bot_boilerplate.models.webhook.Attachment;
import com.hyurumi.fb_bot_boilerplate.models.webhook.Messaging;
import com.hyurumi.fb_bot_boilerplate.models.webhook.ReceivedMessage;
import com.ksa.myanmarlottery.dto.GetPrizeDTO;
import com.ksa.myanmarlottery.dto.ItemDTO;
import com.ksa.myanmarlottery.model.LogInfo;
import com.ksa.myanmarlottery.model.Result;
import com.ksa.myanmarlottery.model.ScheduleItem;
import com.ksa.myanmarlottery.repository.LogRepository;
import com.ksa.myanmarlottery.service.parser.CSVFileParser;
import com.ksa.myanmarlottery.service.ResultService;
import com.ksa.myanmarlottery.service.ScheduleService;
import com.ksa.myanmarlottery.service.parser.ExcelFileParser;
import com.ksa.myanmarlottery.util.AppMessageHandler;
import com.ksa.myanmarlottery.util.ConstantUtil;
import com.ksa.myanmarlottery.util.ResultBox;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Service;

/**
 *
 * @author Kyawswar
 */
@Service
@PropertySource({"classpath:application.properties"})
public class FBResultServiceImpl implements FBResultService {

    @Autowired
    private FBMessageSender messageSender;
    
    @Autowired
    private CSVFileParser csvParser;
    
    @Autowired
    private ExcelFileParser excelParser;
    
    @Autowired
    private ResultService resultService;
    
    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private AppMessageHandler messageHandler;
    
    @Autowired
    private ResultBox resultBox;
    
    @Autowired
    private Environment env;
    
    @Autowired
    private LogRepository logRepository;
    
    @Value("${authorized.senders}")
    private String[] authorizedSenders;

    private static final String[] HELP_COMMANDS = {"?","help", "HELP", "Help"};
    
    private Log log = LogFactory.getLog(FBResultServiceImpl.class);

    @PostConstruct
    public void init() {
        // https://developers.facebook.com/docs/messenger-platform/thread-settings/greeting-text
//        try {
//            // add greeting message to facebook page.
//            Response response = messageSender.sendWelcomeMessage(
//                    messageSender.getGson().toJson(
//                            new Welcome(
//                                    "greeting", new Greeting(messageHandler.get("default.welcome.message")
//                                    )
//                            )
//                    )
//            );
//        } catch (IOException ex) {
//            log.error(ex, ex);
//        }
    }
    
    /**
     * This method is entry point to handle all facebook request.
     * The developer need to know Facebook Send API and Webhook API knowledge.
     * 
     * @param body json string sent from Facebook.
     */
    @Override
    public void manageRequst(String body) {
        log.info("Manage Facebook Request..");
        // convert json string into Json Object.
        ReceivedMessage receivedMessage = messageSender.getGson().fromJson(body, ReceivedMessage.class);
        List<Messaging> messagings = receivedMessage.entry.get(0).messaging;
        try {
            for (Messaging messaging : messagings) {
                String senderId = messaging.sender.id;
                if (messaging.message !=null) {
                    
                    if (messaging.message.text != null) {
                        QuickReply quickReply = messaging.message.quick_reply;
                        if(quickReply != null) { // check request is quick reply
                            // @remove since 500 lottery ticket out.
                            //manageQuickReply(quickReply, senderId);
                            log.info("ManageQuickReply.. " + senderId);
                        } else { // normal input
                            // insert Log
                            logRepository.inserLog(new LogInfo(new Date(), senderId, ConstantUtil.FACEBOOK, null, null));
                            String param = messaging.message.text;
                            log.debug("Param : "+ param);
                            
                            // no mre need for performance reason.
                            // insertLog(senderId, "/result");
                            if(param != null) {
                                if("help".equalsIgnoreCase(param) || "?".equals(param)) { // check for help command or ?
                                    // read the data from message properties and send response message to Facebook
                                    messageSender.sendMessageTo(senderId, Message.Text(messageHandler.get("default.help.message")));
                                    log.info("Send Help reply "+ senderId);
                                } else {
                                    // Yep. the actual process starts here.
                                    manageQueryParam(param, senderId);
                                    log.info("manageQueryParam... "+ senderId);
                                }
                            }
                        }
                        // user send message as a attachment??
                    } else if(messaging.message.attachments != null ||
                            !messaging.message.attachments.isEmpty()) {
//                        if(senderId.equals(env.getProperty("sender.id"))) {
                        if(isAuthorizedSender(senderId)) { // check this attachment come from authorized sender id.
                            Attachment attachment = messaging.message.attachments.get(0);
                            log.info("manageUpLoad... "+ senderId);
                            if(attachment.type.FILE.equals(attachment.type)) {
                                log.info("manageUpLoad... "+ senderId);
                                URL url = new URL(attachment.payload.url);
                                String fileName = new File(url.getPath()).getName();
                                log.debug("Upload File Name: "+fileName);
                                String[] arg = fileName.split(".");
                                
                                String fileType = Files.getFileExtension(fileName);
                                // skip if file extension is other file type. accept only csv and excel file type
                                if(CSV_FILE_TYPE.equals(fileType) || EXCEL_FILE_TYPE.equals(fileType)) {
                                    String message = "Upload Successfully.";
//                                    boolean isSuccess = false;
                                    try {
                                        // try to insert into DB.
                                        message = insertUploadData(attachment.payload.url, fileType);
                                        // Setp 5 send notification for all registred lottery schedules.
                                        startAsycNotifyMessageTasks();
                                    } catch (Exception e) {
                                        log.error(e.getMessage(), e);
                                        message = "Upload Failed.\n"+e.getMessage();
                                    }
                                    log.info("Upload success message:" + message);
                                    messageSender.sendMessageTo(senderId, Message.Text(message));
                                }
                            }
                        }
                    }
                } else {
                    // sticker may not be supported for now.
                    log.debug("skip other events.");
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
    
    private final String lottery500 = "500"; // concat space
    @Deprecated
    private final String lottery200 = "200"; // deprecated now.
    private final String regCommand = "@"; // sign for lottery schedules registration.
    
    private void manageQueryParam(String param, String senderId) throws IOException {
        
        // remove space between comma
        param = param.replaceAll(",\\s|\\s,\\s", ",");

        // for lottery schedules registration
        boolean forNoti = false;
        String originParam = "";
        
        if(param.startsWith(regCommand)) { // check strat with @
            forNoti = true;
            param = param.substring(2); // remove noti command(@)
            originParam = param;
        }
        
        // classify lottery type
        int lotteryType = ConstantUtil.NEW_LOTTERY_TYPE;
        if(param.startsWith(lottery200)) {
            lotteryType = ConstantUtil.OLD_LOTTERY_TYPE;
            param = param.substring(4); // remove lottery type from param
        } else if(param.startsWith(lottery500)) {
            lotteryType = ConstantUtil.NEW_LOTTERY_TYPE;
            param = param.substring(4); // remove 500 from param(500 ka 123456)
        }
        
        // 0 is normal query(ka 123456,nya 123457)
        // 1 and 2 is find by range query (ka nya 123456 or ka 123456 123459)
        int queryType = 0;
        
        // #Step 1 split comma firstly.
        String[] arr = param.split(",");
        List<String> codePoints = new ArrayList<>();
        
        for(String str: arr) {
            
            // split the character and number by space. eg. nya 123456
            String[] content = str.split(" ");
            
            // this is find by range
            if(content.length == 3) { // If length is 3, it is sure for range search.
                queryType = ConstantUtil.getCodePointRangeType(content);
                if(queryType != 0) { // 0 is invalid
                    codePoints.add(content[0]); 
                    codePoints.add(content[1]); 
                    codePoints.add(content[2]); 
                }
                break; // only choose first part and skip the rest of all cos it is searched by range.
                
            } else if(content.length == 2) { // this is normal search(ka 123456,nya 123457)
                // skip if codepoint is not valid.
                if(ConstantUtil.isCodePointValid(content)) {
                    // to change myanmar number to english number, concat by '-'
                    // eg. ka-123456
                    codePoints.add(content[0]+"-"+content[1]);
                }
            }
        }
        
        // prepare for quick reply
        if(codePoints.size() > 0 || !codePoints.isEmpty()) {
            
            // for notification.
            if(forNoti) {
                // for scheduled lottery.
                replyForNotiMessage(lotteryType, codePoints, senderId, queryType, originParam);
            } else { // normal process.
                // reply to sender.
                replyMessage(lotteryType, codePoints, senderId, queryType);
            }
            
        } else {
            // send error message
            messageSender.sendMessageTo(senderId, Message.Text(messageHandler.get("default.format.error")));
        }
    }
    
    /**
     * Check this sender id is already included the list of authorized.senders 
     * in application.properties
     * 
     * @param senderID
     * @return 
     */
    private boolean isAuthorizedSender(String senderID) {
        log.info("Check authorized sender:"+senderID);
        for(String str : authorizedSenders) {
            log.info("Check authorized sender:"+senderID.equals(str));
            if(senderID.equals(str)) return true;
        }
        return false;
    }
    
    /**
     * reply for schedule registration success or not.
     * 
     * @param lotteryType
     * @param codePoints
     * @param senderId
     * @param queryType
     * @param originParam
     * @throws IOException 
     */
    private void replyForNotiMessage(int lotteryType, List<String> codePoints, String senderId, int queryType, String originParam) throws IOException {
        log.info("replyForNotiMessage lotteryType: " + lotteryType + " senderId: " + senderId + " queryType: " + queryType);
        
        // send typing_on action.
        messageSender.sendActionTo(senderId, ConstantUtil.ACTION_TYPING_ON);
        
        ScheduleItem item = new ScheduleItem(senderId, lotteryType, queryType, originParam);
        item.setParam(codePoints);
        String replyMsg = messageHandler.get("default.noti.success.message", new Object[]{item.getOriginParam()});
        try {
            // save schedule item.
            scheduleService.insertOrUpdateItem(item);
        } catch (Exception e) {
            e.printStackTrace();
            replyMsg = messageHandler.get("default.noti.failed.message", new Object[]{item.getOriginParam()});
        }
        // send result message.
        messageSender.sendMessageTo(senderId, Message.Text(replyMsg));
    }
    
    /**
     * Try to reply message based on query type and code points(character+numbers).
     * 
     * @param lotteryType
     * @param codePoints
     * @param senderId
     * @param queryType
     * @throws IOException 
     */
    private void replyMessage(int lotteryType, List<String> codePoints, String senderId, int queryType) throws IOException {
        log.info("replyMessage lotteryType: " + lotteryType + " senderId: " + senderId + " queryType: " + queryType);
        Result resultSummary = resultService.findLatestResultSummary(lotteryType);
        // send typing_on action.
        messageSender.sendActionTo(senderId, ConstantUtil.ACTION_TYPING_ON);
        
        List<GetPrizeDTO> dtoList = null;
        if(queryType == 0) { // normal query(ka 123456)
            // find prize
            dtoList = resultService.findPrizeByResultType(lotteryType, Iterables.toArray(codePoints, String.class));
        } else if(queryType == 1){ // character range query(ka nya 123456)
            // find prize by code range(start and end)
            dtoList = resultService.findPrizesByCode(codePoints.get(0), codePoints.get(1), codePoints.get(2), lotteryType);
        } else { // number range query(ka 123456 123459)
            dtoList = resultService.findPrizesByPoints(codePoints.get(0), codePoints.get(1), codePoints.get(2), lotteryType);
        }
        
        String lotteryMsg = messageHandler.get("default.prize.type"+lotteryType);
        String resultMsg = messageHandler.get("default.noprize.message", new Object[]{resultSummary==null? 0:resultSummary.getNumberOfTimes(), lotteryMsg});
        if(!dtoList.isEmpty()) {
            // get the related messages from message properties.
            if(queryType == ConstantUtil.CODE_RANGE_QUERY) {
                resultMsg = messageHandler.get("default.win.char.series.prize.message", new Object[]{resultSummary.getNumberOfTimes(), lotteryMsg});
            } else if(queryType == ConstantUtil.POINT_RANGE_QUERY) {
                resultMsg = messageHandler.get("default.win.number.series.prize.message", new Object[]{resultSummary.getNumberOfTimes(), lotteryMsg});
            } else {
                resultMsg = messageHandler.get("default.win.prize.message", new Object[]{resultSummary.getNumberOfTimes(), lotteryMsg});
            }
            
            for(GetPrizeDTO dto : dtoList) {
                resultMsg += messageHandler.get("default.win.prize.item.message", new Object[]{dto.getCode(), dto.getResult()});
            }
        }
        // send result message.
        messageSender.sendMessageTo(senderId, Message.Text(resultMsg));
    }
    
    // since 500 lottery ticket out.
    @Deprecated
    private void manageQuickReply(QuickReply quickReply, String senderId) throws IOException {
        log.debug("Quick reply payload: "+ quickReply.payload);
        ItemDTO item = messageSender.getGson().fromJson(quickReply.payload, ItemDTO.class);
        log.debug(item);
        
        // send typing_on action.
        messageSender.sendActionTo(senderId, ConstantUtil.ACTION_TYPING_ON);
        
        List<GetPrizeDTO> dtoList = null;
        if(item.queryType == 0) {
            // find prize
            dtoList = resultService.findPrizeByResultType(item.type, Iterables.toArray(item.codePoints, String.class));
        } else if(item.queryType == 1){
            // find prize by code range(start and end)
            dtoList = resultService.findPrizesByCode(item.codePoints.get(0), item.codePoints.get(1), item.codePoints.get(2), item.type);
        } else {
            dtoList = resultService.findPrizesByPoints(item.codePoints.get(0), item.codePoints.get(1), item.codePoints.get(2), item.type);
        }
        
//        String lotteryType = messageHandler.get("default.prize.type"+item.type);
//        String resultMsg = messageHandler.get("default.noprize.message", new Object[]{resultSummary==null? 0:resultSummary.getNumberOfTimes(), lotteryType});
//        if(!dtoList.isEmpty()) {
//            if(item.queryType == 1) {
//                resultMsg = messageHandler.get("default.win.char.series.prize.message", new Object[]{resultSummary.getNumberOfTimes(), lotteryType});
//            } else if(item.queryType == 2) {
//                resultMsg = messageHandler.get("default.win.number.series.prize.message", new Object[]{resultSummary.getNumberOfTimes(), lotteryType});
//            } else {
//                resultMsg = messageHandler.get("default.win.prize.message", new Object[]{resultSummary.getNumberOfTimes(), lotteryType});
//            }
//            
//            for(GetPrizeDTO dto : dtoList) {
//                resultMsg += messageHandler.get("default.win.prize.item.message", new Object[]{dto.getCode(), dto.getResult()});
//            }
//        }
//        messageSender.sendMessageTo(senderId, Message.Text(resultMsg));
        
        sendResultMessage(senderId, dtoList, item.type, item.queryType);
    }
    
    private void sendResultMessage(String senderID, List<GetPrizeDTO> list, int lotteryType, int queryType) throws IOException {
        String lotteryTypeMsg = messageHandler.get("default.prize.type"+lotteryType);
        Result resultSummary = resultService.findLatestResultSummary(lotteryType);
        
        int numOfTimes = (resultSummary==null? 0:resultSummary.getNumberOfTimes());
        String resultMsg = messageHandler.get("default.noprize.message", new Object[]{numOfTimes, lotteryTypeMsg});
        if(!list.isEmpty()) {
            if(queryType == 1) {
                resultMsg = messageHandler.get("default.win.char.series.prize.message", new Object[]{numOfTimes, lotteryTypeMsg});
            } else if(queryType == 2) {
                resultMsg = messageHandler.get("default.win.number.series.prize.message", new Object[]{numOfTimes, lotteryTypeMsg});
            } else {
                resultMsg = messageHandler.get("default.win.prize.message", new Object[]{numOfTimes, lotteryTypeMsg});
            }
            
            for(GetPrizeDTO dto : list) {
                resultMsg += messageHandler.get("default.win.prize.item.message", new Object[]{dto.getCode(), dto.getResult()});
            }
        }
        // send result message.
        messageSender.sendMessageTo(senderID, Message.Text(resultMsg));
    }
    
    /**
     * No more need for performance reason.
     * @param userID
     * @param url 
     
    private void insertLog(String userID, String url) {
        logRepository.inserLog(new LogInfo(new Date(), 
                userID, LogInfo.FACEBOOK_USER, 
                "N/A", 
                url));
    }
    * */
    
    /**
     * Step 1 - delete old result
     * Step 2 - insert new result
     * Step 3 - clear spring cache data
     * Step 4 - add data into Spring cache
     * Step 5 - send notification message to registered user id for schedule lottery result.
     * 
     * @param result 
     */
    private void processUploadData(Result result) {
        // Step 1 to delete old result first.
        resultService.deleteByType(result.getType());
        log.info("Step 1 to delete old result first.");

        // Step 2 insert new record.
        result = resultService.insert(result);
        log.info("Step 2 insert new record."+ result.getType());

        // Step 3 clear json cache with lottery type.
        resultBox.removeJsonValue(result.getType());
        log.info("Step 3 clear json cache.");

        // Step 4 add json cache
        resultBox.getJsonValue(result.getType());
        log.info("Step 4 add json cache.");
    }
    
    private final String CSV_FILE_TYPE = "csv";
    private final String EXCEL_FILE_TYPE = "xlsx";
    
    /**
     * The following process go here.
     * (1) download file
     * (2) parse file.
     * (3) insert file data into DB.
     * 
     * @param link
     * @param fileType
     * @return
     * @throws ParseException
     * @throws IOException 
     */
    private String insertUploadData(String link, String fileType) throws ParseException, IOException {
        InputStream in = null;
        String message = "Upload Successfully.";
        try {
            in = downloadCSVFile(link);
            List<Result> resultList = null;
            switch (fileType) {
                case CSV_FILE_TYPE:
                    resultList = csvParser.getResult(in);
                    log.info("Parsing Upload csv file.");
            
                    break;
                case EXCEL_FILE_TYPE:
                    log.info("Parsing Upload excel file.");
                    resultList = excelParser.getResult(in);
                    break;
            }
            
            
            for(Result r : resultList) {
                // try to insert DB.
                processUploadData(r);
                message += "\n Upload Lottery Result count "+ r.getPrizes().size();
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(FBResultServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return message;
    }
    
    /**
     * Start Spring Scheduling service.
     */
    private void startAsycNotifyMessageTasks() {
        log.info("start AsycNotify MessageTasks...");
        try {
            for(ScheduleItem item : scheduleService.findAll()) {
                log.info("item: " + item.getRecipientID());
                Future<List<GetPrizeDTO>> itm = scheduleService.scheduleItems(item);
                messageSender.sendMessageTo(item.getRecipientID(), Message.Text(messageHandler.get("default.notify.info.message", new Object[]{item.getOriginParam()})));
                sendResultMessage(item.getRecipientID(), itm.get(), item.getLotteryType(), item.getQueryType());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(FBResultServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private InputStream downloadCSVFile(String link) throws IOException {
        OkHttpClient client = messageSender.getHttpClient();
        Request request = new Request.Builder().url(link).build();
        okhttp3.Response response = client.newCall(request).execute();

        InputStream in = response.body().byteStream();
        return in;
    }
}
