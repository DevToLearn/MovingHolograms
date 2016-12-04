package net.movingholograms.devtolearn.methods;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.movingholograms.devtolearn.main.MovingHolograms;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;

public class Hologram_CMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		
		if(sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Du musst ein Spieler sein!");
			return true;
		} else {
			if(cmd.getName().equalsIgnoreCase("movingholograms")) {
				if(p.hasPermission("movingholograms.edit")) {
					if(args.length == 0) {
						sendHelpList(p);
					} else if(args.length > 0) {
						if(args[0].equalsIgnoreCase("add")) {
							String message = args[1];
							for(int i = 2; i < args.length; i++) {message = message + " " + args[i];}
							if(message.length() > 30) {
								p.sendMessage(MovingHolograms.prefix + "§cEine Nachricht kann höchstens 30 Zeichen lang sein!");
							} else {
								if(MovingHolograms.hololines.size() >= 5) {
									p.sendMessage(MovingHolograms.prefix + "§7Maximal 5 Linien passen in das Hologramm!");
								} else {
									MovingHolograms.hololines.add(message);
									p.sendMessage(MovingHolograms.prefix + "§7Du hast auf Linie §6§l" + MovingHolograms.hololines.size() + " §7die Nachricht §6" + ChatColor.translateAlternateColorCodes('&', message) + " §7gelegt!");
								}
							}
						} else if(args[0].equalsIgnoreCase("remove")) {
							String message = args[1];
							for(int i = 2; i < args.length; i++) {message = message + " " + args[i];}
							if(MovingHolograms.hololines.contains(message)) {
								MovingHolograms.hololines.remove(message);
								p.sendMessage(MovingHolograms.prefix + "§7Du hast erfolgreich §6§l" + ChatColor.translateAlternateColorCodes('&', message) + " §7entfernt!");
							} else {
								p.sendMessage(MovingHolograms.prefix + "§7Diese Nachricht existiert nicht!");
							}
						} else if(args[0].equalsIgnoreCase("list")) {
							if(MovingHolograms.hololines.size() == 0) {
								p.sendMessage(MovingHolograms.prefix + "§cKeine Linien eingetragen! [/movingholograms]");
							} else {
								p.sendMessage(MovingHolograms.prefix + "§e§lHologramm Linien:");
								for(int i = 0; i < MovingHolograms.hololines.size(); i++) {
									IChatBaseComponent line = ChatSerializer.a("{\"text\":\"§b#" + (i + 1) + " \",\"extra\":[{\"text\":\" §7" + MovingHolograms.hololines.get(i) 
									+ "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§c§oKlicke um die Linie zu löschen!\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/movingholograms remove "
									+ MovingHolograms.hololines.get(i) + "\"}}]}");
									PacketPlayOutChat packet = new PacketPlayOutChat(line);
									((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
								}
							}
						} else if(args[0].equalsIgnoreCase("clear")) {
							if(MovingHolograms.hololines.size() == 0) {
								p.sendMessage(MovingHolograms.prefix + "§cKeine Linien eingetragen! [/movingholograms]");
							} else {
								p.sendMessage(MovingHolograms.prefix + "§7Es wurden erfolgreich §6§l" + MovingHolograms.hololines.size() + " §7Linien gelöscht!");
								MovingHolograms.hololines.clear();
							}
						} else {
							sendHelpList(p);
						}
					}
				} else {
					p.sendMessage(MovingHolograms.noperm);
				}
			}
		}
		return false;
	}
	private static void sendHelpList(Player p) {
		p.sendMessage(MovingHolograms.prefix + "§e§lMovingHolograms Management:");
		p.sendMessage("§4§lAchtung! §cDu kannst Farbcodes verwenden (&)!");
		p.sendMessage("§4§lAchtung! §cDu kannst eine Nachricht löschen, indem du");
		p.sendMessage("§cim Chat (/movingholograms list) auf sie klickst!");
		p.sendMessage("§7- §8§l/§bmovingholograms add <Nachricht> §8§l> §7Fügt eine Linie hinzu!");
		p.sendMessage("§7- §8§l/§bmovingholograms list §8§l> §7Listet alle Linien auf!");
		p.sendMessage("§7- §8§l/§bmovingholograms clear §8§l> §7Löscht alle Linien!");
		p.sendMessage("§e§lPlugin by DevToLearn");
	}
}