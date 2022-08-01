import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class MainRunner {

    public static void main(String[] args)
    {
        IDiscordClient cli = new ClientBuilder().withToken("NDUwODQ1NDU0OTUyMDM4NDM5.DfSGfw.VVb6g_LjIks5kGqwFfXp_J1uut4").login();
        cli.getDispatcher().registerListener(new MyEvents());
    }
}