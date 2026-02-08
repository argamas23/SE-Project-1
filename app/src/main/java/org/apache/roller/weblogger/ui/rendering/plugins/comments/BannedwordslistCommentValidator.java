package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import java.util.ResourceBundle;
import java.util.Set;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.pojos.WeblogEntryComment;
import org.apache.roller.weblogger.ui.rendering.plugins.comments.CommentValidator;
import org.apache.roller.weblogger.util.RollerMessages;
import org.apache.roller.weblogger.util.StringUtils;

public class BannedwordslistCommentValidator implements CommentValidator {

    private static final String DEFAULT_BANNED_WORDS_RESOURCE = "bannedwordslist";
    private static final int MAX_WORD_LENGTH = 50;
    private static final int MIN_WORD_LENGTH = 2;

    private ResourceBundle bannedWordsResource;
    private Set<String> bannedWords;

    @Override
    public void init(WebloggerRuntimeConfig config) {
        bannedWordsResource = ResourceBundle.getBundle(DEFAULT_BANNED_WORDS_RESOURCE);
        bannedWords = StringUtils.splitAndTrim(bannedWordsResource.getString("banned.words"), ",");
    }

    @Override
    public boolean validate(WeblogEntryComment comment, Weblog weblog, RollerMessages messages) {
        if (bannedWords == null || bannedWords.isEmpty()) {
            return true;
        }

        String commentText = comment.getCommentText();
        for (String bannedWord : bannedWords) {
            if (commentText.contains(bannedWord) && bannedWord.length() >= MIN_WORD_LENGTH && bannedWord.length() <= MAX_WORD_LENGTH) {
                messages.addError("comment.banned.word", bannedWord);
                return false;
            }
        }

        return true;
    }
}