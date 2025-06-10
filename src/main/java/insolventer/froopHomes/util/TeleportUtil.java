package insolventer.froopHomes.util;

import insolventer.froopHomes.FroopHomes;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class TeleportUtil {

    private static final ConcurrentHashMap<UUID, Location> move = new ConcurrentHashMap<>();

    public static void teleport(Player player, Location location) {
        player.closeInventory();

        if (player.hasPermission("froophomes.bypass")) {
            player.teleport(location);
            SoundUtil.playSound(player, "ENTITY_PLAYER_LEVELUP");
            return;
        }

        move.put(player.getUniqueId(), player.getLocation());

        final int[] seconds = {5};
        UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            @Override
            public void run() {
                seconds[0]--;

                if (Bukkit.getPlayer(uuid) == null) {
                    move.remove(player.getUniqueId());
                    this.cancel();
                    return;
                }

                Location moveLocation = move.get(player.getUniqueId());
                if (moveLocation != null && moveLocation.distance(player.getLocation()) > 0.3) {
                    player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + ColorUtil.RED + "Teleportation was canceled you moved!"));
                    player.showTitle(Title.title(
                            ColorUtil.translate(ColorUtil.RED + "Teleportation was canceled!"),
                            ColorUtil.translate(ColorUtil.RED + "you moved!")
                    ));
                    SoundUtil.playSound(player, "BLOCK_ANVIL_LAND");
                    move.remove(player.getUniqueId());
                    this.cancel();
                    return;
                }

                switch (seconds[0]) {
                    case 5, 4, 3, 2, 1 -> {
                        player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "You will be teleported in " + ColorUtil.MAIN + seconds[0] + "§7 seconds!"));
                        player.sendActionBar(ColorUtil.translate(ColorUtil.MAIN + seconds[0]));
                        SoundUtil.playSound(player, "BLOCK_NOTE_BLOCK_PLING");
                    }
                    case 0 -> {
                        player.teleport(location);
                        SoundUtil.playSound(player, "ENTITY_PLAYER_LEVELUP");
                        move.remove(player.getUniqueId());
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(FroopHomes.getInstance(), 0, 20);
    }
}