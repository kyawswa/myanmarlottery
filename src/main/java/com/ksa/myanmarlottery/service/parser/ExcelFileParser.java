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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kyawswa
 */
@Component
public class ExcelFileParser implements FileParser {

    private final Log log = LogFactory.getLog(ExcelFileParser.class);
    
    @Autowired
    private CharacterMap charMap;
    
    @Override
    public List<Result> getResult(InputStream in) throws FileNotFoundException, IOException, ParseException {
        List<Prize> prizes = null;
        List<Result> resultList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy"); // 01-May-2017
        try {
            Workbook workbook = new XSSFWorkbook(in);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Cell cell0 = currentRow.getCell(0); // get first cell.
                
                if (cell0.getCellTypeEnum() == CellType.NUMERIC) {
                    int numberic = (int)cell0.getNumericCellValue();
                    log.info("Numberic - " + numberic);

                    // check lottery type
                    if(ConstantUtil.OLD_LOTTERY_TYPE == numberic || ConstantUtil.NEW_LOTTERY_TYPE == numberic) { // for lottery type result
                        Result result = new Result();
                        result.setType(numberic);
                        result.setNumberOfTimes((int)currentRow.getCell(1).getNumericCellValue());
//                        result.setResultFor(format.parse(currentRow.getCell(2).toString()));
                        result.setResultFor(currentRow.getCell(2).getDateCellValue());
                        result.setDataProvider(currentRow.getCell(3).getStringCellValue());
                        result.setCompanyName(currentRow.getCell(4).getStringCellValue());
                        
                        prizes = new ArrayList<>();
                        result.setPrizes(prizes);
                        resultList.add(result);
                    }

                } else if (cell0.getCellTypeEnum() == CellType.STRING) { // result data
                    String character = cell0.getStringCellValue();
                    log.info("character - "+character);
                    
                    // check validation for character.
                    String value = charMap.get(character);
                    if(value == null) {
                        throw new ParseException("Character is Not valid at Row: " + currentRow.getRowNum()+" > column:" + 0, 400);
                    }
                    Cell cell1 = currentRow.getCell(1);
                    if(cell1.getCellTypeEnum() != CellType.NUMERIC) {
                        throw new ParseException("Should be Number at Row: " + currentRow.getRowNum()+" > column:" + 1, 400);
                    }
                    log.info("Cell Type "+cell1.getCellTypeEnum());
                    int code = (int) cell1.getNumericCellValue();
                    log.info("code - "+code+ " Row:" +currentRow.getRowNum()+" > column:" + 1);
                    String prizeTitle = currentRow.getCell(2).getStringCellValue();
                    log.info("prizeTitle - "+prizeTitle);
                    String prizeDesc = currentRow.getCell(4).getStringCellValue();
                    log.info("prizeDesc - "+prizeDesc);
                    prizes.add(new Prize(character, code, prizeTitle, prizeDesc));
                }
            }
            log.info("resultList size: "+resultList.size());
            for(Result r : resultList) {
                log.info("prizeList size: "+r.getPrizes().size());
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return resultList;
    }

    public CharacterMap getCharMap() {
        return charMap;
    }

    public void setCharMap(CharacterMap charMap) {
        this.charMap = charMap;
    }
}
