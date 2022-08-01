import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEditEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyEvents
{

    @EventSubscriber
    public void messageDelete(MessageDeleteEvent event)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName(event.getAuthor().getName()+"'s message has been deleted in #"+event.getChannel().getName()+".");
        builder.withThumbnail(event.getAuthor().getAvatarURL());
        builder.withColor(255, 0, 0);
        builder.withDescription(formatter.format(date));
        builder.appendField("Deleted Message", event.getMessage().getContent()  , false);
        event.getGuild().getChannelByID(439402645489451008L).sendMessage(builder.build());    }
    @EventSubscriber
    public void messageEdit(MessageEditEvent event)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName(event.getAuthor().getName()+" has edited their message in #"+event.getChannel().getName()+".");
        builder.withThumbnail(event.getAuthor().getAvatarURL());
        builder.withColor(255, 255, 0);
        builder.withDescription(formatter.format(date));
        builder.appendField("Old Message", event.getOldMessage().getContent(), false);
        builder.appendField("New Message", event.getNewMessage().getContent(), false);
        event.getGuild().getChannelByID(439402645489451008L).sendMessage(builder.build());
    }
    @EventSubscriber
    public void userJoin(UserJoinEvent event)
    {
        BotUtils.sendMessage(event.getGuild().getChannelByID(351789748915929099L),"Welcome "+event.getUser()+" to Mikanland! Please read the "+event.getGuild().getChannelByID(387684786015174677L)+" and introduce yourself in this channel so you can access the rest of the server~ Have fun!!");
        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        builder.withAuthorName(event.getUser().getName()+" has joined the server.");
        builder.withThumbnail(event.getUser().getAvatarURL());
        builder.withColor(64, 252, 5);
        builder.withDescription(formatter.format(date));
        event.getGuild().getChannelByID(439402726099517471L).sendMessage(builder.build());
    }
    @EventSubscriber
    public void userLeave(UserLeaveEvent event)
    {
        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        builder.withAuthorName(event.getUser().getName()+" has left the server.");
        builder.withThumbnail(event.getUser().getAvatarURL());
        builder.withColor(255, 255, 0);
        builder.withDescription(formatter.format(date));
        event.getGuild().getChannelByID(439402726099517471L).sendMessage(builder.build());
    }
    @EventSubscriber
    public void userBan(UserBanEvent event)
    {
        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        builder.withAuthorName(event.getUser().getName()+" has been banned from the server.");
        builder.withThumbnail(event.getUser().getAvatarURL());
        builder.withColor(255, 0, 0);
        builder.withDescription(formatter.format(date));
        event.getGuild().getChannelByID(439402726099517471L).sendMessage(builder.build());
    }
}