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
package com.ksa.myanmarlottery.service.parser;

import com.ksa.myanmarlottery.model.Prize;
import com.ksa.myanmarlottery.model.Result;
import com.ksa.myanmarlottery.util.CharacterMap;
import com.ksa.myanmarlottery.util.ConstantUtil;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.security.pkcs.ParsingException;

/**
 *
 * @author Kyawswa
 */
@Component
public class CSVFileParser implements FileParser {
    
    private final Log log = LogFactory.getLog(CSVFileParser.class);
    
    @Deprecated
    private final String OLD_LOTTERY_TYPE = String.valueOf(ConstantUtil.OLD_LOTTERY_TYPE); // 200 lottery for 1
    private final String NEW_LOTTERY_TYPE = String.valueOf(ConstantUtil.NEW_LOTTERY_TYPE); // 500 lottery for 2
    
    @Autowired
    private CharacterMap charMap;
    
    
    public List<Result> getResult(InputStream in) throws FileNotFoundException, IOException, ParseException {
        Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(new BufferedReader(new InputStreamReader(in,"UTF-8")));
        int i = 0;
        List<Result> resultList = new ArrayList<>();
        List<Prize> prizes = null;
        for (CSVRecord record : records) {
            if(OLD_LOTTERY_TYPE.equals(record.get(0))) { // for lottery type result
                Result result = new Result();
                result.setType(ConstantUtil.OLD_LOTTERY_TYPE);
                result.setNumberOfTimes(Integer.valueOf(record.get(1)));
                result.setResultFor(new SimpleDateFormat("dd/MM/yyyy").parse(record.get(2)));
                result.setDataProvider(record.get(3));
                result.setCompanyName(record.get(4));
                prizes = new ArrayList<Prize>();
                result.setPrizes(prizes);
                resultList.add(result);
                
                
            } else if(NEW_LOTTERY_TYPE.equals(record.get(0))){
                Result result = new Result();
                result.setType(ConstantUtil.NEW_LOTTERY_TYPE);
                result.setNumberOfTimes(Integer.valueOf(record.get(1)));
                result.setResultFor(new SimpleDateFormat("dd/MM/yyyy").parse(record.get(2)));
                result.setDataProvider(record.get(3));
                result.setCompanyName(record.get(4));
                
                prizes = new ArrayList<Prize>();
                result.setPrizes(prizes);
                resultList.add(result);
                
            } else {
                // check validation for character.
                String character = record.get(0);
                String value = charMap.get(character);
                if(value == null) {
                    throw new ParseException("Character is Not valid at Row " + i+" column:" + 0, 400);
                }
                prizes.add(new Prize(record.get(0), Integer.parseInt(record.get(1)), record.get(2), record.get(4)));
            }
            i++;
        }
        log.info("Total rows after parsing. " + i);
        return resultList;
    }
}
