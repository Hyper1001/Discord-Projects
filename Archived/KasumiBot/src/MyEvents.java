import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEditEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionRemoveEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.impl.events.shard.ShardReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyEvents
{
    IChannel userLogs;
    IChannel messageLogs;
    IChannel greetings;
    @EventSubscriber
    public void ready(ShardReadyEvent event)
    {
        userLogs = event.getShard().getChannelByID(445991116043452417L);
        messageLogs = event.getShard().getChannelByID(445991098582827008L);
        greetings = event.getShard().getChannelByID(445989473499480074L);
    }
    @EventSubscriber
    public void join (UserJoinEvent event) //join logs
    {
        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        builder.withAuthorName(event.getUser().getName()+" has joined the server.");
        builder.withThumbnail(event.getUser().getAvatarURL());
        builder.withColor(64, 252, 5);
        builder.withDescription(formatter.format(date));
        userLogs.sendMessage(builder.build());
        greetings.sendMessage("Welcome to the Kasumi Server, "+event.getUser().getName() +"! Please make sure to read "+event.getGuild().getChannelByID(446151589351391232L)+" and enjoy your time here.");
    }
    @EventSubscriber
    public void leave (UserLeaveEvent event) //leave logs
    {
        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        builder.withAuthorName(event.getUser().getName()+" has left the server.");
        builder.withThumbnail(event.getUser().getAvatarURL());
        builder.withColor(255, 255, 0);
        builder.withDescription(formatter.format(date));
        userLogs.sendMessage(builder.build());
    }
    @EventSubscriber
    public void ban(UserBanEvent event) //ban logs
    {
        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        builder.withAuthorName(event.getUser().getName()+" has been banned from the server.");
        builder.withThumbnail(event.getUser().getAvatarURL());
        builder.withColor(255, 0, 0);
        builder.withDescription(formatter.format(date));
        userLogs.sendMessage(builder.build());
    }
    @EventSubscriber
    public void edit(MessageEditEvent event) //edit logs
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
        messageLogs.sendMessage(builder.build());
    }
    @EventSubscriber
    public void delete(MessageDeleteEvent event) //delete logs
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName(event.getAuthor().getName()+"'s message has been deleted in #"+event.getChannel().getName()+".");
        builder.withThumbnail(event.getAuthor().getAvatarURL());
        builder.withColor(255, 0, 0);
        builder.withDescription(formatter.format(date));
        builder.appendField("Deleted Message", event.getMessage().getContent()  , false);
        messageLogs.sendMessage(builder.build());
    }
    @EventSubscriber
    public void reactionAdd(ReactionAddEvent event)
    {
        int size =1;
        String test = event.getReaction().getEmoji().getName();
        if(event.getUser().hasRole(event.getGuild().getRoleByID(446162041703759872L)))
        {
            size++;
        }
        if(event.getUser().hasRole(event.getGuild().getRoleByID(446069731372433439L)))
        {
            size++;
        }
        if(event.getChannel().getLongID()==445990374159417354L)
        {
            if(event.getUser().hasRole(event.getGuild().getRoleByID(445988221072048160L)))
            {
                event.getMessage().removeReaction(event.getUser(),event.getReaction());
            }
            else if(event.getReaction().getEmoji().getName().equals("RikoLewd"))
            {
                event.getUser().addRole(event.getGuild().getRoleByID(446162041703759872L));
            }
            else if(event.getUser().getRolesForGuild(event.getGuild()).size()==size)
            {
                event.getUser().addRole(event.getGuild().getRolesByName(test).get(0));
            }
            else
            {
                event.getMessage().removeReaction(event.getUser(),event.getReaction());
            }
        }
    }
    @EventSubscriber
    public void reactionDelete(ReactionRemoveEvent event)
    {
        String test = event.getReaction().getEmoji().getName();
        if(event.getReaction().getEmoji().getName().equals("RikoLewd"))
        {
            event.getUser().removeRole(event.getGuild().getRoleByID(446162041703759872L));
        }
        else if(event.getUser().getRolesForGuild(event.getGuild()).size()>=2)
        {
            event.getUser().removeRole(event.getGuild().getRolesByName(test).get(0));
        }
    }
    @EventSubscriber
    public void voiceJoin(UserVoiceChannelJoinEvent event)
    {
        event.getUser().addRole(event.getGuild().getRoleByID(446069731372433439L));
    }
    @EventSubscriber
    public void voiceLeave(UserVoiceChannelLeaveEvent event)
    {
        event.getUser().removeRole(event.getGuild().getRoleByID(446069731372433439L));
    }
}