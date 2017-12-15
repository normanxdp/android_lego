/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.bigplan.lego.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn 名称：AbDateUtil.java 描述：日期处理类.
 */
public class AbDateUtil {
    
    /** 时间日期格式化到年月日时分秒. */
    public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";
    
    /** 时间日期格式化到年月日. */
    public static final String dateFormatYMD = "yyyy-MM-dd";
    
    /** 时间日期格式化到年月. */
    public static final String dateFormatYM = "yyyy-MM";
    
    /** 时间日期格式化到年月日时分. */
    public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";
    
    /** 时间日期格式化到月日. */
    public static final String dateFormatMD = "MM/dd";
    
    /** 时分秒. */
    public static final String dateFormatHMS = "HH:mm:ss";
    
    /** 时分. */
    public static final String dateFormatHM = "HH:mm";
    
    /** 上午. */
    public static final String AM = "AM";
    
    /** 下午. */
    public static final String PM = "PM";

    public static String getDateFromInt(String time) {
        long aLong = Long.parseLong(time);
        aLong = aLong*1000;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(aLong);
        return formatter.format(calendar.getTime());
    }
    /**
     * 描述：String类型的日期时间转化为Date类型.
     * 
     * @param strDate String形式的日期时间
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return Date Date类型日期时间
     */
    public static Date getDateByFormat(String strDate, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = mSimpleDateFormat.parse(strDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * 描述：获取偏移之后的Date.
     * 
     * @param date 日期时间
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset 偏移(值大于0,表示+,值小于0,表示－)
     * @return Date 偏移之后的日期时间
     */
    public Date getDateByOffset(Date date, int calendarField, int offset) {
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(date);
            c.add(calendarField, offset);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }
    
    /**
     * 描述：获取指定日期时间的字符串(可偏移).
     * 
     * @param strDate String形式的日期时间
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset 偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String getStringByOffset(String strDate, String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(mSimpleDateFormat.parse(strDate));
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return mDateTime;
    }
    
    /**
     * 描述：Date类型转化为String类型(可偏移).
     * 
     * @param date the date
     * @param format the format
     * @param calendarField the calendar field
     * @param offset the offset
     * @return String String类型日期时间
     */
    public static String getStringByOffset(Date date, String format, int calendarField, int offset) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(date);
            c.add(calendarField, offset);
            strDate = mSimpleDateFormat.format(c.getTime());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    
    /**
     * 描述：Date类型转化为String类型.
     * 
     * @param date the date
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    
    /**
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     * 
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format 输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    public static String getStringByFormat(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDHMS);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }
    
    /**
     * 描述：获取milliseconds表示的日期时间的字符串.
     * 
     * @param milliseconds the milliseconds
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 日期时间字符串
     */
    public static String getStringByFormat(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }
    
    public static String getStringByFormat(long milliseconds) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            thisDateTime = mSimpleDateFormat.format(new Date(milliseconds));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    public static String getStringByFormat(String strMilliseconds) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressWarnings("unused")
            long milliseconds = Long.parseLong(strMilliseconds);
            thisDateTime = mSimpleDateFormat.format(new Date(milliseconds));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    public static String getStringBySecFormat(String strSeconds) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressWarnings("unused")
            long seconds = Long.parseLong(strSeconds);
            thisDateTime = mSimpleDateFormat.format(new Date(seconds * 1000L));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }


    
    /**
     * 描述：获取表示当前日期时间的字符串.
     * 
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    public static String getCurrentDate(String format) {
      
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;
        
    }
    
    public static String getCurrentDate() {
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;
        
    }
    
    public static long getCurrentDateByLong() {
        long curDateTime = 0;
        try {
            Calendar c = new GregorianCalendar();
            return c.getTimeInMillis();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;
        
    }
    
    /**
     * 描述：获取表示当前日期时间的字符串(可偏移).
     * 
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset 偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String getCurrentDateByOffset(String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
        
    }
    
    /**
     * 描述：计算两个日期所差的天数.
     * 
     * @param milliseconds1 the milliseconds1
     * @param milliseconds2 the milliseconds2
     * @return int 所差的天数
     */
    public static int getOffectDay(long milliseconds1, long milliseconds2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(milliseconds1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(milliseconds2);
        // 先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        }
        else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        }
        else {
            day = d1 - d2;
        }
        return day;
    }
    
    /**
     * 描述：计算两个日期所差的小时数.
     * 
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    public static int getOffectHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = getOffectDay(date1, date2);
        h = h1 - h2 + day * 24;
        return h;
    }
    
    /**
     * 描述：计算两个日期所差的分钟数.
     * 
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    public static int getOffectMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = getOffectHour(date1, date2);
        int m = 0;
        m = m1 - m2 + h * 60;
        return m;
    }
    
    /**
     * 描述：获取本周一.
     * 
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getFirstDayOfWeek(String format) {
        return getDayOfWeek(format, Calendar.MONDAY);
    }
    
    /**
     * 描述：获取本周日.
     * 
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getLastDayOfWeek(String format) {
        return getDayOfWeek(format, Calendar.SUNDAY);
    }
    
    /**
     * 描述：获取本周的某一天.
     * 
     * @param format the format
     * @param calendarField the calendar field
     * @return String String类型日期时间
     */
    private static String getDayOfWeek(String format, int calendarField) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (week == calendarField) {
                strDate = mSimpleDateFormat.format(c.getTime());
            }
            else {
                int offectDay = calendarField - week;
                if (calendarField == Calendar.SUNDAY) {
                    offectDay = 7 - Math.abs(offectDay);
                }
                c.add(Calendar.DATE, offectDay);
                strDate = mSimpleDateFormat.format(c.getTime());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    
    /**
     * 描述：获取本月第一天.
     * 
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getFirstDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            // 当前月的第一天
            c.set(GregorianCalendar.DAY_OF_MONTH, 1);
            strDate = mSimpleDateFormat.format(c.getTime());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
        
    }
    
    /**
     * 描述：获取本月最后一天.
     * 
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getLastDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            // 当前月的最后一天
            c.set(Calendar.DATE, 1);
            c.roll(Calendar.DATE, -1);
            strDate = mSimpleDateFormat.format(c.getTime());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    
    /**
     * 描述：获取表示当前日期的0点时间毫秒数.
     * 
     * @return the first time of day
     */
    public static long getFirstTimeOfDay() {
        Date date = null;
        try {
            String currentDate = getCurrentDate(dateFormatYMD);
            date = getDateByFormat(currentDate + " 00:00:00", dateFormatYMDHMS);
            return date.getTime();
        }
        catch (Exception e) {
        }
        return -1;
    }
    
    /**
     * 描述：获取表示当前日期24点时间毫秒数.
     * 
     * @return the last time of day
     */
    public static long getLastTimeOfDay() {
        Date date = null;
        try {
            String currentDate = getCurrentDate(dateFormatYMD);
            date = getDateByFormat(currentDate + " 24:00:00", dateFormatYMDHMS);
            return date.getTime();
        }
        catch (Exception e) {
        }
        return -1;
    }
    
    /**
     * 描述：判断是否是闰年()
     * <p>
     * (year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
     * 
     * @param year 年代（如2012）
     * @return boolean 是否为闰年
     */
    public static boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * 描述：根据时间返回格式化后的时间的描述. 小于1小时显示多少分钟前 大于1小时显示今天＋实际日期，大于今天全部显示实际时间
     * 
     * @param strDate the str date
     * @param outFormat the out format
     * @return the string
     */
    public static String formatDateStr2Desc(String strDate, String outFormat) {
        if(strDate!=null && strDate.length()==16){
            strDate =strDate+":00";
        }
        DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c2.setTime(df.parse(strDate));
            c1.setTime(new Date());
            int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
            if (d == 0) {
                int h = getOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
                if (h > 0) {
                    //return "今天" + getStringByFormat(strDate, dateFormatHM);
                     return h + "小时前";
                }
                else if (h < 0) {
                     return Math.abs(h) + "小时后";
                }
                else if (h == 0) {
                    int m = getOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
                    if (m > 0) {
                        return m + "分钟前";
                    }
                    else if (m < 0) {
                        // return Math.abs(m) + "分钟后";
                    }
                    else {
                        return "刚刚";
                    }
                }
                
            }
            else if (d > 0) {
                if (d == 1) {
                     return "昨天";
                }
                else if (d == 2) {
                     return "前天";
                }
            }
            else if (d < 0) {
                if (d == -1) {
                     return "明天";
                }
                else if (d == -2) {
                     return "后天";
                }
                else {
                     return Math.abs(d) + "天后";
                }
            }
            
            String out = getStringByFormat(strDate, outFormat);
            if (!AbStrUtil.isEmpty(out)) {
                return out;
            }
        }
        catch (Exception e) {
        }
        
        return strDate;
    }
    
    /**
     * 取指定日期为星期几.
     * 
     * @param strDate 指定日期
     * @param inFormat 指定日期格式
     * @return String 星期几
     */
    public static String getWeekNumber(String strDate, String inFormat) {
        String week = "星期日";
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(inFormat);
        try {
            calendar.setTime(df.parse(strDate));
        }
        catch (Exception e) {
            return "错误";
        }
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (intTemp)
        {
            case 0:
                week = "星期日";
                break;
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
        }
        return week;
    }
    
    /**
     * 根据给定的日期判断是否为上下午.
     * 
     * @param strDate the str date
     * @param format the format
     * @return the time quantum
     */
    public static String getTimeQuantum(String strDate, String format) {
        Date mDate = getDateByFormat(strDate, format);
        int hour = mDate.getHours();
        if (hour >= 12)
            return "PM";
        else
            return "AM";
    }
    
    /**
     * 根据给定的毫秒数算得时间的描述.
     * 
     * @param milliseconds the milliseconds
     * @return the time description
     */
    public static String getTimeDescription(long milliseconds) {
        if (milliseconds >= 1000) {
            // 大于一分
            if (milliseconds / 1000 / 60 > 1) {
                long minute = milliseconds / 1000 / 60;
                long second = milliseconds / 1000 % 60;
                return minute + "分" + second + "秒";
            }
            else {
                // 显示秒
                return milliseconds / 1000 + "秒";
            }
        }
        else {
            return milliseconds + "毫秒";
        }
    }
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        System.out.println(formatDateStr2Desc("2012-3-2 12:2:20", "MM月dd日  HH:mm"));
    }
    
