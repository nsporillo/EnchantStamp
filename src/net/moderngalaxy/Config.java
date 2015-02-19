package net.moderngalaxy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bukkit.ChatColor.*;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Config {

	private SimpleDateFormat sdf;
	private List<String> format;
	protected String[] bindings = new String[8];
	private String gmperm;

	public Config(EnchantStamp main) {
		main.saveDefaultConfig();
		this.sdf = new SimpleDateFormat(main.getConfig().getString("SimpleDateFormat"));
		this.gmperm = main.getConfig().getString("Creative_Permission");
		this.format = main.getConfig().getStringList("Format");
		run(main.getConfig().getString("ActionBindings.Enchanted"), 0);
		run(main.getConfig().getString("ActionBindings.Anvil"), 2);
		run(main.getConfig().getString("ActionBindings.Villager"), 4);
		run(main.getConfig().getString("ActionBindings.Suffixes"), 6);
	}

	private void run(String str, int x) {
		String[] bind = str.split("::");
		for (int i = 0; i < 2; i++)
			bindings[(x + i)] = bind[i];
	}

	public SimpleDateFormat getDate() {
		return sdf;
	}

	public String getPerm() {
		return gmperm;
	}
	
	public boolean isStampLore(String str) {
		String lore = stripColor(str);
		for(int i = 0; i < bindings.length; i++) {
			if(lore.startsWith(stripColor(bindings[i]))) {
				return true;
			}
		}
		return false;
	}

	public List<String> processFormat(Player p, String pre, String suff) {
		List<String> lore = new ArrayList<String>();
		if (format.size() > 0) {
			String form = format.get(0).replaceAll("%action", pre)
					.replaceAll("%player", p.getName());
			if (p.getGameMode() == GameMode.CREATIVE) {
				form += bindings[6];
				lore.add(translateAlternateColorCodes('&', form));
			} else if (p.hasPermission(this.gmperm)) {
				form += bindings[7];
				lore.add(translateAlternateColorCodes('&', form));
			} else {
				lore.add(translateAlternateColorCodes('&', form));
			}
			if (format.size() > 1) {
				String form2 = format.get(1).replaceAll("%action", pre)
						.replaceAll("%date", this.sdf.format(new Date()));
				lore.add(translateAlternateColorCodes('&', form2));
			}
		} else
			lore.add("Config format is null");
		return lore;
	}
}
