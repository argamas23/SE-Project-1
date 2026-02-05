```java
package org.apache.roller.planet.business;

import java.util.Date;
import java.util.List;

// Assuming Planet, PlanetConfig, PlanetGroup, Subscription, SubscriptionEntry, and PlanetException
// are defined in the same package or are properly imported from other packages.
// For example:
// import org.apache.roller.planet.pojos.Planet;
// import org.apache.roller.planet.pojos.PlanetConfig;
// import org.apache.roller.planet.pojos.PlanetGroup;
// import org.apache.roller.planet.pojos.Subscription;
// import org.apache.roller.planet.pojos.SubscriptionEntry;


/**
 * Query object for retrieving SubscriptionEntry objects.
 * Encapsulates filtering and pagination parameters.
 * Addresses the "Long Parameter List" smell for methods that query entries.
 */
public class EntryQuery {
    private PlanetGroup group;
    private Date startDate;
    private Date endDate;
    private int offset;
    private int length;

    public EntryQuery() {
        // Default constructor
    }

    public EntryQuery(PlanetGroup group, Date startDate, Date endDate, int offset, int length) {
        this.group = group;
        this.startDate = startDate;
        this.endDate = endDate;
        this.offset = offset;
        this.length = length;
    }

    public PlanetGroup getGroup() {
        return group;
    }

    public void setGroup(PlanetGroup group) {
        this.group = group;
    }

    public Date getStartDate() {
        return (startDate != null) ? new Date(startDate.getTime()) : null;
    }

    public void setStartDate(Date startDate) {
        this.startDate = (startDate != null) ? new Date(startDate.getTime()) : null;
    }

    public Date getEndDate() {
        return (endDate != null) ? new Date(endDate.getTime()) : null;
    }

    public void setEndDate(Date endDate) {
        this.endDate = (endDate != null) ? new Date(endDate.getTime()) : null;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}

/**
 * Service interface for managing Planet entities and their aggregated content.
 * This refactored interface focuses on the 'Planet' entity and operations related to its
 * overall management, including its configuration and the aggregated entries it displays.
 *
 * This addresses part of the "God Interface" smell by separating concerns for
 * Planet-specific management.
 * It also addresses the "Confusing Naming" smells by consistently using "Planet"
 * instead of "Weblogger" when referring to the primary entity managed by this service.
 */
public interface PlanetService {

    /**
     * Save a Planet entity.
     * @param planet The Planet entity to save.
     * @throws PlanetException If an error occurs during saving.
     */
    void savePlanet(Planet planet) throws PlanetException;

    /**
     * Retrieve a Planet entity by its handle.
     * @param handle The unique handle of the Planet.
     * @return The Planet entity, or null if not found.
     * @throws PlanetException If an error occurs during retrieval.
     */
    Planet getPlanet(String handle) throws PlanetException;

    /**
     * Retrieve a Planet entity by its ID.
     * @param id The unique ID of the Planet.
     * @return The Planet entity, or null if not found.
     * @throws PlanetException If an error occurs during retrieval.
     */
    Planet getPlanetById(String id) throws PlanetException;

    /**
     * Delete a Planet entity.
     * @param planet The Planet entity to delete.
     * @throws PlanetException If an error occurs during deletion.
     */
    void deletePlanet(Planet planet) throws PlanetException;

    /**
     * Refresh the content associated with a specific Planet.
     * This typically involves fetching new entries from its subscriptions.
     * @param planet The Planet to refresh.
     * @throws PlanetException If an error occurs during refresh.
     */
    void refreshPlanet(Planet planet) throws PlanetException;

    /**
     * Retrieve the global Planet configuration.
     * @return The PlanetConfig object.
     * @throws PlanetException If an error occurs during retrieval.
     */
    PlanetConfig getPlanetConfig() throws PlanetException;

    /**
     * Save the global Planet configuration.
     * @param config The PlanetConfig object to save.
     * @throws PlanetException If an error occurs during saving.
     */
    void savePlanetConfig(PlanetConfig config) throws PlanetException;

    /**
     * Retrieve a list of aggregated SubscriptionEntry objects based on a query.
     * Addresses "Long Parameter List" smell for getEntries.
     * @param query The EntryQuery object specifying filter and pagination criteria.
     * @return A list of matching SubscriptionEntry objects.
     * @throws PlanetException If an error occurs during retrieval.
     */
    List<SubscriptionEntry> getEntries(EntryQuery query) throws PlanetException;

    /**
     * Retrieve a list of new SubscriptionEntry objects since a given date.
     * @param date The cutoff date. Only entries newer than this date are returned.
     * @param offset The starting index of the results.
     * @param len The maximum number of results to return.
     * @return A list of new SubscriptionEntry objects.
     * @throws PlanetException If an error occurs during retrieval.
     */
    List<SubscriptionEntry> getNewEntries(Date date, int offset, int len) throws PlanetException;

    /**
     * Trigger a refresh for all managed entities (Planets, Groups, Subscriptions).
     * This is a high-level, overarching refresh operation.
     * @throws PlanetException If an error occurs during the refresh process.
     */
    void refreshAll() throws PlanetException;
}

/**
 * Service interface for managing PlanetGroup entities.
 * This interface is responsible for CRUD operations on groups and managing
 * the association between Planets and their groups.
 * Addresses part of the "God Interface" smell by separating concerns for
 * PlanetGroup-specific management.
 */
public interface PlanetGroupService {

    /**
     * Save a PlanetGroup entity.
     * @param group The PlanetGroup entity to save.
     * @throws PlanetException If an error occurs during saving.
     */
    void saveGroup(PlanetGroup group) throws PlanetException;

    /**
     * Retrieve a PlanetGroup entity by its handle.
     * @param handle The unique handle of the group.
     * @return The PlanetGroup entity, or null if not found.
     * @throws PlanetException If an error occurs during retrieval.
     */
    PlanetGroup getGroup(String handle) throws PlanetException;

    /**
     * Retrieve a PlanetGroup entity by its ID.
     * @param id The unique ID of the group.
     * @return The PlanetGroup entity, or null if not found.
     * @throws PlanetException If an error occurs during retrieval.
     */
    PlanetGroup getGroupById(String id) throws PlanetException;

    /**
     * Retrieve all PlanetGroup entities.
     * @return A list of all PlanetGroup entities.
     * @throws PlanetException If an error occurs during retrieval.
     */
    List<PlanetGroup> getAllGroups() throws PlanetException;

    /**
     * Delete a PlanetGroup entity.
     * @param group The PlanetGroup entity to delete.
     * @throws PlanetException If an error occurs during deletion.
     */
    void deleteGroup(PlanetGroup group) throws PlanetException;

    /**
     * Add a Planet to a specific PlanetGroup.
     * @param planet The Planet to add.
     * @param group The PlanetGroup to add the Planet to.
     * @throws PlanetException If an error occurs during the operation.
     */
    void addPlanetToGroup(Planet planet, PlanetGroup group) throws PlanetException;

    /**
     * Remove a Planet from a specific PlanetGroup.
     * @param planet The Planet to remove.
     * @param group The PlanetGroup to remove the Planet from.
     * @throws PlanetException If an error occurs during the operation.
     */
    void removePlanetFromGroup(Planet planet, PlanetGroup group) throws PlanetException;

    /**
     * Retrieve all Planet entities associated with a specific PlanetGroup.
     * @param group The PlanetGroup.
     * @return A list of Planet entities in the group.
     * @throws PlanetException If an error occurs during retrieval.
     */
    List<Planet> getPlanetsInGroup(PlanetGroup group) throws PlanetException;

    /**
     * Retrieve all Planet entities not associated with a specific PlanetGroup.
     * @param group The PlanetGroup.
     * @return A list of Planet entities not in the group.
     * @throws PlanetException If an error occurs during retrieval.
     */
    List<Planet> getPlanetsNotInGroup(PlanetGroup group) throws PlanetException;

    /**
     * Refresh the content associated with a specific PlanetGroup.
     * This typically involves refreshing subscriptions within the group.
     * @param group The PlanetGroup to refresh.
     * @throws PlanetException If an error occurs during refresh.
     */
    void refreshGroup(PlanetGroup group) throws PlanetException;
}

/**
 * Service interface for managing Subscription entities.
 * This interface handles CRUD operations for subscriptions, their grouping,
 * and operations related to the entries owned by these subscriptions.
 * Addresses part of the "God Interface" smell by separating concerns for
 * Subscription-specific management.
 */
public interface SubscriptionService {

    /**
     * Save a Subscription entity.
     * @param sub The Subscription entity to save.
     * @throws PlanetException If an error occurs during saving.
     */
    void saveSubscription(Subscription sub) throws PlanetException;

    /**
     * Retrieve a Subscription entity by its feed URL.
     * @param feedURL The unique feed URL of the subscription.
     * @return The Subscription entity, or null if not found.
     * @throws PlanetException If an error occurs during retrieval.
     */
    Subscription getSubscription(String feedURL) throws PlanetException;

    /**
     * Retrieve a Subscription entity by its ID.
     * @param id The unique ID of the subscription.
     * @return The Subscription entity, or null if not found.
     * @throws PlanetException If an error occurs during retrieval.
     */
    Subscription getSubscriptionById(String id) throws PlanetException;

    /**
     * Retrieve all Subscription entities.
     * @return A list of all Subscription entities.
     * @throws PlanetException If an error occurs during retrieval.
     */
    List<Subscription> getAllSubscriptions() throws PlanetException;

    /**
     * Delete a Subscription entity.
     * @param sub The Subscription entity to delete.
     * @throws PlanetException If an error occurs during deletion.
     */
    void deleteSubscription(Subscription sub) throws PlanetException;

    /**
     * Retrieve all Subscription entities associated with a specific PlanetGroup.
     * @param group The PlanetGroup.
     * @return A list of Subscription entities in the group.
     * @throws PlanetException If an error occurs during retrieval.
     */
    List<Subscription> getSubscriptionsInGroup(PlanetGroup group) throws PlanetException;

    /**
     * Add a Subscription to a specific PlanetGroup.
     * @param sub The Subscription to add.
     * @param group The PlanetGroup to add the Subscription to.
     * @throws PlanetException If an error occurs during the operation.
     */
    void addSubscriptionToGroup(Subscription sub, PlanetGroup group) throws PlanetException;

    /**
     * Remove a Subscription from a specific PlanetGroup.
     * @param sub The Subscription to remove.
     * @param group The PlanetGroup to remove the Subscription from.
     * @throws PlanetException If an error occurs during the operation.
     */
    void removeSubscriptionFromGroup(Subscription sub, PlanetGroup group) throws PlanetException;

    /**
     * Refresh the content associated with a specific Subscription.
     * This typically involves fetching new entries from its feed URL.
     * @param sub The Subscription to refresh.
     * @throws PlanetException If an error occurs during refresh.
     */
    void refreshSubscription(Subscription sub) throws PlanetException;

    /**
     * Refresh all Subscription entities managed by the system.
     * @throws PlanetException If an error occurs during the refresh process.
     */
    void refreshAllSubscriptions() throws PlanetException;

    /**
     * Retrieve all SubscriptionEntry objects belonging to a specific Subscription.
     * @param sub The Subscription.
     * @return A list of SubscriptionEntry objects for the given subscription.
     * @throws PlanetException If an error occurs during retrieval.
     */
    List<SubscriptionEntry> getEntriesBySubscription(Subscription sub) throws PlanetException;

    /**
     * Delete all SubscriptionEntry objects belonging to a specific Subscription.
     * @param sub The Subscription whose entries are to be deleted.
     * @throws PlanetException If an error occurs during deletion.
     */
    void deleteEntriesBySubscription(Subscription sub) throws PlanetException;
}

/**
 * Service interface for managing individual SubscriptionEntry entities.
 * This interface provides basic CRUD operations for single entries, independent
 * of their parent subscription or aggregation context.
 * Addresses part of the "God Interface" smell by separating concerns for
 * SubscriptionEntry-specific management.
 */
public interface SubscriptionEntryService {

    /**
     * Save a SubscriptionEntry entity.
     * @param entry The SubscriptionEntry entity to save.
     * @throws PlanetException If an error occurs during saving.
     */
    void saveEntry(SubscriptionEntry entry) throws PlanetException;

    /**
     * Retrieve a SubscriptionEntry entity by its permalink.
     * @param permalink The unique permalink of the entry.
     * @return The SubscriptionEntry entity, or null if not found.
     * @throws PlanetException If an error occurs during retrieval.
     */
    SubscriptionEntry getEntry(String permalink) throws PlanetException;

    /**
     * Retrieve a SubscriptionEntry entity by its ID.
     * @param id The unique ID of the entry.
     * @return The SubscriptionEntry entity, or null if not found.
     * @throws PlanetException If an error occurs during retrieval.
     */
    SubscriptionEntry getEntryById(String id) throws PlanetException;

    /**
     * Delete a SubscriptionEntry entity.
     * @param entry The SubscriptionEntry entity to delete.
     * @throws PlanetException If an error occurs during deletion.
     */
    void deleteEntry(SubscriptionEntry entry) throws PlanetException;
}
```