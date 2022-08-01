import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEditEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionRemoveEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.impl.events.shard.ShardReadyEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.EmbedBuilder;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyEvents
{
    IChannel userLogs;
    IChannel messageLogs;
    IChannel greetings;
    IChannel announcements;
    final String prefix = "!";
    @EventSubscriber
    public void ready(ShardReadyEvent event)
    {
        userLogs = event.getShard().getGuilds().get(0).getChannelByID(435221618587860993L);
        messageLogs = event.getShard().getGuilds().get(0).getChannelByID(435221691761688576L);
        greetings = event.getShard().getGuilds().get(0).getChannelByID(184155676002484225L);
        announcements = event.getShard().getGuilds().get(0).getChannelByID(184153725403791360L);
    }
    @EventSubscriber
    public void MessageReceived(MessageReceivedEvent event)
    {
        if(event.getMessage().getContent().startsWith(prefix + "help"))
        {
            event.getChannel().sendMessage("!hagu or !hugu - Prints out a random hagu/hugu gif\n!join - Joins voice call if you are in it.\n!leave - Leaves voicecall if in it.");
        }
        else if(event.getMessage().getContent().startsWith(prefix + "status")&&event.getAuthor().hasRole(event.getGuild().getRolesByName("Mod").get(0)))
        {
            String status = event.getMessage().getContent().substring(7);
            MainRunner.cli.changePresence(StatusType.ONLINE,ActivityType.PLAYING,status);
        }
        else if(event.getMessage().getContent().startsWith(prefix+"hagu")||event.getMessage().getContent().startsWith(prefix+"hugu")) //prints out random hug gif
        {
            String test [] = {"https://imgur.com/a/yjcMC","https://imgur.com/a/g9B5N","https://imgur.com/a/zjt73","https://imgur.com/a/viBRScT","https://imgur.com/a/F0LTBik","https://imgur.com/a/LpxC9PW","https://imgur.com/a/dyWeq6a","https://imgur.com/a/q0hTeyA","https://imgur.com/a/qLb7QKf","https://imgur.com/a/1eiZNcZ","https://imgur.com/a/82F2WHT","https://imgur.com/a/0qPZNHE","https://imgur.com/a/9G8kFGJ","https://imgur.com/a/yAVzmJZ","https://imgur.com/a/T9v71Il","https://imgur.com/a/KZRCO3N","https://imgur.com/a/j3dPb1K","https://imgur.com/a/WFpd52f","https://imgur.com/a/lpXoH2I","https://imgur.com/a/MrUPOOB","https://imgur.com/a/D5Bdmpe","https://imgur.com/a/tabXL74","https://imgur.com/a/YSkCOQi","https://imgur.com/a/y3sphze","https://imgur.com/a/wEXXCyp"};
            int pick = (int)(Math.random()*test.length);
            event.getChannel().sendMessage(test[pick]);
        }
        else if(event.getMessage().getContent().startsWith(prefix+"join"))
        {
            IVoiceChannel join = event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel();
            if(join==null)
            {
                event.getChannel().sendMessage("You have to be in a voice channel to make me join.");
            }
            else
            {
                join.join();
            }
        }
        else if(event.getMessage().getContent().startsWith(prefix+"leave"))
        {
            IVoiceChannel leave = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();
            if (leave==null)
            {
                return;
            }
            else
            {
                leave.leave();
            }
        }
        else if (event.getMessage().getContent().startsWith(prefix+"senda"))
        {
            announcements.sendMessage(event.getMessage().getContent().substring(5));
        }
        else if(event.getMessage().getContent().contains("hello"))
        {
            event.getMessage().addReaction(event.getGuild().getEmojiByName("KananWave"));
        }
        else if(event.getMessage().getContent().contains("night"))
        {
            event.getMessage().addReaction(event.getGuild().getEmojiByName("kananSleeper"));
        }
        else if(event.getMessage().getContent().contains("hagu"))
        {
            event.getMessage().addReaction(event.getGuild().getEmojiByName("KananHug"));
        }
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
        greetings.sendMessage("Welcome to the Dolphin Tank "+event.getUser().getName() +"! Please make sure to read "+event.getGuild().getChannelByID(184147921367728128L)+" and enjoy your time here.");
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
        int size = 1;
        if(event.getMessageID()==434439080986804264L)
        {
            if(event.getUser().hasRole(event.getGuild().getRoleByID(436191365664342016L))) //does user have voiceless dolphin
            {
                size++;
            }
            if(event.getUser().hasRole(event.getGuild().getRoleByID(434937953391738884L))) //does user have pure dolphin?
            {
                size++;
            }
            if(event.getUser().hasRole(event.getGuild().getRoleByID(450683631288188929L)))
            {
                size++;
            }
            String test = event.getReaction().getEmoji().getName();
            test = test.substring(0,test.length()-3);
            //add color roles
            if(event.getUser().hasRole(event.getGuild().getRoleByID(175184780592283648L)))
            {
                event.getMessage().removeReaction(event.getUser(),event.getReaction());
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
        else if(event.getMessageID()==434437938588418059L)//Add Pure Dolphin
        {
            event.getUser().addRole(event.getGuild().getRoleByID(434937953391738884L));
        }
        else if(event.getMessageID()==450678027538661386L)
        {
            event.getUser().addRole(event.getGuild().getRoleByID(450683631288188929L));
        }
    }
    @EventSubscriber
    public void reactionDelete(ReactionRemoveEvent event)
    {
        if(event.getMessageID()==434439080986804264L) //Remove the color roles
        {
            String test = event.getReaction().getEmoji().getName();
            test = test.substring(0,test.length()-3);
            if(event.getUser().getRolesForGuild(event.getGuild()).size()>=2)
            {
                event.getUser().removeRole(event.getGuild().getRolesByName(test).get(0));
            }
        }
        else if(event.getMessageID()==434437938588418059L) //Remove Pure dolphin
        {
            event.getUser().removeRole((event.getGuild().getRoleByID(434937953391738884L)));
        }
        else if(event.getMessageID()==450678027538661386L)
        {
            event.getUser().removeRole(event.getGuild().getRoleByID(450683631288188929L));
        }
    }
    @EventSubscriber
    public void userJoinVoice(UserVoiceChannelJoinEvent event)
    {
        event.getUser().addRole(event.getGuild().getRoleByID(436191365664342016L));
    }
    @EventSubscriber
    public void userLeaveVoice(UserVoiceChannelLeaveEvent event)
    {
        event.getUser().removeRole(event.getGuild().getRoleByID(436191365664342016L));
    }
}