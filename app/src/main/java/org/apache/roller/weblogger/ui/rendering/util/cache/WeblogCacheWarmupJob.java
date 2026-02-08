package org.apache.roller.weblogger.ui.rendering.util.cache;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.business.WeblogEntryManager;
import org.apache.roller.weblogger.business.WeblogManager;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntry;
import org.apache.roller.weblogger.util.RollerContext;
import org.apache.roller.weblogger.util.UIUtils;
import org.apache.roller.weblogger.ui.rendering.util.cache.strategies.AllEntriesStrategy;
import org.apache.roller.weblogger.ui.rendering.util.cache.strategies.CategoryStrategy;
import org.apache.roller.weblogger.ui.rendering.util.cache.strategies.DayStrategy;
import org.apache.roller.weblogger.ui.rendering.util.cache.strategies.TagStrategy;
import org.apache.roller.weblogger.ui.rendering.util.cache.strategies.WeekStrategy;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WeblogCacheWarmupJob {

    private static final String ALL_ENTRIES = "all_entries";
    private static final String CATEGORY = "category";
    private static final String DAY = "day";
    private static final String TAG = "tag";
    private static final String WEEK = "week";

    private WeblogManager weblogManager;
    private WeblogEntryManager weblogEntryManager;

    public WeblogCacheWarmupJob() {
        WebloggerFactory factory = WebloggerFactory.getFactory();
        this.weblogManager = factory.getWeblogManager();
        this.weblogEntryManager = factory.getWeblogEntryManager();
    }

    public void warmUp() {
        List<Weblog> weblogs = weblogManager.getAllWeblogs();
        for (Weblog weblog : weblogs) {
            warmUpWeblog(weblog);
        }
    }

    private void warmUpWeblog(Weblog weblog) {
        warmUpAllEntries(weblog);
        warmUpCategories(weblog);
        warmUpTags(weblog);
        warmUpDays(weblog);
        warmUpWeeks(weblog);
    }

    private void warmUpAllEntries(Weblog weblog) {
        AllEntriesStrategy strategy = new AllEntriesStrategy(weblog);
        strategy.warmUp();
    }

    private void warmUpCategories(Weblog weblog) {
        List<String> categories = weblogEntryManager.getWeblogCategories(weblog);
        for (String category : categories) {
            CategoryStrategy strategy = new CategoryStrategy(weblog, category);
            strategy.warmUp();
        }
    }

    private void warmUpTags(Weblog weblog) {
        List<String> tags = weblogEntryManager.getWeblogTags(weblog);
        for (String tag : tags) {
            TagStrategy strategy = new TagStrategy(weblog, tag);
            strategy.warmUp();
        }
    }

    private void warmUpDays(Weblog weblog) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            Date date = calendar.getTime();
            DayStrategy strategy = new DayStrategy(weblog, date);
            strategy.warmUp();
        }
    }

    private void warmUpWeeks(Weblog weblog) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        for (int i = 0; i < 4; i++) {
            calendar.add(Calendar.WEEK_OF_YEAR, -i);
            Date date = calendar.getTime();
            WeekStrategy strategy = new WeekStrategy(weblog, date);
            strategy.warmUp();
        }
    }
}