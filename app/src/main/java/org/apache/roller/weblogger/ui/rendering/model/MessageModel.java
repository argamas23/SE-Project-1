package org.apache.roller.weblogger.ui.rendering.model;

public class MessageModel {

    private static final int MAX_MESSAGES = 9;

    private String[] messages;
    private MessageSource messageSource;

    public MessageModel() {
        this.init();
    }

    private void init() {
        this.messageSource = new MessageSource();
        this.messages = new MessageRetriever(messageSource).retrieveMessages(MAX_MESSAGES);
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }

    private static class MessageSource {
        // Implementation details
    }

    private static class MessageRetriever {
        private final MessageSource messageSource;

        public MessageRetriever(MessageSource messageSource) {
            this.messageSource = messageSource;
        }

        public String[] retrieveMessages(int maxMessages) {
            // Implementation details
            return new String[maxMessages];
        }
    }
}