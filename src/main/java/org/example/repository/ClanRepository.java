package org.example.repository;

import org.example.model.Clan;

import java.util.Collection;

public interface ClanRepository {
    Clan save(Clan clan);
    void deleteById(long id);
    Clan getById(long id);
    Clan update(Clan clan);
    Collection<Clan> getAll();
}
