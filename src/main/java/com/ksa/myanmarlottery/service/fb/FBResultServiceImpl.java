/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.service.fb;

import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import com.hyurumi.fb_bot_boilerplate.FBMessageSender;
import com.hyurumi.fb_bot_boilerplate.models.send.Message;
import com.hyurumi.fb_bot_boilerplate.models.send.QuickReply;
import com.hyurumi.fb_bot_boilerplate.models.webhook.Attachment;
import com.hyurumi.fb_bot_boilerplate.models.webhook.Messaging;
import com.hyurumi.fb_bot_boilerplate.models.webhook.ReceivedMessage;
import com.ksa.myanmarlottery.dto.GetPrizeDTO;
import com.ksa.myanmarlottery.dto.ItemDTO;
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
import java.util.List;
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
 * @author Kyawswa
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
    
    @Override
    public void manageRequst(String body) {
        log.info("Manage Facebook Request..");
        ReceivedMessage receivedMessage = messageSender.getGson().fromJson(body, ReceivedMessage.class);
        List<Messaging> messagings = receivedMessage.entry.get(0).messaging;
        try {
            for (Messaging messaging : messagings) {
                String senderId = messaging.sender.id;
                if (messaging.message !=null) {
                    
                    if (messaging.message.text != null) {
                        QuickReply quickReply = messaging.message.quick_reply;
                        if(quickReply != null) { // quick reply
                            //manageQuickReply(quickReply, senderId);
                            log.info("ManageQuickReply.. " + senderId);
                        } else { // normal input
                            // insert Log
                            // logRepository.inserLog(new LogInfo(new Date(), senderId, ConstantUtil.FACEBOOK, null, null));
                            String param = messaging.message.text;
                            log.debug("Param : "+ param);
                            // insert log
                            // no mre need for performance reason.
                            // insertLog(senderId, "/result");
                            if(param != null) {
                                if("help".equalsIgnoreCase(param) || "?".equals(param)) {
                                    messageSender.sendMessageTo(senderId, Message.Text(messageHandler.get("default.help.message")));
                                    log.info("Send Help reply "+ senderId);
                                } else {
                                    manageQueryParam(param, senderId);
                                    log.info("manageQueryParam... "+ senderId);
									log.debug("Param... "+ param);
                                }
                            }
                        }
                    } else if(messaging.message.attachments != null || !messaging.message.attachments.isEmpty()) {
//                        if(senderId.equals(env.getProperty("sender.id"))) {
                        if(isAuthorizedSender(senderId)) {
                            Attachment attachment = messaging.message.attachments.get(0);
                            log.info("manageUpLoad... "+ senderId);
                            if(attachment.type.FILE.equals(attachment.type)) {
                                log.info("manageUpLoad... "+ senderId);
                                URL url = new URL(attachment.payload.url);
                                String fileName = new File(url.getPath()).getName();
                                log.debug("Upload File Name: "+fileName);
                                String[] arg = fileName.split(".");
                                
                                String fileType = Files.getFileExtension(fileName);
                                // skip if file extension is other file type.
                                if(CSV_FILE_TYPE.equals(fileType) || EXCEL_FILE_TYPE.equals(fileType)) {
                                    String message = "Upload Successfully.";
//                                    boolean isSuccess = false;
                                    try {
                                        message = insertUploadData(attachment.payload.url, fileType);
                                        // Setp 5 send notification for lottery result if the all process are success.
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
    
    private final String lottery1K = "1000"; // concat space
    private final String lottery500 = "500";
    private final String regCommand = "@"; // sign for notification.
    
    private void manageQueryParam(String param, String senderId) throws IOException {
        
        // remove space between comma
        param = param.replaceAll(",\\s|\\s,\\s", ",");

        boolean forNoti = false;
        String originParam = "";
        
        if(param.startsWith(regCommand)) {
            forNoti = true;
            param = param.substring(2); // remove noti command
            originParam = param;
        }
        
        // default one
        int lotteryType = ConstantUtil.NEW_LOTTERY_TYPE;
        // classify lottery type
        if(param.startsWith(lottery500)) {
            lotteryType = ConstantUtil.OLD_LOTTERY_TYPE;
            param = param.substring(4); // remove lottery type from paramlottery200
        } else if(param.startsWith(lottery1K)) {
            lotteryType = ConstantUtil.NEW_LOTTERY_TYPE;
            param = param.substring(5); // remove lottery type from param
        }
        
        // 0 is normal query
        // 1 and 2 is find by range query
        int queryType = 0;
        // step 1 split comma first.
        String[] arr = param.split(",");
        List<String> codePoints = new ArrayList<>();
        
        for(String str: arr) {
            
            String[] content = str.split(" ");
            
            // this is find by range
            if(content.length == 3) {
                queryType = ConstantUtil.getCodePointRangeType(content);
                if(queryType != 0) { // 0 is invalid
                    codePoints.add(content[0]); 
                    codePoints.add(content[1]); 
                    codePoints.add(content[2]); 
                }
                break; // only choose first part and skip the rest of all.
                
            } else if(content.length == 2) { // this is normal search
                // skip if codepoint is not valid.
                if(ConstantUtil.isCodePointValid(content)) {
                    codePoints.add(content[0]+"-"+content[1]); // to change myanmar number to english number
                }
            }
        }
        
        // prepare for quick reply
        if(codePoints.size() > 0 || !codePoints.isEmpty()) {
            
            // for notification.
            if(forNoti) {
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
    
    private boolean isAuthorizedSender(String senderID) {
        log.info("Check authorized sender:"+senderID);
        for(String str : authorizedSenders) {
            log.info("Check authorized sender:"+senderID.equals(str));
            if(senderID.equals(str)) return true;
        }
        return false;
    }
    
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
    
    private void replyMessage(int lotteryType, List<String> codePoints, String senderId, int queryType) throws IOException {
        log.info("replyMessage lotteryType: " + lotteryType + " senderId: " + senderId + " queryType: " + queryType);
        Result resultSummary = resultService.findLatestResultSummary(lotteryType);
        // send typing_on action.
        messageSender.sendActionTo(senderId, ConstantUtil.ACTION_TYPING_ON);
        
        List<GetPrizeDTO> dtoList = null;
        if(queryType == 0) {
            // find prize
            dtoList = resultService.findPrizeByResultType(lotteryType, Iterables.toArray(codePoints, String.class));
        } else if(queryType == 1){
            // find prize by code range(start and end)
            dtoList = resultService.findPrizesByCode(codePoints.get(0), codePoints.get(1), codePoints.get(2), lotteryType);
        } else {
            dtoList = resultService.findPrizesByPoints(codePoints.get(0), codePoints.get(1), codePoints.get(2), lotteryType);
        }
        
        String lotteryMsg = messageHandler.get("default.prize.type"+lotteryType);
        String resultMsg = messageHandler.get("default.noprize.message", new Object[]{resultSummary==null? 0:resultSummary.getNumberOfTimes(), lotteryMsg});
        if(!dtoList.isEmpty()) {
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
