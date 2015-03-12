package me.jezza.fgpm.config;

import com.google.gson.JsonObject;

public interface IJsonObject {

    public void toJsonObject(JsonObject jsonObject);

    public void fromJsonObject(JsonObject jsonObject);

}