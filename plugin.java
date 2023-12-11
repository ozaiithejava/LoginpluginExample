import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class LoginGUIPlugin extends JavaPlugin implements Listener {

    private final Map<Player, Boolean> loggedInPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        openLoginGUI(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !loggedInPlayers.containsKey(player)) {
            return;
        }

        event.setCancelled(true);

        if (clickedItem.getType() == Material.PAPER) {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "Oyuna giriş yapmak için kullanıcı adı ve şifrenizi yazın.");
            player.sendMessage(ChatColor.YELLOW + "Doğru format: kullaniciAdi sifre");
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (loggedInPlayers.containsKey(player)) {
            player.sendMessage(ChatColor.RED + "Giriş yapılmadı. Sunucudan çıkış yapılıyor.");
            player.kickPlayer(ChatColor.RED + "Giriş yapılmadığı için sunucudan atıldınız.");
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!loggedInPlayers.containsKey(player)) {
            event.setCancelled(true);
            String message = event.getMessage();
            String[] parts = message.split(" ");

            if (parts.length == 2) {
                String username = parts[0];
                String password = parts[1];

                // Kullanıcı adı ve şifre kontrolü
                if (isValidLogin(username, password)) {
                    loggedInPlayers.put(player, true);
                    player.sendMessage(ChatColor.GREEN + "Giriş başarılı!");
                    player.sendMessage(ChatColor.GREEN + "Oyunun tadını çıkarın!");
                } else {
                    player.sendMessage(ChatColor.RED + "Giriş başarısız. Yanlış kullanıcı adı veya şifre.");
                    player.sendMessage(ChatColor.RED + "Doğru format: kullaniciAdi sifre");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Doğru format: kullaniciAdi sifre");
            }
        }
    }

    private boolean isValidLogin(String username, String password) {
        // Gerçek bir giriş doğrulama işlemi yapılabilir.
        // Bu örnek sadece kullanıcı adı ve şifreyi kontrol eder.
        return username.equals("demo") && password.equals("password");
    }

    private void openLoginGUI(Player player) {
        Inventory gui = Bukkit.createInventory(player, 9, ChatColor.BOLD + "Login");

        ItemStack infoItem = new ItemStack(Material.PAPER);
        gui.setItem(4, infoItem);

        player.openInventory(gui);
        player.sendMessage(ChatColor.YELLOW + "Oyuna giriş yapmak için kullanıcı adı ve şifrenizi yazın.");
        player.sendMessage(ChatColor.YELLOW + "Doğru format: kullaniciAdi sifre");
    }
}
