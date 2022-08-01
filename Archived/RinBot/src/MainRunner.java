
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import sx.blah.discord.api.IDiscordClient;

public class MainRunner {

    public static void main(String[] args) {

        try {

            Properties prop = new Properties();
            InputStream input = null;
            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
//            String token = "MzU3MjE3NjIzMTIzMDM0MTE0.DJmsXQ.UOgoe4ObDcAo-ZQ8WToebwyStTA";
            String token = prop.getProperty("token");

            System.out.println("token is " + token);

            String chancesStr = prop.getProperty("chances", "400");
            String chosenStr = prop.getProperty("chosen", "88");
            String wordStr = prop.getProperty("word", "nya");
            try {
                MyEvents.word = wordStr;
                MyEvents.chances = Integer.parseInt(chancesStr);
                MyEvents.choosenNumber = Integer.parseInt(chosenStr);

            } catch (Exception e) {
                e.printStackTrace();
            }

//            System.out.println("hi: " + MyEvents.chances);
//            System.out.println("hi2: " + MyEvents.choosenNumber);
            IDiscordClient cli = BotUtils.getBuiltDiscordClient(token);

            /*
            // Commented out as you don't really want duplicate listeners unless you're intentionally writing your code
            // like that.
            // Register a listener via the IListener interface
            cli.getDispatcher().registerListener(new IListener<MessageReceivedEvent>() {
            public void handle(MessageReceivedEvent event) {
            if(event.getMessage().getContent().startsWith(BotUtils.BOT_PREFIX + "test"))
            BotUtils.sendMessage(event.getChannel(), "I am sending a message from an IListener listener");
            }
            });
             */
            // Register a listener via the EventSubscriber annotation which allows for organisation and delegation of events
            cli.getDispatcher().registerListener(new MyEvents());
            // Only login after all events are registered otherwise some may be missed.
            cli.login();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainRunner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainRunner.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
