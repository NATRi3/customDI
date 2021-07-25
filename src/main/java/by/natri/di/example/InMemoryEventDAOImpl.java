package by.natri.di.example;

import by.natri.di.annotation.Inject;

public class InMemoryEventDAOImpl implements EventDAO{

    private DBParam db;

    @Inject
    public InMemoryEventDAOImpl(DBParam dbParam) {
        this.db = dbParam;
    }

    @Override
    public DBParam getDb() {
        return db;
    }
}
