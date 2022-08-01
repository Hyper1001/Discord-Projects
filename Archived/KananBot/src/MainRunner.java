import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class MainRunner
{
    static IDiscordClient cli;
    public static void main(String[] args)
    {
        cli = new ClientBuilder().withToken("NDM2MzYzOTk4MDUzNjYyNzIw.XTqlAw.OvAtUSBiaGHgi-65Bmg4HvTBdxM").login();
        cli.getDispatcher().registerListener(new MyEvents());   
    }
}