package insolventer.froopHomes;

import insolventer.froopHomes.command.HomeCommand;
import insolventer.froopHomes.listener.PlayerJoinListener;
import insolventer.froopHomes.util.HomeUtil;
import insolventer.froopHomes.util.MySqlUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class FroopHomes extends JavaPlugin {
    @Getter
    private static FroopHomes instance;
    private MySqlUtil mySqlUtil;
    private HomeUtil homeUtil;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        instance = this;
        mySqlUtil = new MySqlUtil(getConfig().getString("Sql.Host"), getConfig().getString("Sql.Port"), getConfig().getString("Sql.Database"), getConfig().getString("Sql.Username"), getConfig().getString("Sql.Password"));
        homeUtil = new HomeUtil();

        mySqlUtil.update("CREATE TABLE IF NOT EXISTS Homes (UUID VARCHAR(100) NOT NULL, Home1 VARCHAR(100) NOT NULL DEFAULT '-', Home2 VARCHAR(100) NOT NULL DEFAULT '-', Home3 VARCHAR(100) NOT NULL DEFAULT '-', Home4 VARCHAR(100) NOT NULL DEFAULT '-', Home5 VARCHAR(100) NOT NULL DEFAULT '-', Home6 VARCHAR(100) NOT NULL DEFAULT '-', Home7 VARCHAR(100) NOT NULL DEFAULT '-');");

        new PlayerJoinListener();

        new HomeCommand("home");
        new HomeCommand("homes");

    }
}
