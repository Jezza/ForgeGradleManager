package me.jezza.fgpm.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.jezza.fgpm.App;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collection;

public class ConfigHandler {

    public static final Type jsonArrayType = new TypeToken<JsonArray>() {
    }.getType();

    private static final File configFile = new File("./FGMM.cfg");

    public static void save() {
        App.LOG.info("Saving...");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JsonArray jsonArray = new JsonArray();
        Collection<IJsonObject> handlers = App.HANDLERS;
        for (IJsonObject handler : handlers) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("objectClazz", handler.getClass().getCanonicalName());
            handler.toJsonObject(jsonObject);
            jsonArray.add(jsonObject);
        }

        try {
            try (OutputStream os = new FileOutputStream(configFile)) {
                IOUtils.write(jsonArray.toString(), os);
            }
        } catch (Exception e) {
            App.LOG.error("Failed to save to config.", e);
        }
    }

    public static void load() {
        App.LOG.info("Loading...");
        if (!configFile.exists()) {
            App.LOG.info("Failed to locate Config File: {}", configFile);
            return;
        }

        String jsonString = "";
        try {
            try (InputStream is = new FileInputStream(configFile)) {
                jsonString = IOUtils.toString(is);
            }
        } catch (Exception e) {
            App.LOG.error("Failed to save to config.", e);
        }

        if (!jsonString.isEmpty()) {
            JsonArray jsonArray = new Gson().fromJson(jsonString, jsonArrayType);
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                if (!jsonElement.isJsonObject())
                    continue;
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (!jsonObject.has("objectClazz"))
                    continue;
                String objectClazz = jsonObject.get("objectClazz").getAsString();
                Class<?> clazz;
                try {
                    clazz = Class.forName(objectClazz);
                } catch (ClassNotFoundException e) {
                    App.LOG.error("Failed to find class {}", objectClazz);
                    continue;
                }
                if (clazz == null || !IJsonObject.class.isAssignableFrom(clazz))
                    continue;

                if (IJsonObjectInstance.class.isAssignableFrom(clazz)) {
                    IJsonObjectInstance jsonHandler = create(clazz);
                    if (jsonHandler != null)
                        jsonHandler.getMainInstance().fromJsonObject(jsonObject);
                } else {
                    IJsonObject jsonHandler = create(clazz);
                    if (jsonHandler != null)
                        jsonHandler.fromJsonObject(jsonObject);
                }

            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends IJsonObject> T create(Class<?> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            App.LOG.error("Failed to create instance. {}", clazz);
            App.LOG.error("Thrown exception: ", e);
        }
        return null;
    }
}