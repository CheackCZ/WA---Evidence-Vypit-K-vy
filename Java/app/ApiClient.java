package Java.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Base64;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiClient {

    private static String username = "coffe";
    private static String password = "kafe";

    /**
     * Fetches the list of people from the server, parses the response, and returns the list of Person objects.
     * 
     * @return A list of Person objects
     * @throws Exception if there is an error in the request or parsing
     */
    public List<Person> getPeopleList() throws Exception {
        String response = sendGetRequest("getPeopleList");
        List<Person> people = new ArrayList<>();
        
        if (response.length() == 0) {
            System.out.println(" --> No data accepted.");
        } else {
           // Remove outer curly braces
            response = response.substring(1, response.length() - 1);

            // Split into individual person objects by the key (i.e., "1", "2", etc.)
            String[] personEntries = response.split("},\"");
            
            // Process each person entry
            for (String entry : personEntries) {
                // Reformat entry if necessary
                entry = entry.replace("{", "").replace("}", "");
                
                // Split entry into fields (ID, name, email)
                String[] fields = entry.split(",");
                
                int id = -1;
                String name = "";
                String email = "";

                for (String field : fields) {
                    String[] keyValue = field.split(":");
                    String key = keyValue[0].replace("\"", "").trim();
                    String value = keyValue[1].replace("\"", "").trim();

                    if (key.equals("ID")) {
                        id = Integer.parseInt(value);
                        
                    } else if (key.equals("name")) {
                        name = value;
                        
                    } else if (key.equals("email")) {
                        email = value;
                    }
                }

                // Add person to the list if valid
                if (id != -1 && !name.isEmpty() && !email.isEmpty()) {
                    people.add(new Person(id, name, email));
                    System.out.println("Person added: " + name);
                }
            }

            System.out.println(" --> Data accepted successfully.");
        }
        return people;
    }

    /**
     * Method to send coffee data to server (not fully implemented).
     * 
     * @param user - person's name
     * @param coffeeAmount - amount of coffee
     * @throws Exception if there is an error in the request
     */
    public void saveCoffeeData(String user, int coffeeAmount) throws Exception {
        // Implement POST request logic here
        System.out.println("Odesílám data: " + user + " vypil " + coffeeAmount + " kávy.");
    }

    /**
     * Sends a GET request to the specified command on the server.
     * 
     * @param command - the API command to be executed
     * @return the server response as a String
     * @throws Exception if there is an error in the request
     */
    private String sendGetRequest(String command) throws Exception {
        String url = "http://ajax1.lmsoft.cz/procedure.php?cmd=" + command;
        URL obj = new URL(url);
        
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");

        String basicAuth = "Basic " + new String(Base64.getEncoder().encode((username + ":" + password).getBytes()));
        connection.setRequestProperty("Authorization", basicAuth);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new Exception("Server returned non-OK status: " + responseCode);
        }
    }
}
