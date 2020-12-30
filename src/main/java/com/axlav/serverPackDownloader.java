package com.axlav;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.net.URLEncoder;
import java.util.Scanner;

import static com.axlav.APIcalls.*;

public class serverPackDownloader {
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
        JsonArray modpacklist = searchModpacks(baseURL+String.format(searchURL, categoryId, gameId, pageSize, URLEncoder.encode(searchFilter, "UTF-8")));
        String AddonId = getAddonId(modpacklist);
        JsonObject modpackInfo = getModpackInfo(baseURL+infoURL+AddonId);
        String fileId = getFileId(modpackInfo);
        String downloadURL = "addon/%s/file/%s/download-url";
        FileUtils.copyURLToFile(getUrlFromUrl(baseURL+String.format(downloadURL, AddonId, fileId)), new File(String.format("%s.zip",searchFilter)));
    }
}
