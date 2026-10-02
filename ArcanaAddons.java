package net.arcana.addons;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import net.arcana.addons.ability.AbilityManager;
import net.arcana.addons.ability.impl.RubySlashAbility;
import net.arcana.addons.block.BlockDisplayManager;
import net.arcana.addons.block.BlockListener;
import net.arcana.addons.block.BlockManager;
import net.arcana.addons.command.ItemCommand;
import net.arcana.addons.durability.DurabilityManager;
import net.arcana.addons.effect.EffectManager;
import net.arcana.addons.entity.CustomEntityManager;
import net.arcana.addons.entity.EntityCombatManager;
import net.arcana.addons.entity.EntityListener;
import net.arcana.addons.entity.EntityPhaseManager;
import net.arcana.addons.entity.EntitySpawner;
import net.arcana.addons.entity.EntityTask;
import net.arcana.addons.entity.animation.EntityAnimationManager;
import net.arcana.addons.entity.visual.VisualEntityManager;
import net.arcana.addons.entity.visual.VisualEntityTask;
import net.arcana.addons.item.ItemListener;
import net.arcana.addons.item.ItemManager;
import net.arcana.addons.resourcepack.ResourcePackGenerator;
import net.arcana.addons.resourcepack.ResourcePackListener;
import net.arcana.addons.resourcepack.sound.SoundManager;

public final class ArcanaAddons extends JavaPlugin {

	private static ArcanaAddons instance;

	/*
	 * Items
	 */
	private ItemManager itemManager;
	private AbilityManager abilityManager;
	private DurabilityManager durabilityManager;

	/*
	 * Resource Pack
	 */
	private ResourcePackGenerator resourcePackGenerator;

	/*
	 * Blocks
	 */
	private BlockManager blockManager;
	private BlockDisplayManager blockDisplayManager;

	/*
	 * Effects
	 */
	private EffectManager effectManager;

	/*
	 * Keys
	 */
	private ArcanaKeys keys;

	/*
	 * Entities
	 */
	private CustomEntityManager entityManager;
	private EntityCombatManager entityCombatManager;
	private EntityPhaseManager entityPhaseManager;
	private EntitySpawner entitySpawner;
	private EntityTask entityTask;
	private EntityAnimationManager entityAnimationManager;

	/*
	 * Entity Visuals
	 */
	private VisualEntityManager visualEntityManager;
	private VisualEntityTask visualEntityTask;
	/*
	 * Sound
	 */
	private SoundManager soundManager;

	@Override
	public void onEnable() {

		instance = this;

		saveDefaultConfig();

		getLogger().info("Starting ArcanaAddons...");

		/*
		 * ===================================== ITEMS
		 * =====================================
		 */

		itemManager = new ItemManager(this);

		abilityManager = new AbilityManager(this);

		durabilityManager = new DurabilityManager(this);

		/*
		 * ===================================== RESOURCE PACK
		 * =====================================
		 */

		resourcePackGenerator = new ResourcePackGenerator(this);

		/*
		 * ===================================== BLOCKS
		 * =====================================
		 */

		blockManager = new BlockManager(this);

		blockManager.load();

		blockDisplayManager = new BlockDisplayManager(this);

		/*
		 * ===================================== EFFECTS
		 * =====================================
		 */

		effectManager = new EffectManager(this);

		effectManager.load();

		/*
		 * ===================================== KEYS
		 * =====================================
		 */

		keys = new ArcanaKeys(this);

		/*
		 * ===================================== ENTITIES
		 * =====================================
		 */

		entityManager = new CustomEntityManager(this);

		entityManager.load();

		entityCombatManager = new EntityCombatManager();

		entityPhaseManager = new EntityPhaseManager();

		entitySpawner = new EntitySpawner(this);

		/*
		 * ===================================== ENTITY VISUALS & SOUNDS
		 * =====================================
		 */

		visualEntityManager = new VisualEntityManager();
		soundManager = new SoundManager(this);
		visualEntityManager = new VisualEntityManager();
		entityAnimationManager = new EntityAnimationManager(visualEntityManager);

		/*
		 * ===================================== TASKS
		 * =====================================
		 */

		entityTask = new EntityTask(this);

		entityTask.runTaskTimer(this, 1L, 5L);

		visualEntityTask = new VisualEntityTask(visualEntityManager, entityAnimationManager);
		
		visualEntityTask.runTaskTimer(this, 1L, 1L);

		/*
		 * ===================================== ABILITIES
		 * =====================================
		 */

		registerAbilities();

		/*
		 * ===================================== LOAD ITEMS
		 * =====================================
		 */

		itemManager.loadItems();

		/*
		 * ===================================== EVENTS
		 * =====================================
		 */

		getServer().getPluginManager().registerEvents(new ItemListener(this), this);

		getServer().getPluginManager().registerEvents(new BlockListener(this), this);

		getServer().getPluginManager().registerEvents(new ResourcePackListener(this), this);

		getServer().getPluginManager().registerEvents(new EntityListener(this), this);

		/*
		 * ===================================== COMMAND
		 * =====================================
		 */

		PluginCommand command = getCommand("arcanaitems");

		if (command == null) {

			getLogger().severe("Command 'arcanaitems' is missing from plugin.yml!");

			getServer().getPluginManager().disablePlugin(this);

			return;
		}

		ItemCommand itemCommand = new ItemCommand(this);

		command.setExecutor(itemCommand);

		command.setTabCompleter(itemCommand);

		/*
		 * ===================================== READY
		 * =====================================
		 */

		getLogger().info("ArcanaAddons enabled successfully.");

		getLogger().info("Loaded items: " + itemManager.getRegistry().size());

		getLogger().info("Loaded abilities: " + abilityManager.size());

		getLogger().info("Loaded entities: " + entityManager.size());
	}

