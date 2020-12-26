import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;


public class API {
    public static void main(String[] args) throws Exception {
        String baseURL = "https://addons-ecs.forgesvc.net/api/v2/";
        String searchURL = "addon/search?categoryId=%s&gameId=%s&pageSize=%s&searchFilter=%s";
        String categoryId = "0";
        String gameId = "432";
        String pageSize = "5";
        String infoURL = "addon/";
        System.out.println("Enter Modpack Name: ");
        Scanner scanner = new Scanner(System.in);
        String searchFilter = scanner.nextLine();
        JsonArray modpacklist = downloadJsonArray(baseURL+String.format(searchURL, categoryId, gameId, pageSize, URLEncoder.encode(searchFilter, "UTF-8")));
        String AddonId = getAddonId(modpacklist);
        JsonObject modpackInfo = downloadJsonObject(baseURL+infoURL+AddonId);
        String fileId = getFileId(modpackInfo);
        String downloadURL = "addon/%s/file/%s/download-url";
        FileUtils.copyURLToFile(new URL(downloadData(baseURL+String.format(downloadURL, AddonId, fileId))), new File(String.format("%s.zip",searchFilter)));
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
        String data = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        return data;
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
    public static String getFileId(JsonObject modpackInfo) {
        JsonArray latestFiles = modpackInfo.get("latestFiles").getAsJsonArray();
        if(latestFiles.size()==1) {
            return latestFiles.get(0).getAsJsonObject().get("serverPackFileId").getAsString();
        } else {
            System.out.println("Multiple versions were found, please select the correct one");
            for (int i = 0; i < latestFiles.size(); i++) {
                System.out.printf("Option %s: %s%n", i+1, latestFiles.get(i).getAsJsonObject().get("displayName").getAsString());
            }
            System.out.println("Enter option number of correct version");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            return latestFiles.get(Integer.parseInt(choice)-1).getAsJsonObject().get("serverPackFileId").getAsString();
        }
    }
}
