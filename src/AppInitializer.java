import com.vdurmont.emoji.EmojiParser;
import de.jensd.fx.glyphs.emojione.EmojiOne;
import de.jensd.fx.glyphs.emojione.EmojiOneView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("lk/ijse/chat_room/view/LoginForm.fxml"))));
        primaryStage.setTitle("Join Live Chat");

        /*System.out.println("\uD83E\uDD29");
        System.out.println("\u001B[32m");
        System.out.println("\u1F603");

        byte[] emojiByteCode = new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0x81};
        String emoji = new String(emojiByteCode, StandardCharsets.UTF_8);
        System.out.println(emoji);
        String s = Character.toString((char) 0x1F601);
        System.out.println(s);*/

        /*String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
        String result = EmojiParser.parseToUnicode(str);
        System.out.println(result);*/

        /*String context = "You can eat water too! "+"🍉";
        context += new String(Character.toChars(0x1F349));
        System.out.println(context);


        byte[] emojiByteCode = new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0x81};
        String emoji = new String(emojiByteCode, Charset.forName("UTF-8"));
        System.out.println(emoji);*/

        primaryStage.show();

    }
}
