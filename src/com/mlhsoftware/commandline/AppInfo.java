/*
 * WebApiBase.java
 *
 * MLH Software
 * Copyright 2010
 * 
 */

package com.mlhsoftware.commandline;

public class AppInfo
{
  static public final boolean APPWORLD = false;
  static public final boolean BETA = false;
  static public final boolean V47 = true;

  static public final String APP_VERSION = "0.0.0.1";

  static public final String APP_NAME = "CommandLine";
  static public final long LOGGER_ID = 0x75a58afd2773d2f1L;      //com.mlhsoftware.CommandLine
  static public final long APP_ID = 0x75a58afd2773d2f1L; //com.mlhsoftware.CommandLine

  static public final String SECRET_KEY = "NOT GOING TO TELL YOU";
  static public final long PERSISTENCE_ID = 0x650cb45de43554a6L;   //Hash of com.mlhsoftware.CommandLine.keycode

  static public final int MAX_TRIALS = 11;
  static public final String MOBIHAND_PID = "";
  static public final String APPWORLD_PID = "";

  static public final String APP_URL = "http://m.mlhsoftware.com";
  static public final String HELP_URL = "http://www.mlhsoftware.com/CommandLine";
  static public final String TELL_A_FRIEND = APP_NAME + " for your BlackBerry, check it out at http://www.mlhsoftware.com";

  static public final int BACKGROUND_COLOR = 0xDFDFDF;
  static public final int COLOR_BORDER = 0x222222;
  static public final int COLOR_INNER_BACKGROUND_FOCUS = 0xB2EEF6;
  

  // Titlebar colors
  static public final int[] TITLEBAR_COLORS = { 0x006C6C6A, 0x006C6C6A, 0x00ACAEAB, 0x00ACAEAB };
  static public final int TITLEBAR_TOPLINE_COLORS = 0x6F6F6F;
  static public final int TITLEBAR_BOTTOMLINE_COLORS = 0x444645;
  static public final int TITLEBAR_FONT_SIZE = 8;
}
