package org.apache.roller.weblogger.ui.rendering.model;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.util.DateUtil;
import org.apache.roller.weblogger.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarModel {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private Weblog weblog;
    private int year;
    private int month;
    private List<WeblogEntry> entries;
    private List<Integer> daysInMonth;
    private int firstDayOfWeek;

    public CalendarModel(Weblog weblog, int year, int month) {
        this.weblog = weblog;
        this.year = year;
        this.month = month;
        this.entries = new ArrayList<>();
        this.daysInMonth = new ArrayList<>();
        this.firstDayOfWeek = getFirstDayOfWeek();
    }

    public void init() {
        loadEntries();
        loadDaysInMonth();
    }

    private void loadEntries() {
        WeblogEntryManager entryManager = WebloggerFactory.getWeblogger().getWeblogEntryManager();
        try {
            entries = entryManager.getRecentWeblogEntries(weblog, 100);
        } catch (WebloggerException e) {
            // handle exception
        }
    }

    private void loadDaysInMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= numDays; i++) {
            daysInMonth.add(i);
        }
    }

    private int getFirstDayOfWeek() {
        Locale locale = WebloggerRuntimeConfig.getLocale();
        return Calendar.getInstance(locale).getFirstDayOfWeek();
    }

    public List<WeblogEntry> getEntries() {
        return entries;
    }

    public List<Integer> getDaysInMonth() {
        return daysInMonth;
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public boolean isEntryOnDay(int day) {
        for (WeblogEntry entry : entries) {
            if (isEntryOnDate(entry.getPubTime(), day)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEntryOnDate(long date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == day;
    }

    public String getEntryLink(int day) {
        for (WeblogEntry entry : entries) {
            if (isEntryOnDate(entry.getPubTime(), day)) {
                return StringUtils.getString("entry.link", entry.getPermalink());
            }
        }
        return null;
    }

    public String getDayLink(int day) {
        return StringUtils.getString("day.link", getYear(), getMonth(), day);
    }

    public String getMonthName() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, WebloggerRuntimeConfig.getLocale());
    }
}