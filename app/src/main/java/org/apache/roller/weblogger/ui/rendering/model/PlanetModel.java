package org.apache.roller.weblogger.ui.rendering.model;

import java.util.ArrayList;
import java.util.List;

public class PlanetModel {
    
    private List<PlanetaryData> planetaryDataList;
    private PlanetType planetType;
    private double orbitalRadius;
    private double orbitalPeriod;
    private double rotationPeriod;
    private double temperature;
    private double mass;
    private double density;
    private double gravity;
    private double escapeVelocity;

    public PlanetModel(PlanetType planetType, double orbitalRadius, double orbitalPeriod, double rotationPeriod, double temperature, double mass, double density, double gravity, double escapeVelocity) {
        this.planetType = planetType;
        this.orbitalRadius = orbitalRadius;
        this.orbitalPeriod = orbitalPeriod;
        this.rotationPeriod = rotationPeriod;
        this.temperature = temperature;
        this.mass = mass;
        this.density = density;
        this.gravity = gravity;
        this.escapeVelocity = escapeVelocity;
        this.planetaryDataList = new ArrayList<>();
        this.planetaryDataList.add(new PlanetaryData("Orbital Radius", orbitalRadius));
        this.planetaryDataList.add(new PlanetaryData("Orbital Period", orbitalPeriod));
        this.planetaryDataList.add(new PlanetaryData("Rotation Period", rotationPeriod));
        this.planetaryDataList.add(new PlanetaryData("Temperature", temperature));
        this.planetaryDataList.add(new PlanetaryData("Mass", mass));
        this.planetaryDataList.add(new PlanetaryData("Density", density));
        this.planetaryDataList.add(new PlanetaryData("Gravity", gravity));
        this.planetaryDataList.add(new PlanetaryData("Escape Velocity", escapeVelocity));
    }

    public List<PlanetaryData> getPlanetaryDataList() {
        return planetaryDataList;
    }

    public enum PlanetType {
        TERRESTRIAL,
        GAS_GIANT,
        ICE_GIANT
    }

    public static class PlanetaryData {
        private String attributeName;
        private double attributeValue;

        public PlanetaryData(String attributeName, double attributeValue) {
            this.attributeName = attributeName;
            this.attributeValue = attributeValue;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public double getAttributeValue() {
            return attributeValue;
        }
    }

    public double calculateHabitableZone() {
        if (planetType == PlanetType.TERRESTRIAL) {
            return 0.95 * orbitalRadius;
        } else if (planetType == PlanetType.GAS_GIANT) {
            return 1.37 * orbitalRadius;
        } else {
            return 0.73 * orbitalRadius;
        }
    }

    public double calculateSurfaceGravity() {
        return gravity * (mass / (Math.pow(orbitalRadius, 2)));
    }

    public double calculateAtmosphericPressure() {
        return 1013.25 * (density / (Math.pow(orbitalRadius, 2)));
    }

    public double calculateEscapeVelocity() {
        return escapeVelocity;
    }

    public String planetClassification() {
        if (planetType == PlanetType.TERRESTRIAL) {
            return "Terrestrial Planet";
        } else if (planetType == PlanetType.GAS_GIANT) {
            return "Gas Giant";
        } else {
            return "Ice Giant";
        }
    }
}