    public static String getTimeDiff(String strDate) {
        String result;
        if (strDate.length() < 17)
        {
            strDate = strDate + ":00";
        }
        if (strDate != null && !strDate.equals(""))
        {
            Date date = strToDateLong(strDate);
            Calendar cal = Calendar.getInstance();
            long diff = 0;
            Date dnow = cal.getTime();
            String str = "";
            diff = dnow.getTime() - date.getTime();
            
            if (diff > 604800000) {// 7 * 24 * 60 * 60 * 1000=604800000 毫秒
                str= getStringByFormat(strDate,"MM-dd");
               
            }
            else if (diff == 86400000) { // 24 * 60 * 60 * 1000=86400000 毫秒
                // System.out.println("X天前");
                str = "昨天";
            }else if (diff > 86400000) { // 24 * 60 * 60 * 1000=86400000 毫秒
                // System.out.println("X天前");
                str = (int)Math.floor(diff / 86400000f) + "天前";
            }
            else if (diff > 18000000) {// 5 * 60 * 60 * 1000=18000000 毫秒
                // System.out.println("X小时前");
                str = (int)Math.floor(diff / 18000000f) + "小时前";
            }
            else if (diff > 60000) {// 1 * 60 * 1000=60000 毫秒
                // System.out.println("X分钟前");
                str = (int)Math.floor(diff / 60000) + "分钟前";
            }
            else {
                str = "刚刚";
            }
            result = str;
        }
        else
        {
            result = strDate;
        }
        
        return result;
    }
    
    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     * 
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
    
