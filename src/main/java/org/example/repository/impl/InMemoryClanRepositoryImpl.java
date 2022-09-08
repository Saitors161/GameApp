package org.example.repository.impl;

import org.example.model.Clan;
import org.example.repository.ClanRepository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryClanRepositoryImpl implements ClanRepository {
    private final ConcurrentHashMap<Long,Clan> clanHashMap = new ConcurrentHashMap<>();
    @Override
    public Clan save(Clan clan) {
        clanHashMap.put(clan.getId(),clan);
        return clan;
    }

    @Override
    public void deleteById(long id) {
        clanHashMap.remove(id);
    }

    @Override
    public Clan getById(long id) {
        return clanHashMap.get(id);
    }

    @Override
    public Clan update(Clan clan) {
        return clanHashMap.put(clan.getId(),clan);
    }

    @Override
    public Collection<Clan> getAll() {
        return clanHashMap.values();
    }

}
