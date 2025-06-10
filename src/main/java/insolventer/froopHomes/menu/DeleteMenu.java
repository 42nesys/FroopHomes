package insolventer.froopHomes.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import insolventer.froopHomes.FroopHomes;
import insolventer.froopHomes.util.ColorUtil;
import insolventer.froopHomes.util.SoundUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
public class DeleteMenu {
    public void openDeleteMenu(Player player, int home) {
        PaginatedGui gui = Gui.paginated()
                .title(ColorUtil.translate(ColorUtil.RED + "Delete Home"))
                .rows(3)
                .disableAllInteractions()
                .create();

        gui.setItem(11, ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE).name(ColorUtil.translate(ColorUtil.RED + "Delete")).asGuiItem(event -> {
            player.closeInventory();
            FroopHomes.getInstance().getHomeUtil().delete(player.getUniqueId(), home);
            player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "You have deleted home " + ColorUtil.MAIN + home));
            SoundUtil.playSound(player, "ENTITY_PLAYER_LEVELUP");
            HomeMenu.openHomeMenu(player);
        }));

        gui.setItem(13, ItemBuilder.from(Material.RED_BED).name(ColorUtil.translate(ColorUtil.RED + "Home " + home)).asGuiItem());

        gui.setItem(15, ItemBuilder.from(Material.RED_STAINED_GLASS_PANE).name(ColorUtil.translate(ColorUtil.RED + "Cancel")).asGuiItem(event -> {
            player.closeInventory();
            HomeMenu.openHomeMenu(player);
        }));


        gui.open(player);
    }
}

