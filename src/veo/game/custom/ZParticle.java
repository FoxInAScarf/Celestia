package veo.game.custom;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.protocol.game.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class ZParticle {

    private ParticleType particle;
    private Location location;
    private int count;
    private double[] offset;

    public ZParticle(ParticleType particle, Location location, int count, double[] offset) {

        this.particle = particle;
        this.location = location;
        this.count = count;
        this.offset = offset;

    }

    public void playParticle(boolean parameter, Player player) { // #playParticle(shouldTheParticleBeDisplayedToAllPlayers?, ifNoThenToWhichSpecificPlayer?)

        if (parameter) {

            for (Player current : Bukkit.getOnlinePlayers()) {

                ((CraftPlayer) current).getHandle().b.a(new PacketPlayOutWorldParticles(particle,
                        true,
                        (float) location.getX(),
                        (float) location.getY(),
                        (float) location.getZ(),
                        Float.valueOf(String.valueOf(offset[0])), //what is this you might ask? eclipse being dumb
                        Float.valueOf(String.valueOf(offset[1])),
                        Float.valueOf(String.valueOf(offset[2])), 0,
                        count));

            }

        } else {

            ((CraftPlayer) player).getHandle().b.a(new PacketPlayOutWorldParticles(particle,
                    true,
                    (float) location.getX(),
                    (float) location.getY(),
                    (float) location.getZ(),
                    Float.valueOf(String.valueOf(offset[0])), //what is this you might ask? eclipse being dumb
                    Float.valueOf(String.valueOf(offset[1])),
                    Float.valueOf(String.valueOf(offset[2])), 0,
                    count));

        }

    }

    public ParticleType getParticle() { return particle; }
    public Location getLocation() { return location; }
    public int getCount() { return count; }
    public double[] getOffset() { return offset; }

    public void setParticle(ParticleType particle) { this.particle = particle; }
    public void setLocation(Location location) { this.location = location; }
    public void setCount(int count) { this.count = count; }
    public void setOffset(double[] offset) { this.offset = offset; }

}