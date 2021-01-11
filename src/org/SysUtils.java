/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org;

/**
 *
 * @author AidenFox
 */
public class SysUtils {
    private static String scriptName = "[FoxesSound]";
    public static void sendErr(String Error){
        String ErrorStyle = scriptName+"[Error] " + Error;
        System.out.println(ErrorStyle);
    }
    
    public static void send(String Message){
        String messageStyle = scriptName+"[Info]"+Message;
        System.out.println(messageStyle);
    }

    public static void sendException(RuntimeException ex) {
        String exceptionStyle = scriptName+"[Exception] "+ex;
        System.out.println(exceptionStyle);
    }
}
