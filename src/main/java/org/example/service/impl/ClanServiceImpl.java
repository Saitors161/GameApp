package org.example.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Clan;
import org.example.repository.ClanRepository;
import org.example.repository.impl.InMemoryClanRepositoryImpl;
import org.example.service.ClanService;

import java.util.Collection;

@Slf4j
@Data
public class ClanServiceImpl implements ClanService {
    private final ClanRepository clanRepository ;

    @Override
    public Clan save(Clan clan) {
        clanRepository.save(clan);
        log.info("create clan in service" + clan);
        return clan;
    }

    @Override
    public void deleteClanById(long id) {
        clanRepository.deleteById(id);
        log.info("delete clan with id "+ id+" in service");
    }

    @Override
    public Clan getClanById(long id) {
        Clan clan = clanRepository.getById(id);
        if(clan!=null){
            log.info("get clan by id "+ id+" in service");
        }else {
            log.info("clan not found by id "+ id+" in service");
        }
        return clan;
    }

    @Override
    public Clan update(Clan clan) {
        return clanRepository.update(clan);
    }

    @Override
    public Collection<Clan> getAll() {
        return clanRepository.getAll();
    }
}
