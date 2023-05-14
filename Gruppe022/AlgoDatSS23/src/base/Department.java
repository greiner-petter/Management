package de.ostfalia.aud.s23ss.base;

public enum Department {
    /**
     *
     */
    SERVICE("Service"),
    /**
     *
     */
    DEVELOPMENT("Development"),
    /**
     *
     */
    SALES("Sales"),
    /**
     *
     */
    PRODUCTION("Production"),
    /**
     *
     */
    MANPOWER("Manpower"),
    /**
     *
     */
    RESEARCH("Research"),
    /**
     *
     */
    MARKETING("Marketing"),
    /**
     *
     */
    MANAGEMENT("Management"),
    /**
     *
     */
    FINANCE("Finance");

    private final String name;

    Department(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Department create(String department) {
        return Department.valueOf(department.toUpperCase());
    }
}
