package insolventer.froopHomes.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@UtilityClass
public class SoundUtil {

    public static void playSound(Player player, String key) {
        if (!key.equalsIgnoreCase("")) {
            try {
                player.playSound(player.getLocation(), Sound.valueOf(key), 5, 5);
            } catch (Exception ignored) {
            }
        }
    }
}
