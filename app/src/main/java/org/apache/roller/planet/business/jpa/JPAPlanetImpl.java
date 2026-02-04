package org.apache.roller.planet.business.jpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.apache.roller.planet.PlanetException;
import org.apache.roller.planet.PlanetManager;
import org.apache.roller.planet.business.PlanetImpl;
import org.apache.roller.planet.model.Planet;
import org.apache.roller.planet.model.Subscription;
import org.apache.roller.planet.model.User;
import org.apache.roller.planet.util.PlanetUtils;
import org.apache.roller.planet.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPAPlanetImpl implements PlanetManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPAPlanetImpl.class);

    private final EntityManager entityManager;

    public JPAPlanetImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Planet> getPlanets() {
        TypedQuery<Planet> query = entityManager.createQuery("SELECT p FROM Planet p", Planet.class);
        return query.getResultList();
    }

    @Override
    public Planet getPlanet(String id) {
        return entityManager.find(Planet.class, id);
    }

    @Override
    public void savePlanet(Planet planet) {
        try {
            entityManager.persist(planet);
            entityManager.flush();
        } catch (PersistenceException e) {
            throw new PlanetException("Error saving planet", e);
        }
    }

    @Override
    public void removePlanet(String id) {
        Planet planet = getPlanet(id);
        if (planet != null) {
            entityManager.remove(planet);
            entityManager.flush();
        }
    }

    @Override
    public List<Subscription> getSubscriptions(String planetId) {
        Planet planet = getPlanet(planetId);
        if (planet != null) {
            return planet.getSubscriptions();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Subscription> getSubscriptions(String planetId, int offset, int length) {
        Planet planet = getPlanet(planetId);
        if (planet != null) {
            List<Subscription> subscriptions = planet.getSubscriptions();
            return getSubscriptions(subscriptions, offset, length);
        }
        return Collections.emptyList();
    }

    private List<Subscription> getSubscriptions(List<Subscription> subscriptions, int offset, int length) {
        if (subscriptions == null || offset < 0 || length <= 0) {
            return Collections.emptyList();
        }
        int toIndex = offset + length;
        if (toIndex > subscriptions.size()) {
            toIndex = subscriptions.size();
        }
        return subscriptions.subList(offset, toIndex);
    }

    @Override
    public Subscription getSubscription(String id) {
        TypedQuery<Subscription> query = entityManager.createQuery("SELECT s FROM Subscription s WHERE s.id = :id", Subscription.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void saveSubscription(Subscription subscription) {
        try {
            entityManager.persist(subscription);
            entityManager.flush();
        } catch (PersistenceException e) {
            throw new PlanetException("Error saving subscription", e);
        }
    }

    @Override
    public void removeSubscription(String id) {
        Subscription subscription = getSubscription(id);
        if (subscription != null) {
            entityManager.remove(subscription);
            entityManager.flush();
        }
    }

    @Override
    public List<User> getUsers() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Override
    public User getUser(String id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void saveUser(User user) {
        try {
            entityManager.persist(user);
            entityManager.flush();
        } catch (PersistenceException e) {
            throw new PlanetException("Error saving user", e);
        }
    }

    @Override
    public void removeUser(String id) {
        User user = getUser(id);
        if (user != null) {
            entityManager.remove(user);
            entityManager.flush();
        }
    }

    @Override
    public void updatePlanetPlanetSettings(String planetId, String settingName, String settingValue) {
        Planet planet = getPlanet(planetId);
        if (planet != null) {
            PlanetUtils.updatePlanetSetting(planet, settingName, settingValue);
            savePlanet(planet);
        }
    }

    @Override
    public void updateSubscriptionSubscriptionSettings(String subscriptionId, String settingName, String settingValue) {
        Subscription subscription = getSubscription(subscriptionId);
        if (subscription != null) {
            PlanetUtils.updateSubscriptionSetting(subscription, settingName, settingValue);
            saveSubscription(subscription);
        }
    }

    @Override
    public void updateUserUserSettings(String userId, String settingName, String settingValue) {
        User user = getUser(userId);
        if (user != null) {
            UserUtils.updateUserSetting(user, settingName, settingValue);
            saveUser(user);
        }
    }

    @Override
    public void updatePlanetConfiguration(String planetId, String propertyName, String propertyValue) {
        Planet planet = getPlanet(planetId);
        if (planet != null) {
            PlanetUtils.updatePlanetConfiguration(planet, propertyName, propertyValue);
            savePlanet(planet);
        }
    }

    public List<Subscription> sortByCreationDate(List<Subscription> subscriptions) {
        if (subscriptions == null) {
            return Collections.emptyList();
        }
        Collections.sort(subscriptions, Comparator.comparing(Subscription::getCreated));
        return subscriptions;
    }

    public List<Subscription> sortByUpdatedDate(List<Subscription> subscriptions) {
        if (subscriptions == null) {
            return Collections.emptyList();
        }
        Collections.sort(subscriptions, Comparator.comparing(Subscription::getUpdated));
        return subscriptions;
    }
}