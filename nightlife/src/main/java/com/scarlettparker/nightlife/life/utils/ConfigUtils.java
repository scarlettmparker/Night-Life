package com.scarlettparker.nightlife.life.utils;

import com.google.gson.*;
import org.bukkit.Bukkit;
import java.io.*;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class ConfigUtils {
  public static final File playerFile = new File(Objects.requireNonNull(Bukkit.getServer()
    .getPluginManager()).getPlugin("nightlife").getDataFolder(), "players.json");

  private static final Logger LOGGER = Logger.getLogger("nightlife");
  private static final Gson gson = new Gson();

  /**
   * Create a JSON file if it does not exist.
   * @param JSONFile File to create.
   */
  public static void createJSONFile(File JSONFile) {
    try {
      File parentDir = JSONFile.getParentFile();

      if (!parentDir.exists() && !parentDir.mkdirs())
        LOGGER.severe("Failed to create directory: " + parentDir.getAbsolutePath());
      if (JSONFile.exists() && !JSONFile.delete())
        LOGGER.warning("Could not delete existing JSON file: " + JSONFile.getAbsolutePath());
      if (!JSONFile.createNewFile())
        LOGGER.severe("Could not create JSON file: " + JSONFile.getAbsolutePath());

      try (FileWriter writer = new FileWriter(JSONFile)) {
        writer.write("{}");
      }
      LOGGER.info("Successfully created JSON file: " + JSONFile.getAbsolutePath());
    } catch (IOException e) {
      LOGGER.severe("Error creating JSON file: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Add an object to the JSON file. For example, a player object.
   * @param JSONFile File to add object to.
   * @param JSON JSON object to add.
   */
  public static void addJSONObject(File JSONFile, String JSON) {
    JsonObject newJSONObject = gson.fromJson(JSON, JsonObject.class);
    JsonObject existingJSONObject = new JsonObject();

    if (existingJSONObject != null) {
      existingJSONObject.add(newJSONObject.get("uuid").getAsString(), newJSONObject);
      writeJSONToFile(JSONFile, existingJSONObject);
    } else {
      JsonObject jsonObject = new JsonObject();
      jsonObject.add(newJSONObject.get("uuid").getAsString(), newJSONObject);
      writeJSONToFile(JSONFile, jsonObject);
    }
  }

  /**
   * Set an attribute of a JSON object. For example, set the number of lives a player has.
   * @param JSONFile File to set attribute in.
   * @param JSONObjectName Name of the JSON object.
   * @param attribute Attribute to set.
   * @param value Value to set attribute to.
   */
  public static void setJSONObjectAttribute(File JSONFile, String JSONObjectName, String attribute, Object value) {
    JsonObject jsonObject = readJSONFile(JSONFile);
    if (jsonObject == null) {
      jsonObject = new JsonObject();
    }

    JsonObject targetObject;
    if (!jsonObject.has(JSONObjectName)) {
      targetObject = new JsonObject();
      jsonObject.add(JSONObjectName, targetObject);
    } else {
      targetObject = jsonObject.getAsJsonObject(JSONObjectName);
    }

    if (value == null) {
      targetObject.add(attribute, JsonNull.INSTANCE);
    } else if (value instanceof Boolean) {
      targetObject.addProperty(attribute, (Boolean) value);
    } else if (value instanceof Number) {
      targetObject.addProperty(attribute, (Number) value);
    } else {
      targetObject.addProperty(attribute, value.toString());
    }

    writeJSONToFile(JSONFile, jsonObject);
  }

  /**
   * Get an attribute of a JSON object. For example, get the number of lives a player has.
   * @param JSONFile File to get attribute from.
   * @param JSONObjectName Name of the JSON object.
   * @param attribute Attribute to get.
   * @return Attribute value.
   */
  public static Object getJSONObjectAttribute(File JSONFile, String JSONObjectName, String attribute) {
    JsonObject JSONObject = readJSONFile(JSONFile);

    if (JSONObject != null && JSONObject.has(JSONObjectName)) {
      JsonElement attributeValue = JSONObject.getAsJsonObject(JSONObjectName).get(attribute);
      if (attributeValue != null) {
        if (attributeValue.isJsonNull()) {
          return null;
        }
        if (attributeValue instanceof JsonPrimitive) {
          JsonPrimitive primitive = attributeValue.getAsJsonPrimitive();
          if (primitive.isBoolean()) {
            return primitive.getAsBoolean();
          }
          if (primitive.isNumber()) {
            String numStr = primitive.getAsString();
            return numStr.contains(".") ? primitive.getAsDouble() : primitive.getAsInt();
          }
          return primitive.getAsString();
        }
      }
    }
    return null;
  }

  /**
   * Helper function to read from a JSON file.
   * @param JSONFile File to read from.
   * @return JSON object.
   */
  public static JsonObject readJSONFile(File JSONFile) {
    try (FileReader reader = new FileReader(JSONFile)) {
      return gson.fromJson(reader, JsonObject.class);
    } catch (IOException e) {
      LOGGER.warning("Could not read JSON file: " + JSONFile.getName());
      return null;
    }
  }

  /**
   * Helper function to write to a JSON file.
   * @param JSONFile File to write to.
   * @param jsonObject JSON object to write.
   */
  public static void writeJSONToFile(File JSONFile, JsonObject jsonObject) {
    try (FileWriter writer = new FileWriter(JSONFile)) {
      Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
      gsonPretty.toJson(jsonObject, writer);
    } catch (IOException e) {
      LOGGER.warning("Could not write to JSON file: " + JSONFile.getName());
    }
  }

  /**
   * Helper function to return all objects in a JSON file.
   * @param JSONFile File to read from.
   * @return JSON object.
   */
  public static JsonObject returnAllObjects(File JSONFile) {
    return readJSONFile(JSONFile);
  }

  /**
   * Helper function to delete a JSON object by name.
   * @param JSONFile File to delete object from.
   * @param JSONObjectName Name of the JSON object to delete.
   * @return True if object was deleted, false otherwise.
   */
  public static boolean deleteJSONObjectByName(File JSONFile, String JSONObjectName) {
    JsonObject jsonObject = readJSONFile(JSONFile);
    if (jsonObject != null) {
      if (jsonObject.has(JSONObjectName)) {
        jsonObject.remove(JSONObjectName);
        writeJSONToFile(JSONFile, jsonObject);
        return true;
      }
    }
    return false;
  }

  public static boolean JSONFileExists(File JSONFile) {
    return JSONFile.exists();
  }

  public static boolean playerExists(UUID playerUUID) {
    return getJSONObjectAttribute(playerFile, playerUUID.toString(), "uuid") != null;
  }
}