package net.tutla.manhuntPlus.twist;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TwistContext {
    public Player cause;
    public Player target;

    public Block causingBlock;
    public Entity causingEntity;

    private boolean responding;
    public void startResponding(){
        responding = true;
    }
    public boolean isResponding(){
        return responding;
    }
}
