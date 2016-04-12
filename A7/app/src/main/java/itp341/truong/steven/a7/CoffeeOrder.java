package itp341.truong.steven.a7;

import java.io.Serializable;

/**
 * Created by steventruong on 3/18/16.
 */
public class CoffeeOrder implements Serializable {

    String instructions;

    int size;
    String brew;

    boolean sugar, cream;


    public CoffeeOrder() {
        instructions = "";
        size = 0;
        brew = "Kona";

        sugar = false;
        cream = false;
    }
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getBrew() {
        return brew;
    }

    public void setBrew(String brew) {
        this.brew = brew;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isSugar() {
        return sugar;
    }

    public void setSugar(boolean sugar) {
        this.sugar = sugar;
    }

    public boolean isCream() {
        return cream;
    }

    public void setCream(boolean cream) {
        this.cream = cream;
    }

    public String toString() {
        return "";
    }
}