    public static final String TODAY = "today";
    public static final String YESTERDAY = "yesterday";
    public static final String TOMORROW = "tomorrow";

    public static String MIDDLE = "12:00";
    
    public static boolean checkTimePassOrNot(String datetime) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = f.parse(datetime);
        Date now = new Date(System.currentTimeMillis());
        if(date.before(now) || sdf.format(date).equals(sdf.format(now))) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean checkTimeAlertable(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = calendar.get(Calendar.MINUTE);
        if(hourOfDay < curHour || (hourOfDay == curHour && curMinute>= minute)) {
            return false;
        }
        return true;
    }
    
    public static boolean checkTimeAlertable(String datetime) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = f.parse(datetime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        int curHour = now.get(Calendar.HOUR_OF_DAY);
        int curMinute = now.get(Calendar.MINUTE);
        if(hourOfDay < curHour || (hourOfDay == curHour && curMinute>= minute)) {
            return false;
        }
        return true;
    }
    
    public static String yesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        return formatDate(year, month, day);
    }
    
    public static String calcDatetime(String datetime, int days) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = f.parse(datetime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        date = calendar.getTime();
        return f.format(date);
    }
    
    public static String getWeekDay(String DateStr) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        int weekDay = 0;
        try {
            date = f.parse(DateStr);// 将String 转换为符合格式的日期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            System.out.println(weekDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // System.out.println("日期:"+DateStr+" ： "+weekDay);
        String weekday1 = "";
        if (weekDay == 1)
            weekday1 = "周日";
        else if (weekDay == 2)
            weekday1 = "周一";
        else if (weekDay == 3)
            weekday1 = "周二";
        else if (weekDay == 4)
            weekday1 = "周三";
        else if (weekDay == 5)
            weekday1 = "周四";
        else if (weekDay == 6)
            weekday1 = "周五";
        else if (weekDay == 7)
            weekday1 = "周六";

        return weekday1;
    }
    
    public static String formatTime(int hourOfDay, int minute) {
        String time = "";
        if (hourOfDay < 10) {
            time += ("0" + hourOfDay);
        } else {
            time += hourOfDay;
        }
        if (minute < 10) {
            time += (":0" + minute);
        } else {
            time += (":" + minute);
        }
        return time;
    }
    
    public static String formatDate(int year, int month, int day) {
        String date = year + "-";
        if (month < 10) {
            date += ("0" + month);
        } else {
            date += month;
        }
        if (day < 10) {
            date += ("-0" + day);
        } else {
            date += ("-" + day);
        }
        return date;
    }
    
    public static String getHeartMessageTimeByDateTime(String datetime) {
        int[] yearMonthDayHourAndMinute = getYearMonthDayHourMinuteAndSecondByDateTime(datetime);
        StringBuilder sb = new StringBuilder();
        sb.append(yearMonthDayHourAndMinute[0]+"年");
        sb.append(yearMonthDayHourAndMinute[1]>=10?yearMonthDayHourAndMinute[1]+"月":"0"+yearMonthDayHourAndMinute[1]+"月");
        sb.append(yearMonthDayHourAndMinute[2]>=10?yearMonthDayHourAndMinute[2]+"日":"0"+yearMonthDayHourAndMinute[2]+"日");
        sb.append(yearMonthDayHourAndMinute[3]>=10?yearMonthDayHourAndMinute[3]+":":"0"+yearMonthDayHourAndMinute[3]+":");
        sb.append(yearMonthDayHourAndMinute[4]>=10?yearMonthDayHourAndMinute[4]+":":"0"+yearMonthDayHourAndMinute[4]+":");
        sb.append(yearMonthDayHourAndMinute[5]>=10?yearMonthDayHourAndMinute[5]:"0"+yearMonthDayHourAndMinute[5]);
        return sb.toString();
    }
    
    public static int[] getYearMonthDayHourMinuteAndSecondByDateTime(String datetime) {
        int[] yearMonthDayHourAndMinute = new int[6];
        String[] spliteOnce = datetime.split(" ", 2);
        String[] spliteDate = spliteOnce[0].split("\\-");
        String[] spliteTime = spliteOnce[1].split("\\:");
        yearMonthDayHourAndMinute[0] = Integer.parseInt(spliteDate[0]);
        yearMonthDayHourAndMinute[1] = Integer.parseInt(spliteDate[1]);
        yearMonthDayHourAndMinute[2] = Integer.parseInt(spliteDate[2]);
        yearMonthDayHourAndMinute[3] = Integer.parseInt(spliteTime[0]);
        yearMonthDayHourAndMinute[4] = Integer.parseInt(spliteTime[1]);
        yearMonthDayHourAndMinute[5] = Integer.parseInt(spliteTime[2]);
        return yearMonthDayHourAndMinute;
    }
    
    public static int[] getYearMonthDayHourAndMinuteByDateTime(String datetime) {
        int[] yearMonthDayHourAndMinute = new int[5];
        String[] spliteOnce = datetime.split(" ", 2);
        String[] spliteDate = spliteOnce[0].split("\\-");
        String[] spliteTime = spliteOnce[1].split("\\:");
        yearMonthDayHourAndMinute[0] = Integer.parseInt(spliteDate[0]);
        yearMonthDayHourAndMinute[1] = Integer.parseInt(spliteDate[1]);
        yearMonthDayHourAndMinute[2] = Integer.parseInt(spliteDate[2]);
        yearMonthDayHourAndMinute[3] = Integer.parseInt(spliteTime[0]);
        yearMonthDayHourAndMinute[4] = Integer.parseInt(spliteTime[1]);
        return yearMonthDayHourAndMinute;
    }
    
    public static int[] getYearMonthAndDayByDateTime(String datetime) {
        String[] split = datetime.split(" ", 2)[0].split("\\-");
        int[] yearMonthAndDay = new int[3];
        yearMonthAndDay[0] = Integer.parseInt(split[0]);
        yearMonthAndDay[1] = Integer.parseInt(split[1]);
        yearMonthAndDay[2] = Integer.parseInt(split[2]);
        return yearMonthAndDay;
    }
    
    public static int[] getHourAndMinuteByDateTime(String datetime) {
        String[] split = datetime.split(" ", 2)[1].split("\\:");
        int[] hourAndMinute = new int[2];
        hourAndMinute[0] = Integer.parseInt(split[0]);
        hourAndMinute[1] = Integer.parseInt(split[1]);
        return hourAndMinute;
    }
    
    public static int[] getNowHourAndMinute() {
        int[] nowHourAndMinute = new int[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        nowHourAndMinute[0] = calendar.get(Calendar.HOUR_OF_DAY);
        nowHourAndMinute[1] = calendar.get(Calendar.MINUTE);
        return nowHourAndMinute;
    }

    public static String now() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(System.currentTimeMillis());
        return format.format(now);
    }
    
    public static String nowDetail() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(System.currentTimeMillis());
        return format.format(now);
    }

    public static String getDateByRepeatDays(int days) {
        String now = "至";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days - 1);
        int year = calendar.get(Calendar.YEAR);
        now += (year + "年");
        int month = calendar.get(Calendar.MONTH) + 1;
        now += (month + "月");
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        now += (day + "日");
        return now;
    }
    
    public static int[] getNowDate(){
        int[] nowYearMonthDay = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        nowYearMonthDay[0] = calendar.get(Calendar.YEAR);
        nowYearMonthDay[1] = calendar.get(Calendar.MONTH)+1;
        nowYearMonthDay[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return nowYearMonthDay;
    }
    
    public static int[] getTomorrowDate(){
        int[] tomorrowYearMonthDay = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        tomorrowYearMonthDay[0] = calendar.get(Calendar.YEAR);
        tomorrowYearMonthDay[1] = calendar.get(Calendar.MONTH)+1;
        tomorrowYearMonthDay[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return tomorrowYearMonthDay;
    }
    
    public static int getDifferDays(int year, int month, int days)
    {

        String datetime = formatDate(year, month, days);
        
        int differ = 0;
        try {
            differ =getDifferDays(datetime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return differ;
    }

    public static int[] getDayInfoByRepeatDays(int days) {
        int[] dayInfo = new int[3];

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days - 1);
        int year = calendar.get(Calendar.YEAR);
        dayInfo[0] = year;
        int month = calendar.get(Calendar.MONTH);
        dayInfo[1] = month;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dayInfo[2] = day;

        return dayInfo;
    }

    public static String getDayByRepeatDays(int days) {
        return "共" + days + "天";
    }

    public static int getDifferDays(String endDateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = sdf.parse(endDateStr);
        GregorianCalendar start = new GregorianCalendar();
        GregorianCalendar end = new GregorianCalendar();
        start.setTime(new Date());
        end.setTime(endDate);
        return getDays(start, end);
    }

    private static int getDays(GregorianCalendar g1, GregorianCalendar g2) {
        int elapsed = 0;
        boolean flag = true;
        GregorianCalendar gc1, gc2;

        g1.clear(Calendar.MILLISECOND);
        g1.clear(Calendar.SECOND);
        g1.clear(Calendar.MINUTE);
        g1.clear(Calendar.HOUR_OF_DAY);
        g2.clear(Calendar.MILLISECOND);
        g2.clear(Calendar.SECOND);
        g2.clear(Calendar.MINUTE);
        g2.clear(Calendar.HOUR_OF_DAY);

        if (g2.after(g1)) {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        } else {
            flag = false;
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }
        while (gc1.before(gc2)) {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return flag ? elapsed : (-elapsed);
    }
    
    public static String getTomorrowWidgetTimeDetail(int days) {
        String time = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        int year = calendar.get(Calendar.YEAR);
        time += (year + "/");
        int month = calendar.get(Calendar.MONTH) + 1;
        time += (month + "/");
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        time += (day + "");
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        switch (weekday) {
        case Calendar.MONDAY:
            time += ",周一";
            break;
        case Calendar.TUESDAY:
            time += ",周二";
            break;
        case Calendar.WEDNESDAY:
            time += ",周三";
            break;
        case Calendar.THURSDAY:
            time += ",周四";
            break;
        case Calendar.FRIDAY:
            time += ",周五";
            break;
        case Calendar.SATURDAY:
            time += ",周六";
            break;
        case Calendar.SUNDAY:
            time += ",周日";
            break;
        }
        return time;
    }

    public static String timeDetail(String time) {
        String now = "";
        Calendar calendar = Calendar.getInstance();
        if (time.equals(TOMORROW)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } else if (time.equals(YESTERDAY)) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        int year = calendar.get(Calendar.YEAR);
        now += (year + "年");
        int month = calendar.get(Calendar.MONTH) + 1;
        now += (month + "月");
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        now += (day + "日");
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        switch (weekday) {
        case Calendar.MONDAY:
            now += "星期一";
            break;
        case Calendar.TUESDAY:
            now += "星期二";
            break;
        case Calendar.WEDNESDAY:
            now += "星期三";
            break;
        case Calendar.THURSDAY:
            now += "星期四";
            break;
        case Calendar.FRIDAY:
            now += "星期五";
            break;
        case Calendar.SATURDAY:
            now += "星期六";
            break;
        case Calendar.SUNDAY:
            now += "星期日";
            break;
        }
        return now;
    }

    public static String getTaskTime(String datetime) {
        String[] splitStr = datetime.split(" ", 2);
        if (splitStr[1].equals(MIDDLE)) {
            return "中午12:00";
        } else {
            String[] time = splitStr[1].split(":");
            if (Integer.parseInt(time[0]) >= 12) {
                return "下午" + splitStr[1];
            } else {
                return "上午" + splitStr[1];
            }
        }
    }
    
}
