

public class Aero {
    public static void main(String[] args){
        SearchAeroport searchAeroport = new SearchAeroport();
        searchAeroport.parse();
        ApiRequestAeroportLines apiRequestAeroportLines = new ApiRequestAeroportLines();
        apiRequestAeroportLines.request(searchAeroport.icao);
        new JsonParsingFlifgtList().parse(apiRequestAeroportLines.responseApiAeroLines);

    }

}
