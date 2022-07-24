package org.example;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface NotesDAO {
    @SqlUpdate("INSERT OR REPLACE INTO notes VALUES (?, ?)")
    void insert(String key, String value);

    @SqlUpdate("DELETE FROM notes WHERE key=?")
    void delete(String key);

    @SqlQuery("SELECT value FROM notes WHERE key=?")
    Optional<String> find(String key);
}
