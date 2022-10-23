import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import io.restassured.path.json.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParsingFlifgtList {
    private static final String filePath = "C:\\departures.txt";

    public void parse(String jsonAeroLine) throws ParseException, FileNotFoundException {
        //FileReader reader = new FileReader(filePath);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonAeroLine);

        int i = 0;
        while (JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].number") != null) {
            System.out.print(JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].departure.actualTimeLocal") + "  ");
            System.out.print(JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].arrival.airport.name") + "  ");
            System.out.print(JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].number") + "  ");
            System.out.print(JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].status") + "  ");
            System.out.print(JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].airline.name") + "  ");
            System.out.println(JsonPath.from(String.valueOf(jsonObject)).getString("departures["+i+"].aircraft.model") + "  ");
            i++;

        }
    }
}
