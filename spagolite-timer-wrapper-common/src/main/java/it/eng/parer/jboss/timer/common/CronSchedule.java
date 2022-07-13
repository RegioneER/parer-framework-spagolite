package it.eng.parer.jboss.timer.common;

import java.io.Serializable;

/**
 *
 * @author Fioravanti_F
 */
public class CronSchedule implements Serializable {

    private static final long serialVersionUID = 4837359509408376849L;

    private String hour;
    private String minute;
    private String dayOfMonth;
    private String month;
    private String dayOfWeek;

    public CronSchedule() {

    }

    public CronSchedule(String hour, String minute, String dayOfMonth, String month, String dayOfWeek) {
        this.hour = hour;
        this.minute = minute;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
