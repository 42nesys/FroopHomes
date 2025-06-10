package insolventer.froopHomes.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import insolventer.froopHomes.FroopHomes;
import insolventer.froopHomes.util.ColorUtil;
import insolventer.froopHomes.util.SoundUtil;
import insolventer.froopHomes.util.TeleportUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@Getter
@NoArgsConstructor
public class HomeMenu {

    public static void openHomeMenu(Player player) {
        int maxHomes = 7;
        PaginatedGui gui = Gui.paginated()
                .title(ColorUtil.translate("Homes"))
                .rows(5)
                .pageSize(45)
                .disableAllInteractions()
                .create();

        for (int i = 1; i <= maxHomes; i++) {
            final int homeIndex = i;
            int slot = i + 18;
            if (getAmount(player) >= homeIndex) {
                if (FroopHomes.getInstance().getHomeUtil().get(player.getUniqueId(), homeIndex) != null) {
                    gui.setItem(slot, ItemBuilder.from(Material.RED_BED)
                            .name(ColorUtil.translate(ColorUtil.MAIN + "Home " + homeIndex))
                            .lore(ColorUtil.translate("&7Left-Click to teleport"), ColorUtil.translate("&7Right-Click to delete"))
                            .asGuiItem(event -> {
                                if (event.getClick() == ClickType.LEFT) {
                                    TeleportUtil.teleport(player, FroopHomes.getInstance().getHomeUtil().get(player.getUniqueId(), homeIndex));
                                } else if (event.getClick() == ClickType.RIGHT) {
                                    new DeleteMenu().openDeleteMenu(player, homeIndex);
                                }
                            }));
                } else {
                    gui.setItem(slot, ItemBuilder.from(Material.LIGHT_GRAY_BED)
                            .name(ColorUtil.translate(ColorUtil.MAIN + "Home " + homeIndex))
                            .lore(ColorUtil.translate("&7Click to set"))
                            .asGuiItem(event -> {
                                if(event.isLeftClick()) {
                                    FroopHomes.getInstance().getHomeUtil().set(player.getUniqueId(), homeIndex, player.getLocation());
                                    openHomeMenu(player);
                                    player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Home " + ColorUtil.MAIN + homeIndex + " has been set."));
                                    SoundUtil.playSound(player, "ENTITY_PLAYER_LEVELUP");

                                }
                            }));
                }
            } else {
                gui.setItem(slot, ItemBuilder.from(Material.BARRIER)
                        .name(ColorUtil.translate(ColorUtil.RED + "You can't use this Home!"))
                        .asGuiItem());
            }
        }

        gui.open(player);
        SoundUtil.playSound(player, "ENTITY_CHICKEN_EGG");
    }

    public static int getAmount(Player player) {
        if (player.hasPermission("homes.7")) {
            return 7;
        }
        if (player.hasPermission("homes.6")) {
            return 6;
        }
        if (player.hasPermission("homes.5")) {
            return 5;
        }
        if (player.hasPermission("homes.4")) {
            return 4;
        }
        if (player.hasPermission("homes.3")) {
            return 3;
        }
        if (player.hasPermission("homes.2")) {
            return 2;
        }
        return 1;
    }
}