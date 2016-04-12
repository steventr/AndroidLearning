package itp341.truong.steven.a9;

/**
 * Created by Steven on 4/12/2016.
 */
public class Hero {
    long _id;
    String name;
    String power1;
    String power2;
    int health;
    int numWins;
    int numLosses;
    int numTies;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPower1() {
        return power1;
    }

    public void setPower1(String power1) {
        this.power1 = power1;
    }

    public String getPower2() {
        return power2;
    }

    public void setPower2(String power2) {
        this.power2 = power2;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getNumWins() {
        return numWins;
    }

    public void setNumWins(int numWins) {
        this.numWins = numWins;
    }

    public int getNumLosses() {
        return numLosses;
    }

    public void setNumLosses(int numLosses) {
        this.numLosses = numLosses;
    }

    public int getNumTies() {
        return numTies;
    }

    public void setNumTies(int numTies) {
        this.numTies = numTies;
    }

    public String toString(){
        return "";
    }

    public boolean isAlive() {
        if (health > 0) {
            return true;
        }
        return false;
    }
}
