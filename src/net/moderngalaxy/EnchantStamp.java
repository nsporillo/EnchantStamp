package net.moderngalaxy;

import java.util.List;

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

	private Config conf;

	@Override
	public void onEnable() {
		conf = new Config(this);
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void onEnchant(EnchantItemEvent e) {
		genLore(e.getEnchanter(), e.getItem(), conf.bindings[0], conf.bindings[1]);
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
		Player p = (Player) e.getWhoClicked();
		if (i.getType() == InventoryType.ANVIL) {
			this.genLore(p, item, conf.bindings[2], conf.bindings[3]);
		} else if (i.getType() == InventoryType.MERCHANT) {
			if (item.getEnchantments().size() > 0) {
				this.genLore(p, item, conf.bindings[4], conf.bindings[5]);
			}
		}
	}

	private void genLore(Player p, ItemStack item, String pre, String suff) {
		List<String> s = conf.processFormat(p, pre, suff);
		ItemMeta im = item.getItemMeta();
		if(im.hasLore()) {
			List<String> lore = im.getLore();
			for(int i = 0; i < lore.size(); i++) {
				if(!conf.isStampLore(lore.get(i))){
					s.add(lore.get(i));
				}
			}		
		}
		im.setLore(s);
		item.setItemMeta(im);
	}
}