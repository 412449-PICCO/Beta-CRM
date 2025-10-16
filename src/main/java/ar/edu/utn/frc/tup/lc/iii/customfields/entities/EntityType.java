package ar.edu.utn.frc.tup.lc.iii.customfields.entities;

public enum EntityType {
    CONTACT("contacts"),
    LEAD("leads"),
    ACCOUNT("accounts"),
    OPPORTUNITY("opportunities"),
    INTERACTION("interactions");

    private final String tableName;

    EntityType(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}

