package io.github.yehan2002.Traps.Util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unused", "WeakerAccess"})
public class AutoUpdater implements Listener {

    private final Pattern alphaCheck = Pattern.compile("(?<=(alpha))\\d+");
    private Pattern betaCheck = Pattern.compile("(?<=(beta))\\d+");
    private Pattern snapshotCheck = Pattern.compile("(?<=(snapshot))\\d+");

    private JavaPlugin plugin;
    private String updateUrl;
    private String id;
    private String perm;
    private config updaterConfig;
    private globalConfig globalConfig;
    private String globalPerm;
    private Integer delay;
    private boolean checkUpdates;


    private boolean initialized = false;


    /*

    */
    public AutoUpdater(String id, JavaPlugin plugin, String perm){
        try {
            this.id = Objects.requireNonNull(id, "Plugin id must not be null");
            this.plugin = Objects.requireNonNull(plugin, "Plugin must not be null");
            this.perm = Objects.requireNonNull(perm, "permission must not be null");


            try {

                updaterConfig = new config();
                globalConfig = new globalConfig();

            } catch (IOException | InvalidConfigurationException e) {

                e.printStackTrace();
                logError("An Error occurred while loading config files.");
                LogException(e);
                return;

            }


            if (updaterConfig.getBoolean("autoUpdater.nagAdmins") && globalConfig.getBoolean("autoUpdater.nagAdmins")) {

                plugin.getServer().getPluginManager().registerEvents(this, plugin);

            }

            globalPerm = Objects.requireNonNull(globalConfig.getString("autoUpdater.nagPerm"), "Invalid global permission in " + globalConfig.file.getName());

            this.delay = updaterConfig.yml.getInt("autoUpdater.check");
            this.checkUpdates =



            initialized = true;

        } catch (NullPointerException e){
            System.out.println("Error while enabling autoUpdater: ");
            e.printStackTrace();
        }
    }

    public AutoUpdater(String id, JavaPlugin plugin) {
        this(id, plugin, "autoUpdater.notify");
    }

