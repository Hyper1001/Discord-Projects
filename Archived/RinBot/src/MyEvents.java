
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEditEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class MyEvents {

    //Channels
    public static final long messageLogs = 470937899492311050L;
    public static final long userLogs = 470937933080166429L;
    public static final long rulesInfo = 175171877281464320L;
    public static final long beginnings = 175171737397231616L;
    public static final long testChannel = 413094101177860106L;

    public static int chances = 400;
    public static int choosenNumber = 88;
    public static String word = "nya";
    //dev
//    public static final long messageLogs = 337538764211486720L;
//    public static final long userLogs = 337538764211486720L;
//    public static final long rulesInfo = 337538764211486720L;
//    public static final long beginnings = 337538764211486720L;
//    

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        int number = new Random().ints(0, chances).limit(1).sum();
//        System.out.println("number "+number);
        if (number == choosenNumber) {
            BotUtils.sendMessage(event.getChannel(), word);
        }

        if (event.getChannel().getLongID() == testChannel) {
            if (event.getMessage().getContent().equals("!stats")) {
                BotUtils.sendMessage(event.getChannel(), "Chances: 1/" + chances + "\nChoosen Number for chances: " + choosenNumber + "\nWord: " + word);
            }
        }
    }

    @EventSubscriber
    public void messageDelete(MessageDeleteEvent event) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName(event.getAuthor().getName() + "'s message has been deleted in #" + event.getChannel().getName() + ".");
        builder.withThumbnail(event.getAuthor().getAvatarURL());
        builder.withColor(255, 0, 0);
        builder.withDescription(formatter.format(date));
        builder.appendField("Deleted Message", event.getMessage().getContent(), false);
        event.getGuild().getChannelByID(messageLogs).sendMessage(builder.build());
    }

    @EventSubscriber
    public void messageEdit(MessageEditEvent event) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName(event.getAuthor().getName() + " has edited their message in #" + event.getChannel().getName() + ".");
        builder.withThumbnail(event.getAuthor().getAvatarURL());
        builder.withColor(255, 255, 0);
        builder.withDescription(formatter.format(date));
        builder.appendField("Old Message", event.getOldMessage().getContent(), false);
        builder.appendField("New Message", event.getNewMessage().getContent(), false);
        event.getGuild().getChannelByID(messageLogs).sendMessage(builder.build());
    }

    @EventSubscriber
    public void userJoin(UserJoinEvent event) {

        BotUtils.sendMessage(event.getGuild().getChannelByID(beginnings), "Welcome " + event.getUser().getName() + "#" + event.getUser().getDiscriminator() + " to The Cutest Idol! Introduce yourself to gain access to the server! Make sure to read the " + event.getGuild().getChannelByID(rulesInfo) + " as well!");
        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        builder.withAuthorName(event.getUser().getName() + " has joined the server.");
        builder.withThumbnail(event.getUser().getAvatarURL());
        builder.withColor(64, 252, 5);
        builder.withDescription(formatter.format(date));
        event.getGuild().getChannelByID(userLogs).sendMessage(builder.build());
    }

    @EventSubscriber
    public void userLeave(UserLeaveEvent event) {

        BotUtils.sendMessage(event.getGuild().getChannelByID(beginnings), "User " + event.getUser().getName() + "#" + event.getUser().getDiscriminator() + " has left");

        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        builder.withAuthorName(event.getUser().getName() + " has left the server.");

        builder.withThumbnail(event.getUser().getAvatarURL());
        builder.withColor(255, 255, 0);
        builder.withDescription(formatter.format(date));
        event.getGuild().getChannelByID(userLogs).sendMessage(builder.build());
    }

    @EventSubscriber
    public void userBan(UserBanEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        builder.withAuthorName(event.getUser().getName() + " has been banned from the server.");
        builder.withThumbnail(event.getUser().getAvatarURL());
        builder.withColor(255, 0, 0);
        builder.withDescription(formatter.format(date));
        event.getGuild().getChannelByID(userLogs).sendMessage(builder.build());
    }
}
