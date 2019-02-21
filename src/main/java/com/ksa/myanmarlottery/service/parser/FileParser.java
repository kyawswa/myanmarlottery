/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.service.parser;

import com.ksa.myanmarlottery.model.Result;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

/**
 *
 * @author Kyawswa
 */
public interface FileParser {
    List<Result> getResult(InputStream in) throws FileNotFoundException, IOException, ParseException;
}
