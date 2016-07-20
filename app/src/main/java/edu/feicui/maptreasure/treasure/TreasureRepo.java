package edu.feicui.maptreasure.treasure;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class TreasureRepo {
    private TreasureRepo(){
    }
    private static TreasureRepo treasureRepo;
    public static TreasureRepo getInstance(){
        if(treasureRepo==null){
            treasureRepo=new TreasureRepo();
        }
        return treasureRepo;
    }

    private final HashSet<Area> cacheAreas=new HashSet<>();
    private final HashMap<Integer,Treasure> treasureMap=new HashMap<>();

    public void cache(Area area){
        cacheAreas.add(area);
    }
    public boolean isCache(Area area){
        return cacheAreas.contains(area);
    }
    public void addTreasure(List<Treasure> treasureList){
        for (Treasure treasure:treasureList) {
            treasureMap.put(treasure.getId(),treasure);
        }
    }

    public Treasure getTreasure(int id){
        return treasureMap.get(id);
    }

    public Collection<Treasure> getTreasure(){
        return treasureMap.values();
    }

    public void clear(){
        cacheAreas.clear();
        treasureMap.clear();
    }

}
