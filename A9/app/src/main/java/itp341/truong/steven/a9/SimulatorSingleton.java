package itp341.truong.steven.a9;

import java.util.ArrayList;

/**
 * Created by Steven on 4/12/2016.
 */
public class SimulatorSingleton {

    private SimulatorSingleton instance;

    private SimulatorSingleton() {

    }

    public SimulatorSingleton getInstance() {
        if (instance == null) {
            instance = new SimulatorSingleton();
        }
        return instance;
    }

    public ArrayList<String> getUniquePowers() {
        return null;
    }

    public void addHero(Hero newHero) {

    }

    public ArrayList<Hero> getRankings() {
        return null;
    }

    public ArrayList<Hero> getHeroes() {
        return null;
    }

    public int getPowerResult(String power1, String power2) {
        return 0;
    }

    public void addBattleResult(Hero hero, int result) {

    }

}
