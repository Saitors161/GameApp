package org.example.service.impl;

import lombok.Data;
import org.example.model.Clan;
import org.example.service.ClanService;
import org.example.service.UserService;
@Data
public class UserServiceImpl implements UserService {
    private final ClanService clanService;
    @Override
    public void addGoldForClan(long idClan, int gold) {
        synchronized (clanService){
            Clan clan = clanService.getClanById(idClan);
            if(clan!=null){
                clan.setGold(clan.getGold()+gold);
            }
            clanService.update(clan);
        }
    }
}
