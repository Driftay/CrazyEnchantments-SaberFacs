package me.BadBones69.CrazyEnchantments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI implements Listener{
	static void openGUI(Player player){
		Inventory inv = Bukkit.createInventory(null, Main.settings.getConfig().getInt("Settings.GUISize"), Api.getInvName());
		for(String cat : Main.settings.getConfig().getConfigurationSection("Categories").getKeys(false)){
			inv.setItem(Main.settings.getConfig().getInt("Categories."+cat+".Slot")-1, Api.makeItem(Main.settings.getConfig().getString("Categories."+cat+".Item"), 1, 
					Main.settings.getConfig().getString("Categories."+cat+".Name"), Main.settings.getConfig().getStringList("Categories."+cat+".Lore")));
		}
		if(Main.settings.getConfig().contains("Settings.GUICustomization")){
			for(String custom : Main.settings.getConfig().getStringList("Settings.GUICustomization")){
				String name = "";
				String item = "1";
				int slot = 0;
				ArrayList<String> lore = new ArrayList<String>();
				String[] b = custom.split(", ");
				for(String i : b){
					if(i.contains("Item:")){
						i=i.replace("Item:", "");
						item=i;
					}
					if(i.contains("Name:")){
						i=i.replace("Name:", "");
						name=i;
					}
					if(i.contains("Slot:")){
						i=i.replace("Slot:", "");
						slot=Integer.parseInt(i);
					}
					if(i.contains("Lore:")){
						i=i.replace("Lore:", "");
						String[] d = i.split("_");
						for(String l : d){
							lore.add(l);
						}
					}
				}
				slot--;
				inv.setItem(slot, Api.makeItem(item, 1, name, lore));
			}
			if(Main.settings.getConfig().getBoolean("Settings.Info.InGUI")){
				String name = Main.settings.getConfig().getString("Settings.Info.Name");
				String id = Main.settings.getConfig().getString("Settings.Info.Item");
				List<String> lore = Main.settings.getConfig().getStringList("Settings.Info.Lore");
				int slot = Main.settings.getConfig().getInt("Settings.Info.Slot")-1;
				if(Main.settings.getConfig().getBoolean("Settings.Info.Glowing")){
					inv.setItem(slot, Api.addGlow(Api.makeItem(id, 1, name, lore)));
				}else{
					inv.setItem(slot, Api.makeItem(id, 1, name, lore));
				}
			}
			if(Main.settings.getConfig().getBoolean("Settings.Dust.SuccessDust.InGUI")){
				String name = Main.settings.getConfig().getString("Settings.Dust.SuccessDust.GUIName");
				String id = Main.settings.getConfig().getString("Settings.Dust.SuccessDust.Item");
				List<String> lore = Main.settings.getConfig().getStringList("Settings.Dust.SuccessDust.GUILore");
				int slot = Main.settings.getConfig().getInt("Settings.Dust.SuccessDust.Slot")-1;
				inv.setItem(slot, Api.makeItem(id, 1, name, lore));
			}
			if(Main.settings.getConfig().getBoolean("Settings.Dust.DestroyDust.InGUI")){
				String name = Main.settings.getConfig().getString("Settings.Dust.DestroyDust.GUIName");
				String id = Main.settings.getConfig().getString("Settings.Dust.DestroyDust.Item");
				List<String> lore = Main.settings.getConfig().getStringList("Settings.Dust.DestroyDust.GUILore");
				int slot = Main.settings.getConfig().getInt("Settings.Dust.DestroyDust.Slot")-1;
				inv.setItem(slot, Api.makeItem(id, 1, name, lore));
			}
			if(Main.settings.getConfig().getBoolean("Settings.BlackScroll.InGUI")){
				String name = Main.settings.getConfig().getString("Settings.BlackScroll.GUIName");
				String id = Main.settings.getConfig().getString("Settings.BlackScroll.Item");
				List<String> lore = Main.settings.getConfig().getStringList("Settings.BlackScroll.Lore");
				int slot = Main.settings.getConfig().getInt("Settings.BlackScroll.Slot")-1;
				inv.setItem(slot, Api.makeItem(id, 1, name, lore));
			}
			if(Main.settings.getConfig().getBoolean("Settings.WhiteScroll.InGUI")){
				String name = Main.settings.getConfig().getString("Settings.WhiteScroll.GUIName");
				String id = Main.settings.getConfig().getString("Settings.WhiteScroll.Item");
				List<String> lore = Main.settings.getConfig().getStringList("Settings.WhiteScroll.Lore");
				int slot = Main.settings.getConfig().getInt("Settings.WhiteScroll.Slot")-1;
				inv.setItem(slot, Api.makeItem(id, 1, name, lore));
			}
		}
		player.openInventory(inv);
	}
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		ItemStack item = e.getCurrentItem();
		Inventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		if(inv!=null){
			if(inv.getName().equals(Api.getInvName())){
				e.setCancelled(true);
				if(item==null)return;
				if(item.hasItemMeta()){
					if(item.getItemMeta().hasDisplayName()){
						String name = item.getItemMeta().getDisplayName();
						for(String cat : Main.settings.getConfig().getConfigurationSection("Categories").getKeys(false)){
							if(name.equals(Api.color(Main.settings.getConfig().getString("Categories."+cat+".Name")))){
								if(Api.isInvFull(player)){
									if(!Main.settings.getMsg().contains("Messages.Inventory-Full")){
										player.sendMessage(Api.color("&cYour inventory is to full. Please open up some space to buy that."));
									}else{
										player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Inventory-Full")));
									}
									return;
								}
								if(player.getGameMode() != GameMode.CREATIVE){
									if(Main.settings.getConfig().getString("Categories."+cat+".Lvl/Total").equalsIgnoreCase("Lvl")){
										if(Api.getXPLvl(player)<Main.settings.getConfig().getInt("Categories."+cat+".XP")){
											String xp = Main.settings.getConfig().getInt("Categories."+cat+".XP") - Api.getXPLvl(player)+"";
											player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-XP-Lvls").replace("%XP%", xp).replace("%xp%", xp)));
											return;
										}
										Api.takeLvlXP(player, Main.settings.getConfig().getInt("Categories."+cat+".XP"));
									}
									if(Main.settings.getConfig().getString("Categories."+cat+".Lvl/Total").equalsIgnoreCase("Total")){
										if(player.getTotalExperience()<Main.settings.getConfig().getInt("Categories."+cat+".XP")){
											String xp = Main.settings.getConfig().getInt("Categories."+cat+".XP") - player.getTotalExperience()+"";
											player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-Total-XP").replace("%XP%", xp).replace("%xp%", xp)));
											return;
										}
										Api.takeTotalXP(player, Main.settings.getConfig().getInt("Categories."+cat+".XP"));
									}
								}
								player.getInventory().addItem(Api.addGlow(ECControl.pick(cat)));
								return;
							}
						}
						if(name.equalsIgnoreCase(Api.color(Main.settings.getConfig().getString("Settings.Info.Name")))){
							openInfo(player);
							return;
						}
						if(name.equalsIgnoreCase(Api.color(Main.settings.getConfig().getString("Settings.Dust.DestroyDust.GUIName")))){
							if(Api.isInvFull(player)){
								if(!Main.settings.getMsg().contains("Messages.Inventory-Full")){
									player.sendMessage(Api.color("&cYour inventory is to full. Please open up some space to buy that."));
								}else{
									player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Inventory-Full")));
								}
								return;
							}
							int price = Main.settings.getConfig().getInt("Settings.SignOptions.DestroyDustStyle.Cost");
							if(Main.settings.getConfig().getString("Settings.SignOptions.DestroyDustStyle.Money/XP").equalsIgnoreCase("Money")){
								if(Api.getMoney(player)<price){
									double needed = price-Api.getMoney(player);
									player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-Money").replace("%Money_Needed%", needed+"").replace("%money_needed%", needed+"")));
									return;
								}
								Main.econ.withdrawPlayer(player, price);
							}else{
								if(Main.settings.getConfig().getString("Settings.SignOptions.DestroyDustStyle.Lvl/Total").equalsIgnoreCase("Lvl")){
									if(Api.getXPLvl(player)<price){
										String xp = price - Api.getXPLvl(player)+"";
										player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-XP-Lvls").replace("%XP%", xp).replace("%xp%", xp)));
										return;
									}
									Api.takeLvlXP(player, price);
								}
								if(Main.settings.getConfig().getString("Settings.SignOptions.DestroyDustStyle.Lvl/Total").equalsIgnoreCase("Total")){
									if(player.getTotalExperience()<price){
										String xp = price - player.getTotalExperience()+"";
										player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-Total-XP").replace("%XP%", xp).replace("%xp%", xp)));
										return;
									}
									Api.takeTotalXP(player, price);
								}
							}
							player.getInventory().addItem(DustControl.getDust("DestroyDust", 1));
							return;
						}
						if(name.equalsIgnoreCase(Api.color(Main.settings.getConfig().getString("Settings.Dust.SuccessDust.GUIName")))){
							if(Api.isInvFull(player)){
								if(!Main.settings.getMsg().contains("Messages.Inventory-Full")){
									player.sendMessage(Api.color("&cYour inventory is to full. Please open up some space to buy that."));
								}else{
									player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Inventory-Full")));
								}
								return;
							}
							int price = Main.settings.getConfig().getInt("Settings.SignOptions.SuccessDustStyle.Cost");
							if(Main.settings.getConfig().getString("Settings.SignOptions.SuccessDustStyle.Money/XP").equalsIgnoreCase("Money")){
								if(Api.getMoney(player)<price){
									double needed = price-Api.getMoney(player);
									player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-Money").replace("%Money_Needed%", needed+"").replace("%money_needed%", needed+"")));
									return;
								}
								Main.econ.withdrawPlayer(player, price);
							}else{
								if(Main.settings.getConfig().getString("Settings.SignOptions.SuccessDustStyle.Lvl/Total").equalsIgnoreCase("Lvl")){
									if(Api.getXPLvl(player)<price){
										String xp = price - Api.getXPLvl(player)+"";
										player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-XP-Lvls").replace("%XP%", xp).replace("%xp%", xp)));
										return;
									}
									Api.takeLvlXP(player, price);
								}
								if(Main.settings.getConfig().getString("Settings.SignOptions.SuccessDustStyle.Lvl/Total").equalsIgnoreCase("Total")){
									if(player.getTotalExperience()<price){
										String xp = price - player.getTotalExperience()+"";
										player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-Total-XP").replace("%XP%", xp).replace("%xp%", xp)));
										return;
									}
									Api.takeTotalXP(player, price);
								}
							}
							player.getInventory().addItem(DustControl.getDust("SuccessDust", 1));
							return;
						}
						if(name.equalsIgnoreCase(Api.color(Main.settings.getConfig().getString("Settings.BlackScroll.GUIName")))){
							if(Api.isInvFull(player)){
								if(!Main.settings.getMsg().contains("Messages.Inventory-Full")){
									player.sendMessage(Api.color("&cYour inventory is to full. Please open up some space to buy that."));
								}else{
									player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Inventory-Full")));
								}
								return;
							}
							int price = Main.settings.getConfig().getInt("Settings.SignOptions.BlackScrollStyle.Cost");
							if(Main.settings.getConfig().getString("Settings.SignOptions.BlackScrollStyle.Money/XP").equalsIgnoreCase("Money")){
								if(Api.getMoney(player)<price){
									double needed = price-Api.getMoney(player);
									player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-Money").replace("%Money_Needed%", needed+"").replace("%money_needed%", needed+"")));
									return;
								}
								Main.econ.withdrawPlayer(player, price);
							}else{
								if(Main.settings.getConfig().getString("Settings.SignOptions.BlackScrollStyle.Lvl/Total").equalsIgnoreCase("Lvl")){
									if(Api.getXPLvl(player)<price){
										String xp = price - Api.getXPLvl(player)+"";
										player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-XP-Lvls").replace("%XP%", xp).replace("%xp%", xp)));
										return;
									}
									Api.takeLvlXP(player, price);
								}
								if(Main.settings.getConfig().getString("Settings.SignOptions.BlackScrollStyle.Lvl/Total").equalsIgnoreCase("Total")){
									if(player.getTotalExperience()<price){
										String xp = price - player.getTotalExperience()+"";
										player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-Total-XP").replace("%XP%", xp).replace("%xp%", xp)));
										return;
									}
									Api.takeTotalXP(player, price);
								}
							}
							player.getInventory().addItem(Api.BlackScroll(1));
							return;
						}
						if(name.equalsIgnoreCase(Api.color(Main.settings.getConfig().getString("Settings.WhiteScroll.GUIName")))){
							if(Api.isInvFull(player)){
								if(!Main.settings.getMsg().contains("Messages.Inventory-Full")){
									player.sendMessage(Api.color("&cYour inventory is to full. Please open up some space to buy that."));
								}else{
									player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Inventory-Full")));
								}
								return;
							}
							int price = Main.settings.getConfig().getInt("Settings.SignOptions.WhiteScrollStyle.Cost");
							if(Main.settings.getConfig().getString("Settings.SignOptions.WhiteScrollStyle.Money/XP").equalsIgnoreCase("Money")){
								if(Api.getMoney(player)<price){
									double needed = price-Api.getMoney(player);
									player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-Money").replace("%Money_Needed%", needed+"").replace("%money_needed%", needed+"")));
									return;
								}
								Main.econ.withdrawPlayer(player, price);
							}else{
								if(Main.settings.getConfig().getString("Settings.SignOptions.WhiteScrollStyle.Lvl/Total").equalsIgnoreCase("Lvl")){
									if(Api.getXPLvl(player)<price){
										String xp = price - Api.getXPLvl(player)+"";
										player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-XP-Lvls").replace("%XP%", xp).replace("%xp%", xp)));
										return;
									}
									Api.takeLvlXP(player, price);
								}
								if(Main.settings.getConfig().getString("Settings.SignOptions.WhiteScrollStyle.Lvl/Total").equalsIgnoreCase("Total")){
									if(player.getTotalExperience()<price){
										String xp = price - player.getTotalExperience()+"";
										player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-Total-XP").replace("%XP%", xp).replace("%xp%", xp)));
										return;
									}
									Api.takeTotalXP(player, price);
								}
							}
							player.getInventory().addItem(Api.addWhiteScroll(1));
							return;
						}
					}
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void addEnchantment(InventoryClickEvent e){
		Inventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		if(inv != null){
			if(e.getCursor() != null&&e.getCurrentItem() != null){
				ItemStack c = e.getCursor();
				ItemStack item  = e.getCurrentItem();
				if(c.hasItemMeta()){
					if(c.getItemMeta().hasDisplayName()){
						String name = c.getItemMeta().getDisplayName();
						for(String en : ECControl.allEnchantments().keySet()){
							if(name.contains(Api.color(Api.getEnchBookColor(en)+Api.getEnchName(en)))){
								if(c.getType()!=Material.BOOK)return;
								for(Material m : ECControl.allEnchantments().get(en)){
									if(item.getType() == m){
										if(c.getAmount() == 1){
											if(item.getItemMeta().hasLore()){
												for(String l:item.getItemMeta().getLore()){
													if(l.contains(Api.getEnchName(en))){
														return;
													}
												}
											}
											if(Main.settings.getConfig().contains("Settings.EnchantmentOptions.MaxAmountOfEnchantmentsToggle")){
												if(Main.settings.getConfig().getBoolean("Settings.EnchantmentOptions.MaxAmountOfEnchantmentsToggle")){
													int limit = Main.settings.getConfig().getInt("Settings.EnchantmentOptions.MaxAmountOfEnchantments");
													int total = Api.getEnchAmount(item);
													if(total>=limit){
														player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Hit-Enchantment-Max")));
														return;
													}
												}
											}
											e.setCancelled(true);
											if(Api.successChance(c) || player.getGameMode() == GameMode.CREATIVE){
												boolean destroy = Api.destroyChance(c);
												if(!destroy||Api.isProtected(item)||player.getGameMode()==GameMode.CREATIVE){
													name = Api.removeColor(name);
													String[] breakdown = name.split(" ");
													String color = "&7";
													if(Main.settings.getEnchs().contains("Enchantments."+en)){
														color=Main.settings.getEnchs().getString("Enchantments."+en+".Color");
													}
													if(Main.settings.getCustomEnchs().contains("Enchantments."+en)){
														color=Main.settings.getCustomEnchs().getString("Enchantments."+en+".Color");
													}
													String enchantment = Api.getEnchName(en);
													String lvl = breakdown[1];
													String full = Api.color(color+enchantment+" "+lvl);
													e.setCursor(new ItemStack(Material.AIR));
													if(!destroy||player.getGameMode()==GameMode.CREATIVE){
														e.setCurrentItem(Api.addGlow(Api.addLore(item, full)));
														if(Api.getVersion()==19){
															player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 1);
														}else{
															player.playSound(player.getLocation(), Sound.valueOf("LEVEL_UP"), 1, 1);
														}
													}
													if(destroy&&Api.isProtected(item)){
														if(player.getGameMode()!=GameMode.CREATIVE){
															e.setCurrentItem(Api.removeProtected(item));
															if(Api.getVersion()==19){
																player.playSound(player.getLocation(), Sound.valueOf("ENTITY_ITEM_BREAK"), 1, 1);
															}else{
																player.playSound(player.getLocation(), Sound.valueOf("ITEM_BREAK"), 1, 1);
															}
															return;
														}
													}
													player.updateInventory();
													return;
												}else{
													e.setCursor(new ItemStack(Material.AIR));
													e.setCurrentItem(new ItemStack(Material.AIR));
													e.setCursor(new ItemStack(Material.AIR));
													if(Api.getVersion()==19){
														player.playSound(player.getLocation(), Sound.valueOf("ENTITY_ITEM_BREAK"), 1, 1);
													}else{
														player.playSound(player.getLocation(), Sound.valueOf("ITEM_BREAK"), 1, 1);
													}
													player.updateInventory();
													return;
												}
											}else{
												e.setCursor(new ItemStack(Material.AIR));
												if(Api.getVersion()==19){
													player.playSound(player.getLocation(), Sound.valueOf("ENTITY_ITEM_BREAK"), 1, 1);
												}else{
													player.playSound(player.getLocation(), Sound.valueOf("ITEM_BREAK"), 1, 1);
												}
												return;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void infoClick(InventoryClickEvent e){
		Inventory inv = e.getInventory();
		if(inv!=null){
			if(inv.getName().equals(Api.color("&c&lEnchantment Info"))){
				e.setCancelled(true);
				if(e.getCurrentItem()!=null){
					ItemStack item = e.getCurrentItem();
					if(item.hasItemMeta()){
						if(item.getItemMeta().hasDisplayName()){
							if(item.getItemMeta().getDisplayName().equals(Api.color("&7&l<<&b&lBack"))){
								openInfo((Player)e.getWhoClicked());
								return;
							}
							if(item.getItemMeta().getDisplayName().equals(Api.color("&e&lHelmet Enchantments"))){
								Inventory in = Bukkit.createInventory(null, 9, Api.color("&c&lEnchantment Info"));
								for(ItemStack i : getInfo("helmet")){
									in.addItem(i);
								}
								in.setItem(8, Api.makeItem(Material.PRISMARINE_CRYSTALS, 1, 0, "&7&l<<&b&lBack"));
								e.getWhoClicked().openInventory(in);
								return;
							}
							if(item.getItemMeta().getDisplayName().equals(Api.color("&e&lBoot Enchantments"))){
								Inventory in = Bukkit.createInventory(null, 9, Api.color("&c&lEnchantment Info"));
								for(ItemStack i : getInfo("boots")){
									in.addItem(i);
								}
								in.setItem(8, Api.makeItem(Material.PRISMARINE_CRYSTALS, 1, 0, "&7&l<<&b&lBack"));
								e.getWhoClicked().openInventory(in);
								return;
							}
							if(item.getItemMeta().getDisplayName().equals(Api.color("&e&lArmor Enchantments"))){
								Inventory in = Bukkit.createInventory(null, 18, Api.color("&c&lEnchantment Info"));
								for(ItemStack i : getInfo("armor")){
									in.addItem(i);
								}
								in.setItem(17, Api.makeItem(Material.PRISMARINE_CRYSTALS, 1, 0, "&7&l<<&b&lBack"));
								e.getWhoClicked().openInventory(in);
								return;
							}
							if(item.getItemMeta().getDisplayName().equals(Api.color("&e&lSword Enchantments"))){
								Inventory in = Bukkit.createInventory(null, 27, Api.color("&c&lEnchantment Info"));
								for(ItemStack i : getInfo("sword")){
									in.addItem(i);
								}
								in.setItem(26, Api.makeItem(Material.PRISMARINE_CRYSTALS, 1, 0, "&7&l<<&b&lBack"));
								e.getWhoClicked().openInventory(in);
								return;
							}
							if(item.getItemMeta().getDisplayName().equals(Api.color("&e&lAxe Enchantments"))){
								Inventory in = Bukkit.createInventory(null, 9, Api.color("&c&lEnchantment Info"));
								for(ItemStack i : getInfo("axe")){
									in.addItem(i);
								}
								in.setItem(8, Api.makeItem(Material.PRISMARINE_CRYSTALS, 1, 0, "&7&l<<&b&lBack"));
								e.getWhoClicked().openInventory(in);
								return;
							}
							if(item.getItemMeta().getDisplayName().equals(Api.color("&e&lBow Enchantments"))){
								Inventory in = Bukkit.createInventory(null, 9, Api.color("&c&lEnchantment Info"));
								for(ItemStack i : getInfo("bow")){
									in.addItem(i);
								}
								in.setItem(8, Api.makeItem(Material.PRISMARINE_CRYSTALS, 1, 0, "&7&l<<&b&lBack"));
								e.getWhoClicked().openInventory(in);
								return;
							}
							if(item.getItemMeta().getDisplayName().equals(Api.color("&e&lPickaxe Enchantments"))){
								Inventory in = Bukkit.createInventory(null, 9, Api.color("&c&lEnchantment Info"));
								for(ItemStack i : getInfo("pick")){
									in.addItem(i);
								}
								in.setItem(8, Api.makeItem(Material.PRISMARINE_CRYSTALS, 1, 0, "&7&l<<&b&lBack"));
								e.getWhoClicked().openInventory(in);
								return;
							}
							if(item.getItemMeta().getDisplayName().equals(Api.color("&e&lTool Enchantments"))){
								Inventory in = Bukkit.createInventory(null, 9, Api.color("&c&lEnchantment Info"));
								for(ItemStack i : getInfo("tools")){
									in.addItem(i);
								}
								in.setItem(8, Api.makeItem(Material.PRISMARINE_CRYSTALS, 1, 0, "&7&l<<&b&lBack"));
								e.getWhoClicked().openInventory(in);
								return;
							}
						}
					}
				}
				return;
			}
		}
	}
	public static void openInfo(Player player){
		Inventory inv = Bukkit.createInventory(null, 9, Api.color("&c&lEnchantment Info"));
		inv.addItem(Api.makeItem(Material.GOLD_HELMET, 1, 0, "&e&lHelmet Enchantments", 
				Arrays.asList("&cEnchantable Items:", "&a- Leather Helmet", "&a- Chain Helmet", "&a- Iron Helmet", "&a- Gold Helmet", "&a- Diamond Helmet")));
		inv.addItem(Api.makeItem(Material.GOLD_BOOTS, 1, 0, "&e&lBoot Enchantments", 
				Arrays.asList("&cEnchantable Items:", "&a- Leather Boots", "&a- Chain Boots", "&a- Iron Boots", "&a- Gold Boots", "&a- Diamond Boots")));
		inv.addItem(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lArmor Enchantments", 
				Arrays.asList("&cEnchantable Items:", "&a- All Leather Armor", "&a- All Chain Armor", "&a- All Iron Armor", "&a- All Gold Armor", "&a- All Diamond Armor")));
		inv.addItem(Api.makeItem(Material.BOW, 1, 0, "&e&lBow Enchantments", 
				Arrays.asList("&cEnchantable Items:", "&a- Bow")));
		inv.addItem(Api.makeItem(Material.GOLD_SWORD, 1, 0, "&e&lSword Enchantments", 
				Arrays.asList("&cEnchantable Items:", "&a- Wood Sword", "&a- Stone Sword", "&a- Iron Sword", "&a- Gold Sword", "&a- Diamond Sword")));
		inv.addItem(Api.makeItem(Material.GOLD_AXE, 1, 0, "&e&lAxe Enchantments", 
				Arrays.asList("&cEnchantable Items:", "&a- Wood Axe", "&a- Stone Axe", "&a- Iron Axe", "&a- Gold Axe", "&a- Diamond Axe")));
		inv.addItem(Api.makeItem(Material.GOLD_HOE, 1, 0, "&e&lTool Enchantments", 
				Arrays.asList("&cEnchantable Items:", "&a- All Pickaxes", "&a- All Axes", "&a- All Shovels", "&a- All Hoes")));
		inv.addItem(Api.makeItem(Material.GOLD_PICKAXE, 1, 0, "&e&lPickaxe Enchantments", 
				Arrays.asList("&cEnchantable Items:", "&a- Wood Pickaxe", "&a- Stone Pickaxe", "&a- Iron Pickaxe", "&a- Gold Pickaxe", "&a- Diamond Pickaxe")));
		player.openInventory(inv);
	}
	public static ArrayList<ItemStack> getInfo(String type){
		ArrayList<ItemStack> swords = new ArrayList<ItemStack>();
		ArrayList<ItemStack> axes = new ArrayList<ItemStack>();
		ArrayList<ItemStack> bows = new ArrayList<ItemStack>();
		ArrayList<ItemStack> armor = new ArrayList<ItemStack>();
		ArrayList<ItemStack> helmets = new ArrayList<ItemStack>();
		ArrayList<ItemStack> boots = new ArrayList<ItemStack>();
		ArrayList<ItemStack> picks = new ArrayList<ItemStack>();
		ArrayList<ItemStack> tools = new ArrayList<ItemStack>();
		for(String en : Main.settings.getEnchs().getConfigurationSection("Enchantments").getKeys(false)){
			String name = Main.settings.getEnchs().getString("Enchantments."+en+".Info.Name");
			List<String> desc = Main.settings.getEnchs().getStringList("Enchantments."+en+".Info.Description");
			ArrayList<Material> Items = ECControl.allEnchantments().get(en);
			ItemStack i = Api.addGlow(Api.makeItem(Material.BOOK, 1, 0, name, desc));
			if(Items.equals(ECControl.isArmor()))armor.add(i);
			if(Items.equals(ECControl.isSword()))swords.add(i);
			if(Items.equals(ECControl.isAxe()))axes.add(i);
			if(Items.equals(ECControl.isBow()))bows.add(i);
			if(Items.equals(ECControl.isHelmet()))helmets.add(i);
			if(Items.equals(ECControl.isBoots()))boots.add(i);
			if(Items.equals(ECControl.isPickAxe()))picks.add(i);
			if(Items.equals(ECControl.isTool()))tools.add(i);
		}
		if(type.equalsIgnoreCase("Armor"))return armor;
		if(type.equalsIgnoreCase("Sword"))return swords;
		if(type.equalsIgnoreCase("Helmet"))return helmets;
		if(type.equalsIgnoreCase("Boots"))return boots;
		if(type.equalsIgnoreCase("Bow"))return bows;
		if(type.equalsIgnoreCase("Axe"))return axes;
		if(type.equalsIgnoreCase("Pick"))return picks;
		if(type.equalsIgnoreCase("Tools"))return tools;
		return null;
	}
}