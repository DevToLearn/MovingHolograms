package net.movingholograms.devtolearn.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.movingholograms.devtolearn.methods.Hologram;
import net.movingholograms.devtolearn.methods.Hologram_CMD;

public class MovingHolograms extends JavaPlugin {

	public static String prefix = "§3§lMovingHolograms §8§l> §r";
	public static String noperm = prefix + "§cDu hast dazu kein Recht!";
	
	private static MovingHolograms main;
	
	public static ArrayList<String> hololines = new ArrayList<>();
	
	public static File file = new File("plugins/MovingHolograms", "config.yml");
	public static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public void onEnable() {
		main = this;
		
		configuration(true);
		new Hologram();
		getCommand("movingholograms").setExecutor(new Hologram_CMD());
		
		System.out.println("[MovingHolograms] Plugin enabled!");
		System.out.println("[MovingHolograms] Author: DevToLearn");
		System.out.println("[MovingHolograms] Created: 2016.08.08");
	}
	public void onDisable() {
		configuration(false);
		System.out.println("[MovingHolograms] Plugin disabled!");
		System.out.println("[MovingHolograms] Author: DevToLearn");
		System.out.println("[MovingHolograms] Created: 2016.08.08");
	}
	public static MovingHolograms getInstance() {return main;}

	private static void configuration(boolean load_true_save_false) {
		if(load_true_save_false) {
			if(!file.exists()) {
				cfg.set("MovingHolograms.WasDevelopedBy", "DevToLearn");
				cfg.set("MovingHolograms.ShowDeveloper", true);
				try {
					cfg.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				if(cfg.getStringList("MovingHolograms.Lines") != null) {
					List<String> lines = cfg.getStringList("MovingHolograms.Lines");
					for(String line : lines) {hololines.add(line);}
				}
				cfg.set("MovingHolograms.WasDevelopedBy", "DevToLearn");
				try {
					cfg.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			if(!hololines.isEmpty() || hololines.size() > 0) {
				List<String> lines = new ArrayList<>();
				for(String line : hololines) {lines.add(line);}
				cfg.set("MovingHolograms.Lines", null);
				cfg.set("MovingHolograms.Lines", lines);
				cfg.set("MovingHolograms.WasDevelopedBy", "DevToLearn");
				try {
					cfg.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}