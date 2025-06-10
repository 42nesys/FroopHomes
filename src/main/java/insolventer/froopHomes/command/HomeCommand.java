package insolventer.froopHomes.command;

import insolventer.froopHomes.FroopHomes;
import insolventer.froopHomes.menu.HomeMenu;
import insolventer.froopHomes.util.ColorUtil;
import insolventer.froopHomes.util.SoundUtil;
import insolventer.froopHomes.util.TeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HomeCommand implements CommandExecutor, TabCompleter {

    public HomeCommand(String name) {
        Objects.requireNonNull(FroopHomes.getInstance().getCommand(name)).setExecutor(this);
        Objects.requireNonNull(FroopHomes.getInstance().getCommand(name)).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return false;

        if (args.length == 0) {
            HomeMenu.openHomeMenu(player);
            return false;
        }

        if (args.length == 1) {
            try {
                int home = Integer.parseInt(args[0]);
                if (home >= 1 && home <= 7) {
                    if (FroopHomes.getInstance().getHomeUtil().get(player.getUniqueId(), home) != null) {
                        TeleportUtil.teleport(player, FroopHomes.getInstance().getHomeUtil().get(player.getUniqueId(), home));
                        return false;
                    }

                    player.sendMessage(ColorUtil.translate(ColorUtil.translate(ColorUtil.PREFIX) + "Home has not been set yet!"));
                    return false;
                }
            } catch (NumberFormatException ignored) { }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                try {
                    int home = Integer.parseInt(args[1]);
                    if (home >= 1 && home <= 7) {
                        if (HomeMenu.getAmount(player) < home) {
                            SoundUtil.playSound(player, "BLOCK_ANVIL_LAND");
                            player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "You dont have permission to use this home!"));
                            return false;
                        }

                        FroopHomes.getInstance().getHomeUtil().set(player.getUniqueId(), home, player.getLocation());
                        player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Home was set successfully!"));
                        return false;
                    }
                } catch (NumberFormatException ignored) { }
            }

            if (args[0].equalsIgnoreCase("delete")) {
                try {
                    int home = Integer.parseInt(args[1]);
                    if (FroopHomes.getInstance().getHomeUtil().get(player.getUniqueId(), home) != null) {
                        FroopHomes.getInstance().getHomeUtil().delete(player.getUniqueId(), home);
                        player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Home was deleted successfully!"));
                        return false;
                    }

                    player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Home has not been set yet!"));
                    return false;
                } catch (NumberFormatException ignored) { }
            }
        }

        if (player.hasPermission("froophomes.admin") && (args.length == 2 || args.length == 3)) {
            UUID target = Bukkit.getPlayerUniqueId(args[0]);
            if (target == null) {
                player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "§cPlayer was not found!"));
                return false;
            }

            if (args.length == 2) {
                try {
                    int home = Integer.parseInt(args[1]);
                    if (home >= 1 && home <= 7) {
                        if (FroopHomes.getInstance().getHomeUtil().get(player.getUniqueId(), home) != null) {
                            player.teleport(FroopHomes.getInstance().getHomeUtil().get(target, home));
                            return false;
                        }

                        player.sendMessage(ColorUtil.translate(ColorUtil.translate(ColorUtil.PREFIX) + "Home has not been set yet!"));
                        return false;
                    }
                } catch (NumberFormatException ignored) { }
            }

            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("set")) {
                    try {
                        int home = Integer.parseInt(args[2]);
                        if (home >= 1 && home <= 7) {
                            FroopHomes.getInstance().getHomeUtil().set(target, home, player.getLocation());
                            player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Home was set successfully!"));
                            return false;
                        }
                    } catch (NumberFormatException ignored) { }
                }

                if (args[1].equalsIgnoreCase("delete")) {
                    try {
                        int home = Integer.parseInt(args[2]);
                        if (FroopHomes.getInstance().getHomeUtil().get(target, home) != null) {
                            FroopHomes.getInstance().getHomeUtil().delete(target, home);
                            player.sendMessage(ColorUtil.translate(ColorUtil.translate(ColorUtil.PREFIX) + "Home was deleted successfully!"));
                            return false;
                        }

                        player.sendMessage(ColorUtil.translate(ColorUtil.translate(ColorUtil.PREFIX) + "Home has not been set yet!"));
                        return false;
                    } catch (NumberFormatException ignored) { }
                }
            }
        }

        player.sendMessage(" ");
        player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Usage: §c/home"));
        player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Usage: §c/home <1-" + HomeMenu.getAmount(player) + ">"));
        player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Usage: §c/home set <1-" + HomeMenu.getAmount(player) + ">"));
        player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Usage: §c/home delete <1-" + HomeMenu.getAmount(player) + ">"));
        if (player.hasPermission("froophomes.admin")) {
            player.sendMessage(" ");
            player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Usage: §c/home <player> <1-7>"));
            player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Usage: §c/home <player> set <1-7>"));
            player.sendMessage(ColorUtil.translate(ColorUtil.PREFIX + "Usage: §c/home <player> delete <1-7>"));
        }
        player.sendMessage(" ");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return new ArrayList<>();

        List<String> list = new ArrayList<>();

        if (args.length == 3) {
            UUID target = Bukkit.getPlayerUniqueId(args[0]);
            if (target != null) {
                if (args[1].equalsIgnoreCase("set")) {
                    for (int i = 1; i <= 7; i++) {
                        list.add(i + "");
                    }
                } else if (args[1].equalsIgnoreCase("delete")) {
                    for (int i = 1; i <= 7; i++) {
                        if (FroopHomes.getInstance().getHomeUtil().get(target, i) != null) {
                            list.add(i + "");
                        }
                    }
                }
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                for (int i = 1; i <= HomeMenu.getAmount(player); i++) {
                    list.add(i + "");
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                for (int i = 1; i <= HomeMenu.getAmount(player); i++) {
                    if (FroopHomes.getInstance().getHomeUtil().get(player.getUniqueId(), i) != null) {
                        list.add(i + "");
                    }
                }
            } else if (player.hasPermission("froophomes.admin")) {
                list.add("set");
                list.add("delete");
            }
        }

        if (args.length == 1) {
            list.add("set");
            list.add("delete");

            if (player.hasPermission("froophomes.admin")) {
                list.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
            }

            for (int i = 1; i <= HomeMenu.getAmount(player); i++) {
                if (FroopHomes.getInstance().getHomeUtil().get(player.getUniqueId(), i) != null) {
                    list.add(i + "");
                }
            }
        }

        return list.stream().filter(content -> content.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).sorted().toList();
    }
}