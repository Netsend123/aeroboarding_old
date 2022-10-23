import java.io.IOException;
import java.text.ParseException;

public class Aero {
    public static void main(String[] args) throws IOException, ParseException, InterruptedException, org.json.simple.parser.ParseException {
        SearchAeroport searchAeroport = new SearchAeroport();
        searchAeroport.parse();
        ApiRequestAeroportLines apiRequestAeroportLines = new ApiRequestAeroportLines();
        apiRequestAeroportLines.request(searchAeroport.icao);
        new JsonParsingFlifgtList().parse(apiRequestAeroportLines.responseApiAeroLines);

    }

}
