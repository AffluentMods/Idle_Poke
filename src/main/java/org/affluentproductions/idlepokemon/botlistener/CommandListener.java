package org.affluentproductions.idlepokemon.botlistener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.affluentproductions.idlepokemon.Constants;
import org.affluentproductions.idlepokemon.IdlePokemon;
import org.affluentproductions.idlepokemon.commands.BotCommand;
import org.affluentproductions.idlepokemon.commands.action.ClickCommand;
import org.affluentproductions.idlepokemon.commands.action.LevelCommand;
import org.affluentproductions.idlepokemon.commands.action.SkillCommand;
import org.affluentproductions.idlepokemon.commands.action.StageCommand;
import org.affluentproductions.idlepokemon.commands.admin.AdminCommand;
import org.affluentproductions.idlepokemon.commands.admin.DevCommand;
import org.affluentproductions.idlepokemon.commands.info.*;
import org.affluentproductions.idlepokemon.commands.shop.AncientCommand;
import org.affluentproductions.idlepokemon.commands.shop.ShopCommand;
import org.affluentproductions.idlepokemon.commands.shop.UpgradeCommand;
import org.affluentproductions.idlepokemon.commands.util.EvolveCommand;
import org.affluentproductions.idlepokemon.commands.util.PrefixCommand;
import org.affluentproductions.idlepokemon.commands.util.PrizeCommand;
import org.affluentproductions.idlepokemon.commands.util.ServerCommand;
import org.affluentproductions.idlepokemon.manager.BanManager;
import org.affluentproductions.idlepokemon.superclass.CommandEvent;
import org.affluentproductions.idlepokemon.util.CooldownUtil;
import org.affluentproductions.idlepokemon.util.MessageUtil;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandListener extends ListenerAdapter {

    private static Map<String, BotCommand> commands = new TreeMap<>();
    private static ExecutorService executorService = Executors.newWorkStealingPool();
    public final static List<String> blacklisted = new ArrayList<>();

    public static void load() {
        try (ResultSet rs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM blacklist;")) {
            while (rs.next()) blacklisted.add(rs.getString("guildId"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        /* LOAD COMMANDS */
        List<BotCommand> cmds = new ArrayList<>(
                Arrays.asList(new HelpCommand(), new InventoryCommand(), new AdminCommand(), new DevCommand(),
                        new PingCommand(), new ClickCommand(), new InfoCommand(), new InviteCommand(),
                        new LevelCommand(), new GuideCommand(), new VoteCommand(), new ShopCommand(),
                        new EvolveCommand(), new UpgradeCommand(), new SkillCommand(), new StatsCommand(),
                        new PrefixCommand(), new ServerCommand(), new BoostsCommand(), new TopCommand(),
                        new LevelTestCommand(), new StartCommand(), new StageCommand(), new AchievementsCommand(),
                        new AncientCommand()));
        if (PrizeCommand.enabled) cmds.add(new PrizeCommand());
        for (BotCommand a : cmds) {
            commands.put(a.getName().toLowerCase(), a);
            String[] aliases = a.getAliases();
            if (aliases.length > 0) for (String alias : aliases) commands.put(alias.toLowerCase(), a);
        }
    }

    private static final HashMap<String, Integer> spam = new HashMap<>();
    private static final int SPAM_TIMER = 3;
    private static final int MAX_SPAM = 5;
    private static long clearSpamAt = 0;

    public static boolean cmdlog = false;
    private static String selfPrefix = null;

    private static int commandsRunned = 0;

    private static String getPrefix(String uid) {
        try (ResultSet rs = IdlePokemon.getBot().getDatabase().query("SELECT * FROM userprefix WHERE userId=?;", uid)) {
            if (rs.next()) return rs.getString("prefix");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return IdlePokemon.test ? Constants.TEST_PREFIX : Constants.PREFIX;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        if (!IdlePokemon.fullyloaded || IdlePokemon.shuttingdown) return;
        if (blacklisted.contains(e.getGuild().getId())) return;
        User user = e.getAuthor();
        String uid = user.getId();
        boolean isBot = user.isBot() && !uid.equals(e.getJDA().getSelfUser().getId());
        if (isBot) return;
        // command and user checks
        String cmdprefix = getPrefix(uid);
        if (selfPrefix == null) selfPrefix = "<@!" + e.getJDA().getSelfUser().getId() + ">";
        if (e.getMessage().getContentRaw().startsWith(selfPrefix)) cmdprefix = selfPrefix;
        boolean isCommand = e.getMessage().getContentRaw().startsWith(cmdprefix);
        if (!isCommand) return;
        // command initialization
        String cmd = e.getMessage().getContentRaw().substring(cmdprefix.length()).split(" ")[0];
        if (!(commands.containsKey(cmd.toLowerCase()))) return;
        BotCommand bc = commands.get(cmd.toLowerCase());
        // botcommmand checks
        if (BanManager.isBanned(uid)) {
            return;
        }
        commandsRunned++;
        boolean isAuthorOwner =
                uid.equalsIgnoreCase("335051227324743682") || uid.equalsIgnoreCase("429307019229397002");
        if (bc.ownerCommand) {
            if (!isAuthorOwner) return;
        }
        long now = System.currentTimeMillis();
        if (clearSpamAt <= now) {
            spam.clear();
            clearSpamAt = now + (SPAM_TIMER * 1000);
        }
        int spamCount = spam.getOrDefault(uid, 0);
        spamCount++;
        if (spamCount > MAX_SPAM) {
            return;
        }
        spam.put(uid, spamCount);
        if (cmdlog) System.out.println(
                "[" + spamCount + "/" + MAX_SPAM + "] (" + commandsRunned + ") " + user.getAsTag() + " (" + uid +
                ") used " + e.getMessage().getContentRaw());
        TextChannel tc = e.getChannel();
        if (bc.getCooldown() > 0) {
            long cd = CooldownUtil.getCooldown(uid, "cmd_cd_" + bc.getName().toLowerCase());
            if (cd != -1L) {
                long diff = cd - now;
                if (diff > 0) {
                    tc.sendMessage(MessageUtil.err("Error",
                            "You are on cooldown!\nYou can use that again in " + CooldownUtil.format(diff, uid)))
                            .queue();
                    return;
                } else {
                    CooldownUtil.removeCooldown(uid, "cmd_cd_" + bc.getName().toLowerCase());
                }
            } else {
                double cd1 = bc.getCooldown();
                cd1 *= 1000;
                long cd3 = new Double(cd1).longValue();
                CooldownUtil.addCooldown(uid, "cmd_cd_" + bc.getName().toLowerCase(), (now + cd3), false);
            }
        }
        // command build
        Runnable runnable = () -> {
            try {
                boolean hasAllPermissions = true;
                List<Permission> needed =
                        Arrays.asList(Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
                                Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS);
                Member m = tc.getGuild().getSelfMember();
                List<Permission> allowed = new ArrayList<>(m.getPermissions(tc));
                if (!allowed.contains(Permission.ADMINISTRATOR)) {
                    for (Permission need : needed)
                        if (!allowed.contains(need)) {
                            hasAllPermissions = false;
                            break;
                        }
                }
                if (hasAllPermissions) {
                    if (IdlePokemon.maintenance && !isAuthorOwner) {
                        e.getChannel().sendMessage(
                                "**Idle Pokémon** is in __**maintenance**__:\n\n" + IdlePokemon.maintenance_message)
                                .queue();
                        return;
                    }
                    if (IdlePokemon.disabledModules.contains(bc.getName().toLowerCase())) {
                        e.getChannel().sendMessage(
                                MessageUtil.err("Error", "This command has been disabled!\nTry again later.")).queue();
                        return;
                    }
                    String[] args = Arrays.copyOfRange(e.getMessage().getContentRaw().split(" "), 1,
                            e.getMessage().getContentRaw().split(" ").length);
                    bc.execute(new CommandEvent(e, isAuthorOwner, args));
                } else {
                    user.openPrivateChannel().queue(pc -> {
                        pc.sendMessage(
                                "I can't chat in this text channel!\nIf you think this is an error, please tell " +
                                "an administrator to update my permissions!").queue();
                        final StringBuilder needPerms = new StringBuilder();
                        needed.forEach(permission -> needPerms.append(permission.getName()).append(", "));
                        String np = needPerms.toString();
                        if (np.endsWith(", ")) np = np.substring(0, np.length() - 2);
                        pc.sendMessage("I need: " + np).queue();
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        executorService.submit(runnable);
        if (commandsRunned >= 100) {
            commandsRunned = 0;
            executorService.shutdown();
            executorService = Executors.newWorkStealingPool();
        }
    }
}