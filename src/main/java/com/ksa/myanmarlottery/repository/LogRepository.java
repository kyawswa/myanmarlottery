/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.repository;

import com.ksa.myanmarlottery.model.LogInfo;

/**
 *
 * @author Kyawswar
 */
public interface LogRepository {
    void inserLog(LogInfo info);
}
