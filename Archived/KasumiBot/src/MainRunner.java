import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class MainRunner {

    public static void main(String[] args)
    {
        IDiscordClient cli = new ClientBuilder().withToken("NDQ2MTU4MDM0MjU1ODcyMDAw.Dd08kg.ciMj6jSPdam6w20ME2q9gbcsD68").login();
        cli.getDispatcher().registerListener(new MyEvents());
    }
}