package io.github.yehan2002.Traps.Util.Serialization;

import io.github.yehan2002.Traps.Main;
import io.github.yehan2002.Traps.api.Trap;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;

public class serialization implements Serializable{
    private static final long serialVersionUID = -6548408158368692448L;
    private static String path;

    static {
        path = Paths.get(Main.get().getDataFolder().getAbsolutePath(), "data.db").toString();
    }

    public static void Serialize(HashMap<org.bukkit.Location, Trap> hashMap){
        HashMap<location, Trap> data = new HashMap<>();

        for (org.bukkit.Location location : hashMap.keySet()) {
            data.put(new location(location),hashMap.get(location));
        }

        try {
            write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static HashMap<org.bukkit.Location, Trap> Deserialize(){
        HashMap<location,Trap> data;
        HashMap<org.bukkit.Location, Trap> traps = new HashMap<>();
        try {
            data = read();
        } catch (FileNotFoundException e){
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }

        if (data == null) return new HashMap<>();
        for (location sLocation : data.keySet()) {
            try {
                traps.put(sLocation.getLocation(), data.get(sLocation));
            } catch (IllegalStateException e){
                Main.get().getLogger().severe("Skipped " + data.get(sLocation).trap + " in missing world " + e.getMessage());
            }
        }

        return traps;
    }

    private static void write(HashMap<location, Trap> trap) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(trap);
            objectOutputStream.flush();
        }
    }

    private static HashMap<location, Trap> read() throws IOException, ClassNotFoundException {
        Object data;
        try (FileInputStream fileInputStream = new FileInputStream(path); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            data = objectInputStream.readObject();
        }

        if (data != null){
            //noinspection unchecked
            return ((HashMap<location, Trap>) data);
        }

        return null;
    }
}

