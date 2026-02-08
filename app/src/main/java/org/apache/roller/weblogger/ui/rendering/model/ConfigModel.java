package org.apache.roller.weblogger.ui.rendering.model;

public class ConfigModel {
    
    // Constants to replace magic strings
    private static final String WEBLOG_HANDLER = "weblog";
    private static final String CATEGORY_HANDLER = "category";
    private static final String ENTRY_HANDLER = "entry";
    private static final String COMMENT_HANDLER = "comment";
    private static final String DAY_HANDLER = "day";
    private static final String MONTH_HANDLER = "month";
    private static final String YEAR_HANDLER = "year";
    
    // Enum to replace repeated conditional statements and switching on type
    private enum HandlerType {
        WEBLOG,
        CATEGORY,
        ENTRY,
        COMMENT,
        DAY,
        MONTH,
        YEAR
    }
    
    // Data classes to avoid data clumps
    private static class Config {
        private String handler;
        private String type;
        
        public Config(String handler, String type) {
            this.handler = handler;
            this.type = type;
        }
        
        public String getHandler() {
            return handler;
        }
        
        public String getType() {
            return type;
        }
    }
    
    // Method to create config object based on handler type
    private Config createConfig(HandlerType handlerType) {
        String handler = null;
        String type = null;
        
        switch (handlerType) {
            case WEBLOG:
                handler = WEBLOG_HANDLER;
                type = WEBLOG_HANDLER;
                break;
            case CATEGORY:
                handler = CATEGORY_HANDLER;
                type = CATEGORY_HANDLER;
                break;
            case ENTRY:
                handler = ENTRY_HANDLER;
                type = ENTRY_HANDLER;
                break;
            case COMMENT:
                handler = COMMENT_HANDLER;
                type = COMMENT_HANDLER;
                break;
            case DAY:
                handler = DAY_HANDLER;
                type = DAY_HANDLER;
                break;
            case MONTH:
                handler = MONTH_HANDLER;
                type = MONTH_HANDLER;
                break;
            case YEAR:
                handler = YEAR_HANDLER;
                type = YEAR_HANDLER;
                break;
        }
        
        return new Config(handler, type);
    }
    
    // Method to process config objects
    private void processConfig(Config config) {
        // Logic to process config object
    }
    
    // Method to handle different types of handlers
    private void handle(HandlerType handlerType) {
        Config config = createConfig(handlerType);
        processConfig(config);
    }
    
    public void init() {
        // Initialize config model
        handle(HandlerType.WEBLOG);
        handle(HandlerType.CATEGORY);
        handle(HandlerType.ENTRY);
        handle(HandlerType.COMMENT);
        handle(HandlerType.DAY);
        handle(HandlerType.MONTH);
        handle(HandlerType.YEAR);
    }
}