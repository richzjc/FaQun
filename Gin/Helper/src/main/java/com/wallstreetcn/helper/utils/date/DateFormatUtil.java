package com.wallstreetcn.helper.utils.date;

import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.text.TextUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Sky on 2016/5/20.
 */
public class DateFormatUtil {

    public static long DAY_SECOND = 24 * 60 * 60;

    public static String dateFormat(long unixTime) {//unixTime单位秒
        try {
            long currentTime = System.currentTimeMillis() / 1000;
            long deltaTime = currentTime - unixTime;


            if (deltaTime > 10 * 60) {//一天前
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                Calendar uCalendar = Calendar.getInstance();
                uCalendar.setTime(new Date(unixTime * 1000));
                int day = c.get(Calendar.DAY_OF_YEAR);
                if (year == uCalendar.get(Calendar.YEAR)) {
                    if (day == uCalendar.get((Calendar.DAY_OF_YEAR))) {
                        return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_n_hours_ago),
                                Locale.CHINA).format(new Date(unixTime * 1000));
                    } else {
                        return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_m_d_h_m),
                                Locale.CHINA).format(new Date(unixTime * 1000));
                    }
                } else {
                    return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_y_m_d_h_m),
                            Locale.CHINA).format(new Date(unixTime * 1000));
                }
            } else if (deltaTime > 10 * 60) {//一小時內
                return String.format(ResourceUtils.getResStringFromId(R.string.helper_n_min_ago), Math.round(deltaTime / 60f));
            } else {
                return ResourceUtils.getResStringFromId(R.string.helper_just_text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String dateFormatJiaSheng(long unixTime) {//unixTime单位秒
        try {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            Calendar uCalendar = Calendar.getInstance();
            uCalendar.setTime(new Date(unixTime * 1000));
            int day = c.get(Calendar.DAY_OF_YEAR);
            if (year == uCalendar.get(Calendar.YEAR)) {
                if (day == uCalendar.get((Calendar.DAY_OF_YEAR))) {
                    return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_n_hours_ago),
                            Locale.CHINA).format(new Date(unixTime * 1000));
                } else {
                    return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_m_d_h_m),
                            Locale.CHINA).format(new Date(unixTime * 1000));
                }
            } else {
                return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_y_m_d_h_m),
                        Locale.CHINA).format(new Date(unixTime * 1000));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateFormatNew(long unixTime) {//unixTime单位秒
        try {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            Calendar uCalendar = Calendar.getInstance();
            uCalendar.setTime(new Date(unixTime * 1000));
            int day = c.get(Calendar.DAY_OF_YEAR);
            if (year == uCalendar.get(Calendar.YEAR)) {
                if (day == uCalendar.get((Calendar.DAY_OF_YEAR))) {
                    return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_n_hours_ago),
                            Locale.CHINA).format(new Date(unixTime * 1000));
                } else {
                    return new SimpleDateFormat("MM-dd",
                            Locale.CHINA).format(new Date(unixTime * 1000));
                }
            } else {
                return new SimpleDateFormat("yyyy-MM-dd",
                        Locale.CHINA).format(new Date(unixTime * 1000));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String dateFormatChartDetail(long unixTime) {//unixTime单位秒
        try {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            Calendar uCalendar = Calendar.getInstance();
            uCalendar.setTime(new Date(unixTime * 1000));
            if (year == uCalendar.get(Calendar.YEAR)) {
                return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_m_d_h_m),
                        Locale.CHINA).format(new Date(unixTime * 1000));
            } else {
                return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_y_m_d_h_m),
                        Locale.CHINA).format(new Date(unixTime * 1000));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String dateFormatNoHM(long unixTime) {//unixTime单位秒
        try {
            long currentTime = System.currentTimeMillis() / 1000;
            long deltaTime = currentTime - unixTime;
            if (deltaTime > 10 * 60) {//一天前
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                Calendar uCalendar = Calendar.getInstance();
                uCalendar.setTime(new Date(unixTime * 1000));
                int day = c.get(Calendar.DAY_OF_YEAR);
                if (year == uCalendar.get(Calendar.YEAR)) {
                    if (day == uCalendar.get((Calendar.DAY_OF_YEAR))) {
                        return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_n_hours_ago),
                                Locale.CHINA).format(new Date(unixTime * 1000));
                    } else {
                        return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_m_d),
                                Locale.CHINA).format(new Date(unixTime * 1000));
                    }
                } else {
                    return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_y_m_d),
                            Locale.CHINA).format(new Date(unixTime * 1000));
                }
            } else if (deltaTime > 60 * 10) {//一天內
                return new SimpleDateFormat(ResourceUtils.getResStringFromId(R.string.helper_m_d), Locale.CHINA).format(new Date(unixTime * 1000));
            } else if (deltaTime > 60) {//一小時內
                return String.format(ResourceUtils.getResStringFromId(R.string.helper_n_min_ago), deltaTime / 60);
            } else if (deltaTime > 30) {
                return String.format(ResourceUtils.getResStringFromId(R.string.helper_seconds_ago), deltaTime);
            } else {//一分以內  其他情况 包括各种其他情况
                return ResourceUtils.getResStringFromId(R.string.helper_just_text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String compareDate(Date now, Date compare, String format) {
        try {
            StringBuilder sb = new StringBuilder();
            if (compare.getTime() <= 0.00001) {
                sb.append(ResourceUtils.getResStringFromId(R.string.helper_just_text));
                return sb.toString();
            }
            long l = now.getTime() - compare.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);

            if (Math.abs(day) > 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                        format, Locale.CHINA);
                return simpleDateFormat.format(compare);
            }

            if (hour > 0) {
                sb.append(hour).append(ResourceUtils.getResStringFromId(R.string.helper_hours_ago));

            }

            if (min > 0 && hour < 1) {
                sb.append(min).append(ResourceUtils.getResStringFromId(R.string.helper_minutes_ago));
            }

            if (hour == 0 && min == 0) {
                sb.append(ResourceUtils.getResStringFromId(R.string.helper_just_text));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis * 1000));
    }


    public static String getTime(long timeInMillis, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return getTime(timeInMillis, dateFormat);
    }


    public static String get24HMSTime(long time) {
        String s = new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(time * 1000));
        return s;
    }

    public static String getTime(Calendar calendar, SimpleDateFormat format) {
        return format.format(calendar.getTime());
    }

    public static double daysFarNow(long seconds) {
        long time = seconds - System.currentTimeMillis() / 1000;
        return time / (60 * 60 * 24);
    }

    public static String secondToDHHMMSS(long time) {
        long HH = time / (60 * 60);
        time = time - HH * 60 * 60;
        long MM = time / 60;
        time = time - MM * 60;
        long SS = time;
        return TextUtil.format("%02d:%02d:%02d", HH, MM, SS);
    }

    public static String secondToMMSS(long time) {
        long MM = time / 60;
        time = time - MM * 60;
        long SS = time;
        return TextUtil.format("%02d:%02d", MM, SS);
    }

    //获取指定秒数的对应星期
    public static String getWeek(long second) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(second * 1000);
        String week = "";
        int cweek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (cweek) {
            case 1:
                week = ResourceUtils.getResStringFromId(R.string.helper_day_text);
                break;
            case 2:
                week = ResourceUtils.getResStringFromId(R.string.helper_one_text);
                break;
            case 3:
                week = ResourceUtils.getResStringFromId(R.string.helper_two_text);
                break;
            case 4:
                week = ResourceUtils.getResStringFromId(R.string.helper_three_text);
                break;
            case 5:
                week = ResourceUtils.getResStringFromId(R.string.helper_four_text);
                break;
            case 6:
                week = ResourceUtils.getResStringFromId(R.string.helper_five_text);
                break;
            case 7:
                week = ResourceUtils.getResStringFromId(R.string.helper_six_text);
                break;
        }
        return week;

    }

    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            long now = sdf.parse(curTime).getTime();
            long start = sdf.parse(args[0]).getTime();
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            long end = sdf.parse(args[1]).getTime();
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
    }

    public static String formatData(Date date, String format) {
        SimpleDateFormat messageDateFormat = new SimpleDateFormat(format);
        return messageDateFormat.format(date);
    }
}
