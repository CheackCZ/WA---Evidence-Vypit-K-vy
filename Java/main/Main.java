package Java.main;

import Java.app.CoffeeApp;

/**
 * The {@code Main} class starts the coffee tracking application by initializing and displaying the {@link CoffeeApp} GUI.
 */
public class Main {

    public static void main(String[] args) {

        CoffeeApp app = new CoffeeApp();
        app.setVisible(true);
    
    }

}