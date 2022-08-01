import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEditEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.shard.ShardReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyEvents
{
    IChannel messageLogs;
    IChannel userLogs;
    IChannel general;
    final String prefix =">";
    @EventSubscriber
    public void shardReady(ShardReadyEvent event)
    {
        userLogs = event.getShard().getGuilds().get(0).getChannelByID(452551254917251092L);
        messageLogs = event.getShard().getGuilds().get(0).getChannelByID(452551271220772864L);
        general = event.getShard().getGuilds().get(0).getChannelByID(449689201819254785L);
    }
    @EventSubscriber
    public void userJoin(UserJoinEvent event)
    {
        EmbedBuilder builder = new EmbedBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        builder.withAuthorName(event.getUser().getName()+" has joined the server.");
        builder.withThumbnail(event.getUser().getAvatarURL());
        builder.withColor(64, 252, 5);
        builder.withDescription(formatter.format(date));
        userLogs.sendMessage(builder.build());
        general.sendMessage("Everyone welcome "+event.getUser().mention()+" to the server!");
    }
    @EventSubscriber
    public void leave (UserLeaveEvent event)
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
}
