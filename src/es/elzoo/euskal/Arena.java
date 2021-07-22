package es.elzoo.euskal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class Arena {
	public static Map<String, Arena> arenas = new HashMap<String, Arena>();
	private static EuskalPVP plugin = (EuskalPVP) Bukkit.getPluginManager().getPlugin("EuskalPVP");
	
	private static ItemStack casco, pechera, pantalones, botas;
	private static ItemStack[] kit;
	
	private Location pos1, pos2;
	private String nombre;
	
	public Arena(String nombre, Location pos1, Location pos2) {
		this.nombre = nombre;
		this.pos1 = pos1;
		this.pos2 = pos2;
		
		arenas.put(nombre, this);
		
		if(casco == null) {
			crearKit();
		}
	}
	
	public void borrar() {
		arenas.remove(nombre);		
		
		plugin.getConfig().getConfigurationSection("arenas").set(nombre, null);
		plugin.saveConfig();
	}
	
	public void guardarConfig() {				
		ConfigurationSection arenasRaw = plugin.getConfig().getConfigurationSection("arenas");
		if(arenasRaw == null) {
			arenasRaw = plugin.getConfig().createSection("arenas");
		}
		
		ConfigurationSection configArena = arenasRaw.createSection(nombre);
		
		ConfigurationSection pos1Conf = configArena.createSection("pos1");
		pos1Conf.set("mundo", pos1.getWorld().getName());
		pos1Conf.set("x", pos1.getX());
		pos1Conf.set("y", pos1.getY());
		pos1Conf.set("z", pos1.getZ());
		pos1Conf.set("pitch", (double) pos1.getPitch());
		pos1Conf.set("yaw", (double) pos1.getYaw());
		
		ConfigurationSection pos2Conf = configArena.createSection("pos2");
		pos2Conf.set("mundo", pos2.getWorld().getName());
		pos2Conf.set("x", pos2.getX());
		pos2Conf.set("y", pos2.getY());
		pos2Conf.set("z", pos2.getZ());
		pos2Conf.set("pitch", (double) pos2.getPitch());
		pos2Conf.set("yaw", (double) pos2.getYaw());
		
		plugin.saveConfig();
	}
	
	public void luchar(Player p1, Player p2) {
		preparar(p1, pos1);
		preparar(p2, pos2);
		
		Utils.broadcast("&6¡Ha comenzado el combate de&r "+p1.getName()+" &6vs&r "+p2.getName()+"&6!");
	}
	
	public void parar() {
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		ProtectedRegion reg = container.get(new BukkitWorld(pos1.getWorld())).getRegion(nombre);
		if(reg == null) {
			return;
		}
		
		pos1.getWorld().getPlayers().forEach(pl -> {
			if(pl.getGameMode() != GameMode.SURVIVAL) {
				return;
			}
			
			Location pos = pl.getLocation();
			if(!reg.contains(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ())) {
				return;
			}
			
			Utils.limpiar(pl);
		});
	}
	
	private static void preparar(Player player, Location pos) {
		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		
		player.getInventory().setHelmet(casco.clone());
		player.getInventory().setChestplate(pechera.clone());
		player.getInventory().setLeggings(pantalones.clone());
		player.getInventory().setBoots(botas.clone());
		
		for(ItemStack item : kit) {
			player.getInventory().addItem(item.clone());
		}
		
		player.teleportAsync(pos);
	}
	
	public static Optional<Arena> getArena(String nombre) {
		return arenas.values().stream().filter(entry -> entry.getNombre().equalsIgnoreCase(nombre)).findFirst();
	}
	
	public static Optional<Arena> getArena(Player player) {
		return getArena(player.getLocation());
	}
	
	public static Optional<Arena> getArena(Location loc) {
		Optional<Arena> arena = Optional.ofNullable(null);
		
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager regiones = container.get(new BukkitWorld(loc.getWorld()));
		
		Optional<String> arenaId = regiones.getApplicableRegions(BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
			.getRegions()
			.stream()
			.filter((rg) -> arenas.keySet().stream().anyMatch(ar -> ar.equalsIgnoreCase(rg.getId())))
			.map(rg -> rg.getId())
			.findFirst();
		
		if(arenaId.isPresent()) {
			arena = getArena(arenaId.get());
		}
		
		return arena;
	}
	
	public static Optional<Arena> getPrimeraArena() {
		return arenas.values().stream().findFirst();
	}
	
	public static void crearKit() {		
		casco = armadura(Material.NETHERITE_HELMET);
		pechera = armadura(Material.NETHERITE_CHESTPLATE);
		pantalones = armadura(Material.NETHERITE_LEGGINGS);
		botas = armadura(Material.NETHERITE_BOOTS);
		
		kit = new ItemStack[] {
			espada(Material.NETHERITE_SWORD),
			new ItemStack(Material.GOLDEN_APPLE, 3),
			new ItemStack(Material.GOLDEN_CARROT, 24),
			new ItemStack(Material.ENDER_PEARL, 6)
		};
	}
	
	public static ItemStack espada(Material mat) {
		ItemStack item = new ItemStack(mat);
		
		item.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		item.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		
		return item;
	}
	
	public static ItemStack armadura(Material mat) {
		ItemStack item = new ItemStack(mat);
		
		item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		item.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		
		return item;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Location getPos1() {
		return pos1;
	}
	
	public void setPos1(Location pos) {
		this.pos1 = pos;
	}
	
	public Location getPos2() {
		return pos2;
	}
	
	public void setPos2(Location pos) {
		this.pos2 = pos;
	}
}
