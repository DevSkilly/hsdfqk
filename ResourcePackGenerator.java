package net.arcana.addons.resourcepack;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.arcana.addons.ArcanaAddons;
import net.arcana.addons.item.CustomItem;
import net.arcana.addons.item.ItemDefinition;
import net.arcana.addons.resourcepack.sound.CustomSoundDefinition;

public final class ResourcePackGenerator {

	private final ArcanaAddons plugin;

	private final File resourcePackFolder;
	private final File generatedFolder;

	public ResourcePackGenerator(ArcanaAddons plugin) {
		this.plugin = plugin;

		this.resourcePackFolder = new File(plugin.getDataFolder(), "resourcepack");

		this.generatedFolder = new File(resourcePackFolder, "generated");
	}

	public File build() throws IOException {

		prepareDirectories();

		writePackMeta();

		Map<String, List<ModelOverride>> overrides = new HashMap<>();

		for (CustomItem customItem : plugin.getItemManager().getRegistry().getItems().values()) {

			ItemDefinition definition = customItem.getDefinition();

			ResourcePackDefinition resource = definition.getResourcePack();

			if (resource == null) {
				continue;
			}

			generateCustomModel(resource);

			copyTexture(resource);

			String material = definition.getMaterial().toLowerCase();
			int modelData = plugin.getItemManager().getModelDataManager().getOrCreate(definition.getId());

			overrides.computeIfAbsent(material, key -> new ArrayList<>())
					.add(new ModelOverride(modelData, resource.getModel()));
		}

		generateVanillaModels(overrides);

		File zip = new File(generatedFolder, "ArcanaItems.zip");

		if (zip.exists() && !zip.delete()) {

			throw new IOException("Unable to replace old resource pack.");
		}

		zipDirectory(generatedFolder, zip);

		plugin.getLogger().info("Resource Pack generated successfully.");

		return zip;
	}

