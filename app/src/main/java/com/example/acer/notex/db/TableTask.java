package com.example.acer.notex.db;

/**
 * Created by acer on 18.07.2017.
 */

public interface TableTask {
    String TABLE_NAME=" tasks ";
    String COLUMN_ID="id";
    String COLUMN_TASK="tasks";
    String COLUMN_IS_DONE="isdone";
    String COLUMN_IS_ALARMSET ="isalarmset";
    String COLUMN_IS_IMPORTANT="isimpotant";
    String CREATE=" CREATE TABLE ";
    String COMMA=" , ";
    String LBR=" ( ";
    String RBR=" ) ";
    String TERMINATE=" ; ";
    String INT_PK_AUTOIC=" INTEGER PRIMARY KEY AUTOINCREMENT ";
    String TYPE_INTEGER=" INTEGER ";
    String TYPE_REAL=" REAL ";
    String TYPE_TEXT=" TEXT ";
    String SELECT=" SELECT * ";
    String FROM =" FROM ";
    String WHEN_UPGRADING_FROM_1_2="";


}
