package com.hisalari.util.date;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
	public static final String FORMAT_DATE_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATE_MDHMS = "MM-dd HH:mm:ss";
	public static final String FORMAT_DATE_MDHM = "MM-dd HH:mm";
	public static final String FORMAT_DATE_YMD = "yyyy-MM-dd";
	public static final String FORMAT_DATE_YYYYMDD = "yyyy-MM-dd";

	public static final String FORMAT_DATE_YM = "yyyy-MM";
	public static final String FORMAT_DATE_HM = "HH:mm";
	public static final String FORMAT_DATE_YMDHM = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_YMD = "yyyyMMdd";

	public static final String START_TIME_END = " 00:00:00";
	public static final String END_TIME_END = " 23:59:59";
	/**
	 * 将Date类型转换为字符串
	 *
	 * @param date
	 *            日期类型
	 * @return 日期字符串
	 */
	public static String format(Date date) {
		return format(date, FORMAT_DATE_YMD);
	}

	public static Date convertStringToDate(String date, String pattern) {
		if (date == "")
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * 将Date类型转换为字符串
	 *
	 * @param date
	 *            日期类型
	 * @param pattern
	 *            字符串格式
	 * @return 日期字符串
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return "null";
		}
		if (pattern == null || pattern.equals("") || pattern.equals("null")) {
			pattern = FORMAT_DATE_YMDHMS;
		}
		return new SimpleDateFormat(pattern).format(date);
	}

	/**

	  * 获取指定日期下个月的第一天
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static String getFirstDayOfNextMonth(String dateStr, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH,1);
			calendar.add(Calendar.MONTH, 1);
			return sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得指定时间的最后一天 日期格式为yyyy-MM
	 * @param endTime
	 */
	public static String getLastDayOfMonth(String endTime) {
		String endYear = endTime.split("-")[0];
		String endMonth = endTime.split("-")[1];
		endTime = DateUtil.getLastDayOfMonth(Integer.parseInt(endYear), Integer.parseInt(endMonth));
		return endTime;
	}

	/**
	 * 将字符串转换为Date类型
	 *
	 * @param date
	 *            字符串类型
	 * @return 日期类型
	 */
	public static Date format(String date) {
		return format(date, null);
	}

	/**
	 * 将字符串转换为Date类型
	 *
	 * @param date
	 *            字符串类型
	 * @param pattern
	 *            格式
	 * @return 日期类型
	 */
	public static Date format(String date, String pattern) {
		if (pattern == null || pattern.equals("") || pattern.equals("null")) {
			pattern = FORMAT_DATE_YMDHMS;
		}
		if (date == null || date.equals("") || date.equals("null")) {
			return new Date();
		}
		Date d = null;
		try {
			d = new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException pe) {
		}
		return d;
	}

	/**
	 * 得到年月日时分秒字符串
	 *
	 * @return 字符串
	 */
	public static String getDateYmdHms() {
		Date date = new Date();
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String now = formatter1.format(date);
		return now;
	}

	public static String getDateYmd() {
		Date date = new Date();
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		String now = formatter1.format(date);
		return now;
	}

	public static Date getNextDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		date = c.getTime();
		return date;
	}

	public static Date getNextHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, 1);
		date = c.getTime();
		return date;
	}

	public static Date getPreviousMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		date = c.getTime();
		return date;
	}

	public static Date getPreviousWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -7);
		date = c.getTime();
		return date;
	}

	// 获得当前月的最后一天
	public static Date getLastDayOfMonth() {
		String dateFormat = FORMAT_DATE_YMD;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		return strToDate(sdf.format(lastDate.getTime()), dateFormat);
	}

	/**
	 * 获取某月的最后一天
	 *
	 */
	public static String getLastDayOfMonth(int year, int month)
	{
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR,year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());

		return lastDayOfMonth;
	}

	// 获得前一月的最后一天
	public static Date getLastDayOfPreviousMonth() {
		String dateFormat = FORMAT_DATE_YMD;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 0);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		return strToDate(sdf.format(lastDate.getTime()), dateFormat);
	}

	// 获得当前月的最后一天
	public static String getLastDayOfMonthStr() {
		String dateFormat = FORMAT_DATE_YMD;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		return sdf.format(lastDate.getTime());
	}

	// 获得上月的第一天
	public static String getFirstDayOfPreviousMonth() {
		String dateFormat = FORMAT_DATE_YMDHMS;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, -1);// 变为当月的1号

		lastDate.set(Calendar.HOUR_OF_DAY, 0);
		lastDate.set(Calendar.MINUTE, 0);
		lastDate.set(Calendar.SECOND, 0);
		lastDate.set(Calendar.MILLISECOND, 0);
		// lastDate.add(Calendar.DATE, -1);//减去一天，变为当月最后一天

		return sdf.format(lastDate.getTime());
	}

	// 获得当上月的最后一天
	public static String getLastDayOfBeforeMonth() {
		String dateFormat = FORMAT_DATE_YMDHMS;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 0);// 变为当月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		lastDate.set(Calendar.HOUR_OF_DAY, 23);
		lastDate.set(Calendar.MINUTE, 59);
		lastDate.set(Calendar.SECOND, 59);
		lastDate.set(Calendar.MILLISECOND, 999);

		return sdf.format(lastDate.getTime());
	}

	// 获得当上月的最后一天
	public static String getLastDayOfBeforeMonthYMD() {
		String dateFormat = FORMAT_DATE_YMD;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 0);// 变为当月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		return sdf.format(lastDate.getTime());
	}

	// 获取当天时间
	public static String getNowDate() {
		String dateFormat = FORMAT_DATE_YMD;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar lastDate = Calendar.getInstance();

		return sdf.format(lastDate.getTime());

	}

	// 获取当月第一天
	public static Date getFirstDayOfMonth() {
		String dateFormat = FORMAT_DATE_YMD;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		return strToDate(sdf.format(lastDate.getTime()), dateFormat);
	}

	// 获取当月第一天
	public static String getFirstDayOfTheMonth(int i) {
		String dateFormat = FORMAT_DATE_YMD;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1 + i);// 设为当前月的1号
		return sdf.format(lastDate.getTime());
	}

	// 获取上月第一天
	public static String getFirstDayOfTheBefordeMonth(int i) {
		String dateFormat = FORMAT_DATE_YMD;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, -1);// 变为上月
		lastDate.set(Calendar.DATE, 1 + i);
		return sdf.format(lastDate.getTime());
	}

	// 获取当天时间
	public static Date getNowTime(String dateformat) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		return strToDate(dateFormat.format(now), dateformat);

	}

	// 获取当天的开始时间
	public static String getStartTime(String date) {
		String dateFromFormat = FORMAT_DATE_YMD;
		String dateToFormat = FORMAT_DATE_YMDHMS;
		SimpleDateFormat dateFormatFrom = new SimpleDateFormat(dateFromFormat);
		SimpleDateFormat dateFormatTo = new SimpleDateFormat(dateToFormat);
		Calendar todayStart = Calendar.getInstance();
		try {
			Date parsedDate = dateFormatFrom.parse(date);

			todayStart.setTime(parsedDate);
			todayStart.set(Calendar.HOUR_OF_DAY, 0);
			todayStart.set(Calendar.MINUTE, 0);
			todayStart.set(Calendar.SECOND, 0);
			todayStart.set(Calendar.MILLISECOND, 0);

		} catch (ParseException ex) {
			ex.printStackTrace();// TODO
		}

		return dateFormatTo.format(todayStart.getTime());

	}

	// 获取当天的开始时间
	public static String getDayStartTime(String date, String dateFromFormat, String dateToFormat) {
		// String dateFromFormat = FORMAT_DATE_YMD;
		// String dateToFormat = FORMAT_DATE_YMDHMS;
		SimpleDateFormat dateFormatFrom = new SimpleDateFormat(dateFromFormat);
		SimpleDateFormat dateFormatTo = new SimpleDateFormat(dateToFormat);
		Calendar todayStart = Calendar.getInstance();
		try {
			Date parsedDate = dateFormatFrom.parse(date);

			todayStart.setTime(parsedDate);
			todayStart.set(Calendar.HOUR_OF_DAY, 0);
			todayStart.set(Calendar.MINUTE, 0);
			todayStart.set(Calendar.SECOND, 0);
			todayStart.set(Calendar.MILLISECOND, 0);

		} catch (ParseException ex) {
			ex.printStackTrace();// TODO
		}

		return dateFormatTo.format(todayStart.getTime());

	}

	// 获取当天的结束时间
	public static String getEndTime(String date) {
		String dateFromFormat = FORMAT_DATE_YMD;
		String dateToFormat = FORMAT_DATE_YMDHMS;
		SimpleDateFormat dateFormatFrom = new SimpleDateFormat(dateFromFormat);
		SimpleDateFormat dateFormatTo = new SimpleDateFormat(dateToFormat);
		Calendar todayEnd = Calendar.getInstance();
		try {
			Date parsedDate = dateFormatFrom.parse(date);
			todayEnd.setTime(parsedDate);
			todayEnd.set(Calendar.HOUR_OF_DAY, 23);
			todayEnd.set(Calendar.MINUTE, 59);
			todayEnd.set(Calendar.SECOND, 59);
			todayEnd.set(Calendar.MILLISECOND, 999);
		} catch (ParseException ex) {
			ex.printStackTrace();// TODO
		}
		return dateFormatTo.format(todayEnd.getTime());
	}

	// 获取当天的结束时间
	public static String getDayEndTime(String date, String dateFromFormat, String dateToFormat) {
		// String dateFromFormat = FORMAT_DATE_YMD;
		// String dateToFormat = FORMAT_DATE_YMDHMS;
		SimpleDateFormat dateFormatFrom = new SimpleDateFormat(dateFromFormat);
		SimpleDateFormat dateFormatTo = new SimpleDateFormat(dateToFormat);
		Calendar todayEnd = Calendar.getInstance();
		try {
			Date parsedDate = dateFormatFrom.parse(date);
			todayEnd.setTime(parsedDate);
			todayEnd.set(Calendar.HOUR_OF_DAY, 23);
			todayEnd.set(Calendar.MINUTE, 59);
			todayEnd.set(Calendar.SECOND, 59);
			todayEnd.set(Calendar.MILLISECOND, 999);
		} catch (ParseException ex) {
			ex.printStackTrace();// TODO
		}
		return dateFormatTo.format(todayEnd.getTime());
	}

	// 获取当天的开始时间
	public static Date getStartDate(Date date) {
		Calendar todayStart = Calendar.getInstance();
		todayStart.setTime(date);
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime();

	}

	// 获取当天的结束时间
	public static Date getEndDate(Date date) {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(date);
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime();
	}

	public static Date getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public static String getYesterdayStr() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat(FORMAT_DATE_YMD).format(cal.getTime());
		return yesterday;
	}

	public static Date computeDate(Date date, int diffDay) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, diffDay);
		date = c.getTime();
		return date;
	}

	public static String dateToString(Date d) {
		return dateToString(d, FORMAT_DATE_YMD);
	}

	public static String dateToString(Date d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}

	public static Date strToDate(String dateStr) {
		return strToDate(dateStr, "MM/dd/yyyy");
	}

	public static Date strParseDate(String dateStr) {
		return strParseDate(dateStr, FORMAT_DATE_YMD);
	}

	public static Date strToDate(String dateStr, String format) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}
		SimpleDateFormat dateFmt = new SimpleDateFormat(format);

		dateFmt.setLenient(false);
		ParsePosition pos = new ParsePosition(0);
		return dateFmt.parse(dateStr, pos);
	}

	public static Date strParseDate(String dateStr, String format) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}
		SimpleDateFormat dateFmt = new SimpleDateFormat(format);

		try {
			return dateFmt.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean compareDate(Date date1, Date date2) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE_YMD);
		return simpleDateFormat.format(date1).equals(simpleDateFormat.format(date2));
	}

	public static String getDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		String DayOfWeek = "";
		switch (dayOfWeek) {
		case 1: {
			DayOfWeek = "Sun";
			break;
		}
		case 2: {
			DayOfWeek = "Mon";
			break;
		}
		case 3: {
			DayOfWeek = "Tue";
			break;
		}
		case 4: {
			DayOfWeek = "Wed";
			break;
		}
		case 5: {
			DayOfWeek = "Thu";
			break;
		}
		case 6: {
			DayOfWeek = "Fri";
			break;
		}
		case 7: {
			DayOfWeek = "Sat";
			break;
		}
		}
		return DayOfWeek;
	}

	public static Integer getIndexOfDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}

	public static Integer getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static boolean checkTodayPassHalfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (day >= 15) {
			return true;
		}
		return false;
	}

	public static Date addMinutes(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, amount);
		return c.getTime();
	}

	public static Date addYear(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, amount);
		return c.getTime();
	}

	public static Date addHours(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, amount);
		return c.getTime();
	}

	public static Date addDays(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, amount);
		return c.getTime();
	}

	public static Date addMonths(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, amount);
		return c.getTime();
	}

	public static Date[] getDateArrays(Date start, Date end, int calendarType) {
		ArrayList<Date> ret = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		Date tmpDate = calendar.getTime();
		long endTime = end.getTime();
		while (tmpDate.before(end) || tmpDate.getTime() == endTime) {
			ret.add(calendar.getTime());
			calendar.add(calendarType, 1);
			tmpDate = calendar.getTime();
		}
		Date[] dates = new Date[ret.size()];
		return (Date[]) ret.toArray(dates);
	}

	public static Date getCurrYearFirst() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearFirst(currentYear);
	}

	public static Date getYearFirst(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	public static int getCurrentYear() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return currentYear;

	}

	public static int getCurrentMonth() {
		Calendar currCal = Calendar.getInstance();
		int currentMonth = currCal.get(Calendar.MONTH);
		return currentMonth+1;
	}

	/**
	   * 得到几天前的时间
	   * @param d
	   * @param day
	   * @return
	   */
	  public static Date getDateBefore(Date d, int day){
	   Calendar now = Calendar.getInstance();
	   now.setTime(d);
	   now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
	   return now.getTime();
	  }

	  /**
	   * 得到几天后的时间
	   * @param d
	   * @param day
	   * @return
	   */
	  public static String getDateAfter(Date d, int day){

	   String dateFormat = FORMAT_DATE_YMDHMS;
	   SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

	   Calendar now = Calendar.getInstance();
	   now.setTime(d);
	   now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
	   return sdf.format(now.getTime());
	  }

	  public static String objToString(Object obj, String pattern) {
		  if (obj == null) {
			  return null;
		  }
		  if (obj instanceof Date) {
			  return format((Date)obj, pattern);
		  } else if (Long.class.isAssignableFrom(obj.getClass()) || long.class.isAssignableFrom(obj.getClass())) {
			  return format(new Date((long)obj), pattern);
		  }
		  return null;
	  }

	  public static Long StringToLong(String date, String pattern) {
		  if (date == null) {
			  return null;
		  }
		  Date d = format(date, pattern);
		  if (d== null)
			  return null;
		  return d.getTime();
	  }


    /**
     * 将字符串转日期成Long类型的时间戳，格式为：yyyy-MM-dd
     */
    public static Long convertTimeToLong(String time) {
        if (time == null || time.equals("")){
            return 0L;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_YMD);
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将字符串转日期成Long类型的时间戳，格式为：yyyy-MM-dd
     */
    public static Long convertTimeToLong(String time, String patten) {
        if (time == null || time.equals("")){
            return 0L;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(patten);
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将long转换为str格式日期
     */
    public static String convertLongToTime(Long time, String patten) {
        if (time == null){
            return null;
        }
        Date date = new Date(time);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(patten);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	/**
	 * 取得日期：年
	 *
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 取得日期：年
	 *
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		return month + 1;
	}

	/**
	 *
	 * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
	 *
	 * @param date
	 * @return
	 */
	public static int getSeason(Date date) {
		int season = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			season = 1;
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			season = 2;
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			season = 3;
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			season = 4;
			break;
		default:
			break;
		}
		return season;
	}

	/**
	 * 比较两个日期相差多少天
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	public static int getDifferDays(Date dateStart, Date dateEnd) {
		return (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
	}

	/**比较两个日期大小
	 * @param date1
	 * @param date2
	 * @Date: Created in 11:35 on 2017/9/30.
	 */
	public static int compareDateNew(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 取得当月天数
	 * */
	public static int getCurrentMonthTotalDay()
	{
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);//把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static String dateToWeek(String datetime)  {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		// 获得一个日历
		Date datet = null;
		try {
			datet = f.parse(datetime);
			cal.setTime(datet);
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			// 指示一个星期中的某天。
			if (w < 0)
				w = 0;
			return weekDays[w];
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return datetime;
	}

	public static int getAge(String date, String pattern){
		return getAge(convertStringToDate(date, pattern));
	}

	public static  int getAge(Date birthDay){
		// 当前时间
		Calendar curr = Calendar.getInstance();
		// 生日
		Calendar born = Calendar.getInstance();
		born.setTime(birthDay);
		// 年龄 = 当前年 - 出生年
		int age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR);
		if (age <= 0) {
			return 0;
		}
		// 如果当前月份小于出生月份: age-1
		// 如果当前月份等于出生月份, 且当前日小于出生日: age-1
		int currMonth = curr.get(Calendar.MONTH);
		int currDay = curr.get(Calendar.DAY_OF_MONTH);
		int bornMonth = born.get(Calendar.MONTH);
		int bornDay = born.get(Calendar.DAY_OF_MONTH);
		if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay <= bornDay)) {
			age--;
		}
		return age < 0 ? 0 : age;
	}

	public static int getSysYear() {
        Calendar date = Calendar.getInstance();
        return date.get(Calendar.YEAR);
    }


}
