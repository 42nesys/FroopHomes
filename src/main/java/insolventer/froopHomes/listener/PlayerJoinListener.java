package insolventer.froopHomes.listener;

import insolventer.froopHomes.FroopHomes;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    public PlayerJoinListener() {
        Bukkit.getPluginManager().registerEvents(this, FroopHomes.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        var player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(FroopHomes.getInstance(), () -> {
            if (!(FroopHomes.getInstance().getMySqlUtil().get("Homes", "UUID", "UUID='" + player.getUniqueId() + "'").equalsIgnoreCase(player.getUniqueId().toString()))) {
                FroopHomes.getInstance().getMySqlUtil().update("INSERT INTO Homes (UUID) VALUES ('" + player.getUniqueId() + "')");
            }
            FroopHomes.getInstance().getHomeUtil().loadLocal(player.getUniqueId());
        });
    }
}
