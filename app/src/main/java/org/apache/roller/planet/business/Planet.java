package org.apache.roller.planet.business;

import org.apache.roller.planet.PlanetException;
import org.apache.roller.planet.PlanetFeed;
import org.apache.roller.planet.PlanetFeedMetadata;
import org.apache.roller.planet.PlanetMember;
import org.apache.roller.planet.PlanetMetadata;

import java.util.List;

/**
 * Interface for planet business logic.
 */
public interface Planet {

    /**
     * Loads a planet by its id.
     * 
     * @param id the id of the planet
     * @return the loaded planet
     * @throws PlanetException if the planet cannot be loaded
     */
    PlanetMetadata loadPlanetById(String id) throws PlanetException;

    /**
     * Creates a new planet.
     * 
     * @param metadata the metadata for the new planet
     * @return the created planet
     * @throws PlanetException if the planet cannot be created
     */
    PlanetMetadata createPlanet(PlanetMetadata metadata) throws PlanetException;

    /**
     * Updates an existing planet.
     * 
     * @param metadata the updated metadata for the planet
     * @return the updated planet
     * @throws PlanetException if the planet cannot be updated
     */
    PlanetMetadata updatePlanet(PlanetMetadata metadata) throws PlanetException;

    /**
     * Deletes a planet by its id.
     * 
     * @param id the id of the planet to delete
     * @throws PlanetException if the planet cannot be deleted
     */
    void deletePlanet(String id) throws PlanetException;

    /**
     * Adds a feed to a planet.
     * 
     * @param planetId the id of the planet
     * @param feed the feed to add
     * @return the added feed
     * @throws PlanetException if the feed cannot be added
     */
    PlanetFeed addFeedToPlanet(String planetId, PlanetFeed feed) throws PlanetException;

    /**
     * Removes a feed from a planet.
     * 
     * @param planetId the id of the planet
     * @param feedId the id of the feed to remove
     * @throws PlanetException if the feed cannot be removed
     */
    void removeFeedFromPlanet(String planetId, String feedId) throws PlanetException;

    /**
     * Gets the feeds for a planet.
     * 
     * @param planetId the id of the planet
     * @return the feeds for the planet
     * @throws PlanetException if the feeds cannot be retrieved
     */
    List<PlanetFeed> getFeedsForPlanet(String planetId) throws PlanetException;

    /**
     * Loads a feed by its id.
     * 
     * @param feedId the id of the feed
     * @return the loaded feed
     * @throws PlanetException if the feed cannot be loaded
     */
    PlanetFeed loadFeedById(String feedId) throws PlanetException;

    /**
     * Adds a member to a planet.
     * 
     * @param planetId the id of the planet
     * @param member the member to add
     * @return the added member
     * @throws PlanetException if the member cannot be added
     */
    PlanetMember addMemberToPlanet(String planetId, PlanetMember member) throws PlanetException;

    /**
     * Removes a member from a planet.
     * 
     * @param planetId the id of the planet
     * @param memberId the id of the member to remove
     * @throws PlanetException if the member cannot be removed
     */
    void removeMemberFromPlanet(String planetId, String memberId) throws PlanetException;

    /**
     * Gets the members for a planet.
     * 
     * @param planetId the id of the planet
     * @return the members for the planet
     * @throws PlanetException if the members cannot be retrieved
     */
    List<PlanetMember> getMembersForPlanet(String planetId) throws PlanetException;

    /**
     * Loads a member by its id.
     * 
     * @param memberId the id of the member
     * @return the loaded member
     * @throws PlanetException if the member cannot be loaded
     */
    PlanetMember loadMemberById(String memberId) throws PlanetException;

    /**
     * Creates a new feed metadata object.
     * 
     * @param title the title of the feed
     * @param link the link of the feed
     * @return the created feed metadata
     */
    PlanetFeedMetadata createFeedMetadata(String title, String link);

    /**
     * Creates a new planet metadata object.
     * 
     * @param title the title of the planet
     * @param link the link of the planet
     * @return the created planet metadata
     */
    PlanetMetadata createPlanetMetadata(String title, String link);
}