    public  void Update() {
        if (!initialized){
            return;
        }
        if (updaterConfig.getBoolean("autoUpdater.enabled") && globalConfig.getBoolean("autoUpdater.enabled")) {

            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::updater, 1);

        }
    }

    private void updater(){
        if (NewerVersionIsAvailable()) {
            this.logMessage("messages.status.newConsole");
            if (updaterConfig.getBoolean("autoUpdater.autoDownload") && globalConfig.getBoolean("autoUpdater.autoDownload")) {
                this.logMessage("messages.download.start");
                this.downloadLatest();
            } else {
                updateUrl = getReleaseURL();
                this.logMessage("messages.status.url", updateUrl);
                this.nagAll();
            }
        } else {
            this.logMessage("messages.status.latest");

        }

        if (checkUpdates && plugin.isEnabled() && delay > 0){
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::updater, 72000 * delay);
        }

    }

    private  boolean NewerVersionIsAvailable() {
        try {


            return getVerID(getLatestVersion()) > getVerID(plugin.getDescription().getVersion());
        } catch (NullPointerException e){
            return false;
        }
    }

    private File getJarPath(){
        return new File(AutoUpdater.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    private void reloadPlugin(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager instanceof SimplePluginManager) {
            if (plugin.isEnabled()){
                pluginManager.disablePlugin(plugin);
            }
            try {
                pluginManager.enablePlugin(pluginManager.loadPlugin(getJarPath()));
            } catch (InvalidPluginException | InvalidDescriptionException e) {
                e.printStackTrace();
            }
        }
    }

    private  Integer getVerID(String v){
        v = v.toLowerCase();
        String id;


        if (v.contains("alpha")){

            Matcher m = alphaCheck.matcher(v);
            if (m.find() && !Objects.equals(m.group(), "")){
                id = "01" + String.format("%02d", Integer.parseInt(m.group()));
            } else {
                id = "0100";
            }

            v = v.split("alpha")[0];

        } else if (v.contains("beta")){

            Matcher m = betaCheck.matcher(v);
            if (m.find() && !Objects.equals(m.group(), "")){
                id = "02" + String.format("%02d", Integer.parseInt(m.group()));
            } else {
                id = "0200";
            }

            v = v.split("beta")[0];


        }else if (v.contains("snapshot")){

            Matcher m = snapshotCheck.matcher(v);
            if (m.find() && !Objects.equals(m.group(), "")){
                id = "03" + String.format("%02d", Integer.parseInt(m.group()));
            } else {
                id = "0300";
            }

            v = v.split("snapshot")[0];


        } else {
            id = "0400";
        }

        v = v.replaceAll("[^\\d.]", "");

        
        StringBuilder floatOf = new StringBuilder();

        for (String s : v.split("\\.")) {
            floatOf.append(String.format("%02d", Integer.parseInt(s)));
        }

        v = String.format("%-6s", floatOf).replace(' ', '0') + id;


        return Integer.parseInt(v);
    }

    private  String getLatestVersion(){
        String url = "https://api.spiget.org/v2/resources/" + id +"/versions?size=1&sort=-releaseDate&fields=name";
        Object jsonData = getJSON(url);
        if (jsonData ==null){
            return null;
        }
        JSONArray value = (JSONArray) jsonData;

        return (String) ((JSONObject)value.get(0)).get("name");

    }

    private String getReleaseURL(){
        String url = "https://api.spiget.org/v2/resources/" + id +"?fields=file";
        Object jsonData = getJSON(url);
        if (jsonData ==null){
            return null;
        }

        return ("https://www.spigotmc.org/" + ((JSONObject)((JSONObject) jsonData).get("file")).get("url")).split("download\\?")[0];
    }

    private void downloadLatest() {
        String url = "https://api.spiget.org/v2/resources/" + id +"/download";
        try {
            File tmp = File.createTempFile("pluginUpdate", ".jar");
            tmp.delete();
            Files.copy(
                    new URL(url).openStream(),
                    tmp.toPath());
            this.logMessage("messages.download.", plugin.getName());
            tmp.renameTo(getJarPath());
            getJarPath().renameTo(new File(getJarPath().toString() + ".old"));
            this.logMessage("messages.download.reload");
            this.reloadPlugin();
            this.logMessage("messages.download.done");

        } catch (MalformedURLException e) {
            this.logError("Error: Malformed URL.");
            this.LogException(e);
        } catch (UnknownHostException e) {
            this.logError("Error: Unable to connect.");
            this.LogException(e);
        } catch (SocketTimeoutException e) {
            this.logError("Error: connection timed out.");
            this.LogException(e);
        } catch (FileNotFoundException e){
            this.logError("Error: File not Found.");
            this.LogException(e);
        } catch (IOException e) {
            this.logError("Error: An unknown error occurred see log file for details. ");
            this.LogException(e);
        }

    }

    private void nagAll(){

        TextComponent message = new TextComponent(String.format(globalConfig.getString("messages.status.new"), plugin.getName()));
        message.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, updateUrl));

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.isOp() || player.hasPermission(perm) || player.hasPermission(globalPerm)) {
               player.spigot().sendMessage(message);
            }
        }
    }


    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        if (updateUrl != null) {
            if (e.getPlayer().isOp() || e.getPlayer().hasPermission(perm) || e.getPlayer().hasPermission(globalPerm)) {

                TextComponent message = new TextComponent(String.format(globalConfig.getString("messages.status.new"), plugin.getName()));
                message.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, updateUrl));
                e.getPlayer().spigot().sendMessage(message);
            }
        }
    }

    /*Helper functions*/

    private void logError(String s){
        plugin.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "][AutoUpdater] " + ChatColor.RED + s);

    }

    private void logMessage(String s){
        String message = Objects.requireNonNull(globalConfig.getString(s), "Invalid config value for " + s + " in " + globalConfig.file.getName());
        plugin.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "][AutoUpdater] " + ChatColor.GREEN + message);

    }

    private void logMessage(String s, String data){
        String message = Objects.requireNonNull(globalConfig.getString(s), "Invalid config value for " + s + " in " + globalConfig.file.getName());
        message = String.format(message, data);
        plugin.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "][AutoUpdater] " + ChatColor.GREEN + message);

    }

    private void LogException(Exception e){
        try {
        File file = Paths.get(plugin.getDataFolder().getParent(), "Updater", "logs", plugin.getName() + ".log").toFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();

        } else {
            file.delete();
            file.createNewFile();
        }
        PrintWriter p = new PrintWriter(file);
        e.printStackTrace(p);
        p.close();
        logError("log: " + file.toString().replace(plugin.getDataFolder().toString(), ""));

        } catch (IOException e1) {
            logError("Error while writing exception to file:");
            e1.printStackTrace();
        }

    }

    private Object getJSON(String u){
        try {
            URL url = new URL(u);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", "SpigotUpdater");

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            return JSONValue.parseWithException(reader);


        } catch (MalformedURLException e) {
            this.logError("Error: Malformed URL.");
            this.LogException(e);
        } catch (ParseException e) {
            this.logError("Error: Failed to parse response.");
            this.LogException(e);
        } catch (UnknownHostException e) {
            this.logError("Error: Unable to connect.");
            this.LogException(e);
        } catch (SocketTimeoutException e){
            this.logError("Error: connection timed out.");
            this.LogException(e);
        } catch (IOException e) {
            this.logError("Error: An unknown error occurred see log file for details. ");
            this.LogException(e);

        }
        return null;
    }

    /* Config2 files*/
    private class config{
        private File file = new File(plugin.getDataFolder(),"updater.yml");
        private YamlConfiguration yml;

        private config() throws IOException, InvalidConfigurationException {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();

                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

                writer.write("# This is the config for the auto updater.");
                writer.newLine();
                writer.write("autoUpdater:");
                writer.newLine();
                writer.write("  # if the auto updater should be enabled");
                writer.newLine();
                writer.write("  enabled: true");
                writer.newLine();
                writer.write("  # if the auto updater should inform admins that a new version is available");
                writer.newLine();
                writer.write("  nagAdmins: true");
                writer.newLine();
                writer.write("  # if the new version should be downloaded automatically");
                writer.newLine();
                writer.write("  autoUpdate: false");
                writer.newLine();
                writer.write("  # if updates checks should occur every n-minutes");
                writer.newLine();
                writer.write("  autoCheck: true");
                writer.newLine();
                writer.write("  # the delay between update checks");
                writer.newLine();
                writer.write("  check: 60");
                writer.newLine();
                writer.close();
            }

            yml = new YamlConfiguration();
            yml.load(file);
        }

        private boolean getBoolean(String path){
            return yml.getBoolean(path);
        }

    }

    private class globalConfig{
        private File file = Paths.get(plugin.getDataFolder().getParent(), "Updater","autoUpdater.yml").toFile();
        private YamlConfiguration yml;
        @SuppressWarnings("ResultOfMethodCallIgnored")
        private globalConfig() throws IOException, InvalidConfigurationException {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();

                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.write("# This is the global config for the auto updater.");
                writer.newLine();
                writer.write("# any option disabled here will be disabled globally");
                writer.newLine();
                writer.write("autoUpdater:");
                writer.newLine();
                writer.write("  # if the auto updater should be enabled");
                writer.newLine();
                writer.write("  enabled: true");
                writer.newLine();
                writer.write("  # if the auto updater should inform admins that a new version is available");
                writer.newLine();
                writer.write("  nagAdmins: true");
                writer.newLine();
                writer.write("  # the global permission the player must have to be notified about the update to all plugins");
                writer.newLine();
                writer.write("  nagPerm: \"autoUpdater.notify\"");
                writer.newLine();
                writer.write("  # if the new version should be downloaded automatically");
                writer.newLine();
                writer.write("  autoUpdate: true");
                writer.newLine();
                writer.write("  # if updates checks should occur every n-minutes");
                writer.newLine();
                writer.write("  autoCheck: true");
                writer.newLine();
                writer.write("# messages used by auto updater");
                writer.newLine();
                writer.write("messages:");
                writer.newLine();
                writer.write("  status:");
                writer.newLine();
                writer.write("    new: \"A new version of %s is available.\"");
                writer.newLine();
                writer.write("    newConsole: \"A newer version is available\"");
                writer.newLine();
                writer.write("    latest: \"You are using the latest version!\"");
                writer.newLine();
                writer.write("    url: \"%s\"");
                writer.newLine();
                writer.write("  download:");
                writer.newLine();
                writer.write("    start: \"Downloading newest version of %s...\"");
                writer.newLine();
                writer.write("    end: \"Downloading complete.\"");
                writer.newLine();
                writer.write("    reload: \"reloading plugin...\"");
                writer.newLine();
                writer.write("    done: \"Successfully updated plugin.\"");
                writer.newLine();
                writer.close();
            }

            yml = new YamlConfiguration();
            yml.load(file);
        }

        private boolean getBoolean(String path){
            return yml.getBoolean(path);
        }

        private String getString(String path){
            return yml.getString(path);
        }
    }


}
