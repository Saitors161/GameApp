package org.example.service;

import org.example.model.Clan;

import java.util.Collection;

public interface ClanService {
    Clan save(Clan clan);
    void deleteClanById(long id);
    Clan getClanById(long id);
    Clan update(Clan clan);
    Collection<Clan> getAll();
}
