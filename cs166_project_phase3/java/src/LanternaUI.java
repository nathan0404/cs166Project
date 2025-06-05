import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

public class LanternaUI {
    public static void start(AirlineManagement esql) throws Exception {
        Screen screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();

        WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
        BasicWindow window = new BasicWindow("Airline Management");

        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));

        panel.addComponent(new Label("Flight Number:"));
        TextBox inputBox = new TextBox();
        panel.addComponent(inputBox);

        panel.addComponent(new EmptySpace(new TerminalSize(0, 0))); // spacer
        Button searchButton = new Button("Search", () -> {
            String flightNumber = inputBox.getText();
            MessageDialog.showMessageDialog(gui, "Info", "You entered: " + flightNumber);
            // Later: call a method like esql.featureX(flightNumber);
        });
        panel.addComponent(searchButton);

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }
}
