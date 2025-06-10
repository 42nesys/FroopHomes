package insolventer.froopHomes.util;

import insolventer.froopHomes.FroopHomes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeUtil {

    private final Map<String, String> cache = new HashMap<>();

    public HomeUtil() {
        try (ResultSet rs = FroopHomes.getInstance().getMySqlUtil().getResult("SELECT * FROM Homes")) {
            if (rs != null) {
                while (rs.next()) {
                    String uuid = rs.getString("UUID");
                    for (int i = 1; i <= 7; i++) {
                        cache.put(uuid + "-" + i, rs.getString("Home" + i));
                    }
                }
            }
        } catch (SQLException ignored) {
        }
    }

    public void loadLocal(UUID uuid) {
        try (ResultSet rs = FroopHomes.getInstance().getMySqlUtil().getResult("SELECT * FROM Homes WHERE UUID='" + uuid + "'")) {
            if (rs != null) {
                while (rs.next()) {
                    for (int i = 1; i <= 7; i++) {
                        cache.put(uuid + "-" + i, rs.getString("Home" + i));
                    }
                }
            }
        } catch (SQLException ignored) {
        }
    }

    public void set(UUID uuid, int number, Location location) {
        String value = locToString(location);
        FroopHomes.getInstance().getMySqlUtil().update(
                "UPDATE Homes SET Home" + number + "='" + value + "' WHERE UUID='" + uuid + "'");
        cache.put(uuid + "-" + number, value);
    }

    public void delete(UUID uuid, int number) {
        FroopHomes.getInstance().getMySqlUtil().update(
                "UPDATE Homes SET Home" + number + "='-' WHERE UUID='" + uuid + "'");
        cache.put(uuid + "-" + number, "-");
    }

    public Location get(UUID uuid, int number) {
        String key = uuid + "-" + number;
        String value = cache.get(key);
        if (value == null) {
            value = FroopHomes.getInstance().getMySqlUtil().get(
                    "Homes", "Home" + number, "UUID='" + uuid + "'");
            if (value != null) cache.put(key, value);
        }
        if (value == null || value.equals("-")) return null;
        return stringToLoc(value);
    }

    private String locToString(Location loc) {
        return loc.getWorld().getName() + "/" + (loc.getBlockX() + 0.5) + "/" + loc.getBlockY() + "/" + (loc.getBlockZ() + 0.5) + "/" + (Math.round(loc.getYaw() / 45) * 45);
    }

    private Location stringToLoc(String s) {
        String[] p = s.split("/");
        if (p.length < 5) return null;
        return new Location(
                Bukkit.getWorld(p[0]),
                Double.parseDouble(p[1]),
                Double.parseDouble(p[2]),
                Double.parseDouble(p[3]),
                (float) Double.parseDouble(p[4]),
                0f
        );
    }
}
