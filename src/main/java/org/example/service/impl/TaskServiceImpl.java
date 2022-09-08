package org.example.service.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Clan;
import org.example.service.ClanService;
import org.example.service.TaskService;
@Data
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final ClanService clanService;
    @Override
    public void completeTask(long idClan) {
        synchronized (clanService){
            Clan clan = clanService.getClanById(idClan);
            if(clan!=null){
                log.info("try add gold for clan id = "+ idClan+ ", current balance = " +clan.getGold() );
                clan.setGold(clan.getGold()+100);
            }
            Clan newClan = clanService.update(clan);
            log.info("added gold for clan id = "+ idClan+ ", current balance = " +newClan.getGold() );
        }
    }
}
