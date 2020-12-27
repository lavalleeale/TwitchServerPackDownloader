package com.axlav;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URL;
import java.util.Scanner;

public class APIcalls {
    public static JsonArray searchModpacks(String url)  throws Exception{
        return downloadJsonArray(url);
    }

    public static JsonObject getModpackInfo(String url) throws Exception{
        return downloadJsonObject(url);
    }
    public static String getFileId(JsonObject modpackInfo) {
        JsonArray latestFiles = modpackInfo.get("latestFiles").getAsJsonArray();
        if(latestFiles.size()==1) {
            try{
                return latestFiles.get(0).getAsJsonObject().get("serverPackFileId").getAsString();
            }
            catch(UnsupportedOperationException e){
                throw new RuntimeException("This modpack does not provide server files");
            }
        } else {
            System.out.println("Multiple versions were found, please select the correct one");
            for (int i = 0; i < latestFiles.size(); i++) {
                System.out.printf("Option %s: %s%n", i+1, latestFiles.get(i).getAsJsonObject().get("displayName").getAsString());
            }
            System.out.println("Enter option number of correct version");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            try {
                return latestFiles.get(Integer.parseInt(choice) - 1).getAsJsonObject().get("serverPackFileId").getAsString();
            }
            catch(UnsupportedOperationException e) {
                throw new RuntimeException("This version does not provide server files");
            }
        }
    }
    public static String getAddonId(JsonArray modpacklist) {
        if(modpacklist.size()!=1) {
            System.out.println("Multiple matching modpacks were found, please select the correct one");
            for (int i = 0; i < modpacklist.size(); i++) {
                System.out.printf("Option %s: %s%n", i+1, modpacklist.get(i).getAsJsonObject().get("name").getAsString());
            }
            System.out.println("Enter option number of correct version");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            return modpacklist.get(Integer.parseInt(choice)-1).getAsJsonObject().get("id").getAsString();
        } else {
            JsonObject modpackInfo = modpacklist.get(0).getAsJsonObject();
            String chosenId = modpackInfo.get("id").getAsString();
            String chosenName = modpackInfo.get("name").getAsString();
            System.out.printf("You have chosen %s%n", chosenName);
            return chosenId;
        }
    }
    public static URL getUrlFromUrl(String url) throws Exception{
        return new URL(downloadData(url));
    }
    public static JsonArray downloadJsonArray(String url) throws Exception {
        Gson gson = new Gson();
        String json = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        return gson.fromJson(json, JsonArray.class);
    }
    public static JsonObject downloadJsonObject(String url) throws Exception {
        Gson gson = new Gson();
        String json = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        return gson.fromJson(json, JsonObject.class);
    }
    public static String downloadData(String url) throws Exception {
        return new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
    }
}