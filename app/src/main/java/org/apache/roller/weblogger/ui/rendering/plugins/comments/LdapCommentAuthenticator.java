package org.apache.roller.weblogger.ui.rendering.plugins.comments;

import org.apache.roller.weblogger.config.WebloggerRuntimeConfig;
import org.apache.roller.weblogger.config.WebloggerConstants;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.pojos.Weblog;
import org.apache.roller.weblogger.ui.rendering.plugins.comments.CommentAuthenticator;
import org.apache.roller.util.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;
import java.util.Properties;

public class LdapCommentAuthenticator implements CommentAuthenticator {

    private static final Log LOG = LogFactory.getLog(LdapCommentAuthenticator.class);
    private static final String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    private static final String LDAP_AUTHENTICATION = "simple";
    private static final int LDAP_PORT = 389;
    private static final int MAX_SEARCH_RESULTS = 1000;

    private String ldapServer;
    private String ldapBase;
    private String ldapUsername;
    private String ldapPassword;
    private String ldapUserAttribute;
    private String ldapEmailAttribute;

    public LdapCommentAuthenticator(String ldapServer, String ldapBase, String ldapUsername, String ldapPassword, String ldapUserAttribute, String ldapEmailAttribute) {
        this.ldapServer = ldapServer;
        this.ldapBase = ldapBase;
        this.ldapUsername = ldapUsername;
        this.ldapPassword = ldapPassword;
        this.ldapUserAttribute = ldapUserAttribute;
        this.ldapEmailAttribute = ldapEmailAttribute;
    }

    @Override
    public boolean authenticateUsername(Weblog weblog, String username, String password) {
        return authenticateUsername(weblog, username, password, false);
    }

    @Override
    public boolean authenticateUsername(Weblog weblog, String username, String password, boolean createIfNotFound) {
        try {
            DirContext context = createLdapContext(ldapServer, ldapUsername, ldapPassword);
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            controls.setCountLimit(MAX_SEARCH_RESULTS);
            String filter = "(" + ldapUserAttribute + "=" + username + ")";
            NamingEnumeration<SearchResult> results = context.search(ldapBase, filter, controls);
            if (results.hasMore()) {
                return authenticateUser(results, password);
            } else if (createIfNotFound) {
                return createUser(context, username, password);
            }
        } catch (NamingException e) {
            LOG.error("Error authenticating user", e);
        }
        return false;
    }

    private DirContext createLdapContext(String ldapServer, String ldapUsername, String ldapPassword) throws NamingException {
        Properties environment = new Properties();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
        environment.put(Context.PROVIDER_URL, "ldap://" + ldapServer + ":" + LDAP_PORT);
        environment.put(Context.SECURITY_AUTHENTICATION, LDAP_AUTHENTICATION);
        environment.put(Context.SECURITY_PRINCIPAL, ldapUsername);
        environment.put(Context.SECURITY_CREDENTIALS, ldapPassword);
        return new InitialDirContext(environment);
    }

    private boolean authenticateUser(NamingEnumeration<SearchResult> results, String password) throws NamingException {
        SearchResult result = results.next();
        Attributes attributes = result.getAttributes();
        Attribute emailAttribute = attributes.get(ldapEmailAttribute);
        if (emailAttribute != null) {
            String email = (String) emailAttribute.get();
            return password.equals(email);
        }
        return false;
    }

    private boolean createUser(DirContext context, String username, String password) {
        try {
            Attributes attributes = new Attributes();
            attributes.put(ldapUserAttribute, username);
            attributes.put(ldapEmailAttribute, password);
            context.createSubcontext(ldapBase + "," + ldapUserAttribute + "=" + username, attributes);
            return true;
        } catch (NamingException e) {
            LOG.error("Error creating user", e);
            return false;
        }
    }

    @Override
    public String getType() {
        return "LDAP";
    }

    @Override
    public String getDescription() {
        return "LDAP Comment Authenticator";
    }
}