	private void copySound(CustomSoundDefinition sound) throws IOException {

		File source = new File(resourcePackFolder, "sounds/" + sound.getFile());

		if (!source.exists()) {

			throw new FileNotFoundException("Custom sound not found: " + source.getAbsolutePath());
		}

		File destination = new File(generatedFolder, "assets/arcana/sounds/" + sound.getFile());

		createParent(destination);

		Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	private void generateSoundsJson(Collection<CustomSoundDefinition> sounds) throws IOException {

		if (sounds.isEmpty()) {
			return;
		}

		File file = new File(generatedFolder, "assets/arcana/sounds.json");

		createParent(file);

		StringBuilder json = new StringBuilder();

		json.append("{\n");

		int index = 0;

		for (CustomSoundDefinition sound : sounds) {

			json.append("  \"").append(sound.getId()).append("\": {\n");

			json.append("    \"category\": \"player\",\n");

			json.append("    \"sounds\": [\n");

			json.append("      {\n");

			json.append("        \"name\": \"arcana:").append(sound.getId()).append("\",\n");

			json.append("        \"volume\": ").append(sound.getVolume()).append(",\n");

			json.append("        \"pitch\": ").append(sound.getPitch()).append("\n");

			json.append("      }\n");

			json.append("    ]\n");

			json.append("  }");

			if (index < sounds.size() - 1) {
				json.append(",");
			}

			json.append("\n");

			index++;
		}

		json.append("}\n");

		Files.writeString(file.toPath(), json.toString());
	}

	private void prepareDirectories() throws IOException {

		if (!resourcePackFolder.exists() && !resourcePackFolder.mkdirs()) {

			throw new IOException("Unable to create resourcepack folder.");
		}

		if (!generatedFolder.exists() && !generatedFolder.mkdirs()) {

			throw new IOException("Unable to create generated folder.");
		}

		File assets = new File(generatedFolder, "assets");

		if (assets.exists()) {
			deleteDirectory(assets);
		}

		if (!assets.mkdirs()) {
			throw new IOException("Unable to create assets folder.");
		}
	}

	private void writePackMeta() throws IOException {

		File file = new File(generatedFolder, "pack.mcmeta");

		String json = """
				{
				  "pack": {
				    "pack_format": 22,
				    "description": "Arcana Network Items"
				  }
				}
				""";

		Files.writeString(file.toPath(), json);
	}

	private void generateCustomModel(ResourcePackDefinition resource) throws IOException {

		File modelFile = new File(generatedFolder, "assets/arcana/models/item/" + resource.getModel() + ".json");

		createParent(modelFile);

		String json = """
				{
				  "parent": "minecraft:item/handheld",
				  "textures": {
				    "layer0": "arcana:item/%s"
				  }
				}
				""".formatted(removeExtension(resource.getTexture()));

		Files.writeString(modelFile.toPath(), json);
	}

	private void generateVanillaModels(Map<String, List<ModelOverride>> overrides) throws IOException {

		for (Map.Entry<String, List<ModelOverride>> entry : overrides.entrySet()) {

			String material = entry.getKey();

			List<ModelOverride> models = entry.getValue();

			models.sort(Comparator.comparingInt(ModelOverride::modelData));

			StringBuilder json = new StringBuilder();

			json.append("{\n");

			json.append("  \"parent\": \"minecraft:item/handheld\",\n");

			json.append("  \"textures\": {\n");

			json.append("    \"layer0\": \"minecraft:item/" + material + "\"\n");

			json.append("  },\n");

			json.append("  \"overrides\": [\n");

			for (int i = 0; i < models.size(); i++) {

				ModelOverride override = models.get(i);

				json.append("    {\n");

				json.append("      \"predicate\": {\n");

				json.append("        \"custom_model_data\": " + override.modelData() + "\n");

				json.append("      },\n");

				json.append("      \"model\": \"arcana:item/" + override.model() + "\"\n");

				json.append("    }");

				if (i < models.size() - 1) {
					json.append(",");
				}

				json.append("\n");
			}

			json.append("  ]\n");
			json.append("}\n");

			File file = new File(generatedFolder, "assets/minecraft/models/item/" + material + ".json");

			createParent(file);

			Files.writeString(file.toPath(), json.toString());
		}
	}

	private void copyTexture(ResourcePackDefinition resource) throws IOException {

		File source = new File(resourcePackFolder, "textures/" + resource.getTexture());

		if (!source.exists()) {

			throw new FileNotFoundException("Texture not found: " + source.getAbsolutePath());
		}

		File destination = new File(generatedFolder, "assets/arcana/textures/item/" + resource.getTexture());

		createParent(destination);

		Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	private String removeExtension(String filename) {

		if (filename.endsWith(".png")) {
			return filename.substring(0, filename.length() - 4);
		}

		return filename;
	}

	private void createParent(File file) throws IOException {

		File parent = file.getParentFile();

		if (parent != null && !parent.exists() && !parent.mkdirs()) {

			throw new IOException("Unable to create directory: " + parent);
		}
	}

	private void zipDirectory(File directory, File output) throws IOException {

		Path base = directory.toPath();

		try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(output))) {

			Files.walk(base).filter(Files::isRegularFile).forEach(path -> {

				try {

					if (path.equals(output.toPath())) {
						return;
					}

					String entry = base.relativize(path).toString().replace(File.separatorChar, '/');

					zip.putNextEntry(new ZipEntry(entry));

					Files.copy(path, zip);

					zip.closeEntry();

				} catch (IOException exception) {

					throw new UncheckedIOException(exception);
				}
			});
		}
	}

	private void deleteDirectory(File directory) throws IOException {

		if (!directory.exists()) {
			return;
		}

		Files.walk(directory.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
	}

	public String generateSha1(File file) throws Exception {

		MessageDigest digest = MessageDigest.getInstance("SHA-1");

		try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {

			byte[] buffer = new byte[8192];

			int read;

			while ((read = input.read(buffer)) != -1) {

				digest.update(buffer, 0, read);
			}
		}

		StringBuilder result = new StringBuilder();

		for (byte b : digest.digest()) {

			result.append(String.format("%02x", b));
		}

		return result.toString();
	}

	private record ModelOverride(int modelData, String model) {
	}

	private void publishToWamp(File zip) throws IOException {

		boolean enabled = plugin.getConfig().getBoolean("resource-pack.wamp-output.enabled", false);

		if (!enabled) {
			return;
		}

		String path = plugin.getConfig().getString("resource-pack.wamp-output.path");

		if (path == null || path.isBlank()) {

			throw new IOException("WAMP output path is not configured.");
		}

		File destination = new File(path);

		File parent = destination.getParentFile();

		if (parent != null && !parent.exists() && !parent.mkdirs()) {

			throw new IOException("Unable to create WAMP directory: " + parent);
		}

		Files.copy(zip.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

		plugin.getLogger().info("Resource Pack published to WAMP: " + destination.getAbsolutePath());
	}
}