	@Override
	public void onDisable() {

		getLogger().info("Stopping ArcanaAddons...");

		/*
		 * ===================================== TASKS
		 * =====================================
		 */

		if (visualEntityTask != null) {

			visualEntityTask.cancel();

			visualEntityTask = null;
		}

		if (entityTask != null) {

			entityTask.cancel();

			entityTask = null;
		}

		/*
		 * ===================================== ENTITY VISUALS & sound
		 * =====================================
		 */

		if (visualEntityManager != null) {

			visualEntityManager.clear();
		}
		if (soundManager != null) {
			soundManager.clear();
		}

		/*
		 * ===================================== ENTITIES
		 * =====================================
		 */

		if (entityCombatManager != null) {

			entityCombatManager.clearAll();
		}

		if (entityManager != null) {

			entityManager.shutdown();
		}

		if (entityAnimationManager != null) {
			entityAnimationManager.clear();
		}
		/*
		 * ===================================== BLOCKS
		 * =====================================
		 */

		if (blockDisplayManager != null) {

			blockDisplayManager.removeAll();
		}

		if (blockManager != null) {

			blockManager.shutdown();
		}

		/*
		 * ===================================== EFFECTS
		 * =====================================
		 */

		if (effectManager != null) {

			effectManager.shutdown();
		}

		/*
		 * ===================================== ITEMS
		 * =====================================
		 */

		if (itemManager != null) {

			itemManager.shutdown();
		}

		if (abilityManager != null) {

			abilityManager.shutdown();
		}

		/*
		 * ===================================== END
		 * =====================================
		 */

		getLogger().info("ArcanaAddons disabled.");

		instance = null;
	}

	/*
	 * ========================================= ABILITIES
	 * =========================================
	 */

	private void registerAbilities() {

		abilityManager.register(new RubySlashAbility());
	}

	/*
	 * ========================================= GETTERS
	 * =========================================
	 */

	public static ArcanaAddons getInstance() {
		return instance;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}

	public AbilityManager getAbilityManager() {
		return abilityManager;
	}

	public DurabilityManager getDurabilityManager() {
		return durabilityManager;
	}

	public ResourcePackGenerator getResourcePackGenerator() {
		return resourcePackGenerator;
	}

	public BlockManager getBlockManager() {
		return blockManager;
	}

	public BlockDisplayManager getBlockDisplayManager() {
		return blockDisplayManager;
	}

	public EffectManager getEffectManager() {
		return effectManager;
	}

	public ArcanaKeys getKeys() {
		return keys;
	}

	public CustomEntityManager getEntityManager() {
		return entityManager;
	}

	public EntityCombatManager getEntityCombatManager() {
		return entityCombatManager;
	}

	public EntityPhaseManager getEntityPhaseManager() {
		return entityPhaseManager;
	}

	public EntitySpawner getEntitySpawner() {
		return entitySpawner;
	}

	public VisualEntityManager getVisualEntityManager() {
		return visualEntityManager;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public EntityAnimationManager getEntityAnimationManager() {
		return entityAnimationManager;
	}
}