package com.tcd.yaatra.utils.offlinemaps;

import java.util.Locale;

public class UnitCalculator
{
  public static final double METERS_OF_FEET = 0.3048;
  public static final double METERS_OF_MILE = 1609.344;
  public static final double METERS_OF_KM = 1000.0;
  public static final double FEETS_OF_MILE = 5280.0;
  
  public static String getString(double m)
  {
    if (m < METERS_OF_KM) return Math.round(m) + " meter";
    return (((int) (m / 100)) / 10f) + " km";
  }
  
  public static String getUnit(boolean big)
  {
    if (big) { return "km"; }
    return "m";
  }
  
  /** How many meters for switch to Big unit **/
  public static double getMultValue()
  {
    return METERS_OF_KM;
  }
  
  /** Returns a rounded Value of KM or MI.
   *  @param pdp Post decimal positions. **/
  public static String getBigDistance(double m, int pdp)
  {
    m = toImperalMiles(m);
    return String.format(Locale.getDefault(), "%." + pdp + "f", m);
  }
  
  /** Returns the value in KM or MI. **/
  public static double getBigDistanceValue(double km)
  {
    return km;
  }
  
  /** Returns a rounded Value of M or FT. **/
  public static String getShortDistance(double m)
  {
    m = toImperalFeet(m);
    return "" + Math.round(m);
  }
//  
//  /** Get KM or MI **/
//  public static double getCorrectValueFromKm(double km)
//  {
//    if (!Variable.getVariable().isImperalUnit()) { return km; }
//    return toImperalMiles(km * 1000);
//  }
//
//  /** Get M or FT **/
//  public static double getCorrectValueFromM(double m)
//  {
//    if (!Variable.getVariable().isImperalUnit()) { return m; }
//    return toImperalFeet(m);
//  }
  
  private static long toImperalFeet(double m)
  {
    m = m / METERS_OF_FEET;
    return Math.round(m);
  }

  /** Returns the Value of MI. **/
  private static double toImperalMiles(double m)
  {
    return m / METERS_OF_MILE;
  }
  
  /** Returns a rounded Value of MI.
   *  @param pdp Post decimal positions. **/
  private static float toImperalMiles(double m, int pdp)
  {
    m = toImperalMiles(m);
    float mult = 10 * pdp;
    return (((int) (m * mult)) / mult);
  }
}
