package org.apache.roller.planet.business.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.roller.planet.PlanetConstants;
import org.apache.roller.planet.PlanetException;
import org.apache.roller.planet.business.PlanetManager;
import org.apache.roller.planet.model.Planet;
import org.apache.roller.planet.model.PlanetEntry;
import org.apache.roller.planet.model.PlanetGroup;
import org.apache.roller.planet.model.Subscription;
import org.apache.roller.util.UUIDUtils;

public class JPAPlanetManagerImpl extends AbstractManagerImpl implements PlanetManager {

    public static final int MAX_ENTRIES_PER_PAGE = 10;

    private List<PlanetGroup> planetGroups;
    private List<Subscription> subscriptions;
    private List<Planet> planets;

    public JPAPlanetManagerImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<PlanetGroup> getAllPlanetGroups() {
        return entityManager.createQuery("SELECT pg FROM PlanetGroup pg", PlanetGroup.class).getResultList();
    }

    @Override
    public PlanetGroup getPlanetGroup(String id) {
        return entityManager.find(PlanetGroup.class, id);
    }

    @Override
    public void savePlanetGroup(PlanetGroup group) {
        if (group.getId() == null) {
            group.setId(UUIDUtils.generateUUID());
        }
        entityManager.persist(group);
    }

    @Override
    public void removePlanetGroup(PlanetGroup group) {
        entityManager.remove(group);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return entityManager.createQuery("SELECT s FROM Subscription s", Subscription.class).getResultList();
    }

    @Override
    public Subscription getSubscription(String id) {
        return entityManager.find(Subscription.class, id);
    }

    @Override
    public void saveSubscription(Subscription subscription) {
        if (subscription.getId() == null) {
            subscription.setId(UUIDUtils.generateUUID());
        }
        entityManager.persist(subscription);
    }

    @Override
    public void removeSubscription(Subscription subscription) {
        entityManager.remove(subscription);
    }

    @Override
    public List<Planet> getAllPlanets() {
        return entityManager.createQuery("SELECT p FROM Planet p", Planet.class).getResultList();
    }

    @Override
    public Planet getPlanet(String id) {
        return entityManager.find(Planet.class, id);
    }

    @Override
    public void savePlanet(Planet planet) {
        if (planet.getId() == null) {
            planet.setId(UUIDUtils.generateUUID());
        }
        entityManager.persist(planet);
    }

    @Override
    public void removePlanet(Planet planet) {
        entityManager.remove(planet);
    }

    @Override
    public List<PlanetEntry> getEntries(PlanetGroup group, Date startDate, Date endDate, int offset, int length) {
        List<PlanetEntry> entries = new ArrayList<>();
        QueryBuilder queryBuilder = new QueryBuilder(entityManager);

        queryBuilder.select("pe")
                .from("PlanetEntry pe")
                .where("pe.planetGroup = :group")
                .setParameter("group", group);

        if (startDate != null) {
            queryBuilder.where("pe.entryDate >= :startDate")
                    .setParameter("startDate", startDate);
        }

        if (endDate != null) {
            queryBuilder.where("pe.entryDate <= :endDate")
                    .setParameter("endDate", endDate);
        }

        queryBuilder.orderBy("pe.entryDate DESC")
                .setFirstResult(offset)
                .setMaxResults(length);

        entries = queryBuilder.getResultList();

        return entries;
    }

    private class QueryBuilder {
        private EntityManager entityManager;
        private StringBuilder query;
        private List<PlanetEntry> result;

        public QueryBuilder(EntityManager entityManager) {
            this.entityManager = entityManager;
            this.query = new StringBuilder();
        }

        public QueryBuilder select(String select) {
            query.append("SELECT ").append(select);
            return this;
        }

        public QueryBuilder from(String from) {
            query.append(" FROM ").append(from);
            return this;
        }

        public QueryBuilder where(String condition) {
            query.append(" WHERE ").append(condition);
            return this;
        }

        public QueryBuilder setParameter(String name, Object value) {
            entityManager.setParameter(name, value);
            return this;
        }

        public QueryBuilder orderBy(String orderBy) {
            query.append(" ORDER BY ").append(orderBy);
            return this;
        }

        public QueryBuilder setFirstResult(int firstResult) {
            entityManager.setFirstResult(firstResult);
            return this;
        }

        public QueryBuilder setMaxResults(int maxResults) {
            entityManager.setMaxResults(maxResults);
            return this;
        }

        public List<PlanetEntry> getResultList() {
            javax.persistence.Query jpaQuery = entityManager.createQuery(query.toString());
            return jpaQuery.getResultList();
        }
    }
}