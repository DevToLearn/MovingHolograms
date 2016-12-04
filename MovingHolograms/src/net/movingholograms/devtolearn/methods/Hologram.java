package net.movingholograms.devtolearn.methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.minecraft.server.v1_10_R1.EntityArmorStand;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_10_R1.PacketPlayOutSpawnEntityLiving;
import net.movingholograms.devtolearn.main.MovingHolograms;

public class Hologram implements Listener {
	
	public Hologram() {MovingHolograms.getInstance().getServer().getPluginManager().registerEvents(this, MovingHolograms.getInstance());}
	
	private static HashMap<String, ArrayList<EntityArmorStand>> holostats = new HashMap<>();
	private static ArrayList<String> holoOfPlayer = new ArrayList<>();	
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if(!p.isSneaking()) {
			if(!holoOfPlayer.contains(p.getName())) {
				Location eyeLoc = p.getLocation().add(p.getEyeLocation().getDirection().multiply(3));
				eyeLoc.add(0, +1, 0);
				Location displayLoc = eyeLoc.clone().subtract(0.0D, 0.5, 0.0D);
				
				List<String> hololines = new ArrayList<String>();
				if(MovingHolograms.hololines.size() == 0) {
					hololines.add("§c§oKeine Linie gesetzt! [/movingholograms]");
				} else {
					for(int i = 0; i < MovingHolograms.hololines.size(); i++) {
						hololines.add(ChatColor.translateAlternateColorCodes('&', MovingHolograms.hololines.get(i)));
					}
				}			
				ArrayList<EntityArmorStand> armorStandList = new ArrayList<>();
				for(int i = 0; i < hololines.size(); i++) {
					String line = hololines.get(i);			
					EntityArmorStand as = new EntityArmorStand(((CraftWorld) displayLoc.getWorld()).getHandle());
					as.setCustomName(line);
					as.setCustomNameVisible(true);
					as.setNoGravity(true);
					as.setInvisible(true);
					as.setLocation(displayLoc.getX(), displayLoc.getY(), displayLoc.getZ(), 0.0F, 0.0F);			
					PacketPlayOutSpawnEntityLiving spawnpacket = new PacketPlayOutSpawnEntityLiving(as);	
					((CraftPlayer)p).getHandle().playerConnection.sendPacket(spawnpacket);			
					displayLoc.add(0.0D, - 0.25D, 0.0D);			
					armorStandList.add(as);
				}
				holostats.put(p.getName(), armorStandList);
				holoOfPlayer.add(p.getName());
			}
		} else {
			if(holoOfPlayer.contains(p.getName())) {
				holoOfPlayer.remove(p.getName());
				ArrayList<EntityArmorStand> armorStandList = holostats.get(p.getName());
				
				for(EntityArmorStand armorStand : armorStandList) {
					PacketPlayOutEntityDestroy destroypacket = new PacketPlayOutEntityDestroy(armorStand.getId());
					((CraftPlayer) p).getHandle().playerConnection.sendPacket(destroypacket);
				}
			}
		}
	}
	@EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(p.isSneaking()) {
        	if(holoOfPlayer.contains(p.getName())) {
        		Location loc = p.getEyeLocation();
	        	loc.add(0, -1, 0);
	            loc.add(loc.getDirection().multiply(3));     
	            ArrayList<EntityArmorStand> armorStandList = holostats.get(p.getName());
	            for(EntityArmorStand armorStand : armorStandList) {
	                 armorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
	                 loc.add(0, - 0.25D, 0);	                     
	                 PacketPlayOutEntityTeleport ppet = new PacketPlayOutEntityTeleport(armorStand);    
	                 ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppet);
	            }	       
			} 
		} 
    }
}