package net.milkycraft.enchantstamp;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantStamp extends JavaPlugin implements Listener {

	private String gmperm;
	private SimpleDateFormat sdf;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		this.sdf = new SimpleDateFormat(this.getConfig().getString("SimpleDateFormat"));
		this.gmperm = this.getConfig().getString("Creative_Permission");
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void onEnchant(EnchantItemEvent e) {
		Player p = e.getEnchanter();
		ItemStack is = e.getItem();
		this.generateLore(p, is, "Enchanted", "Enchanted");
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void onAnvil(InventoryClickEvent e) {		
		if (e.getSlotType() != SlotType.RESULT || !(e.getWhoClicked() instanceof Player)) {
			return;
		}		
		ItemStack item = e.getCurrentItem();
		if (item.getType() == Material.AIR) {
			return;
		}
		Inventory i = e.getInventory();
		if (i.getType() == InventoryType.ANVIL) {
			Player p = (Player) e.getWhoClicked();
			this.generateLore(p, item, "Modified", "Modified");
		} else if (i.getType() == InventoryType.MERCHANT) {
			if (item.getEnchantments().size() > 0) {
				Player p = (Player) e.getWhoClicked();
				this.generateLore(p, item, "Traded with Villager", "Obtained");
			}
		}
	}

	private void generateLore(Player p, ItemStack item, String pre, String suff) {
		ItemMeta im = item.getItemMeta();
		List<String> s = new ArrayList<String>();
		if (p.getGameMode() == GameMode.CREATIVE) {
			s.add(RED + pre + " by " + GOLD + p.getName() + RED + " in creative");
		} else if (p.hasPermission(this.gmperm)) {
			s.add(RED + pre + " by " + GOLD + p.getName() + RED + " (Potentially in creative)");
		} else {
			s.add(GREEN + pre + " by " + p.getName());
		}
		s.add(GOLD + suff + " on " + sdf.format(new Date()));
		im.setLore(s);
		item.setItemMeta(im);
	}
}
