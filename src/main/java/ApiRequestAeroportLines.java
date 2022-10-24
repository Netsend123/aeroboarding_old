import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;



public class ApiRequestAeroportLines {

    public String responseApiAeroLines;

    public void request(String icao) {
        String timeRange = new TimeRange().createTimeForApi(icao);
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create("https://aerodatabox.p.rapidapi.com/flights/airports/icao/" + icao + "/" + timeRange + "?withLeg=true&withCancelled=true&withCodeshared=true&withCargo=true&withPrivate=true&withLocation=false"))
                .header("X-RapidAPI-Key", "2ea3972599mshf337a6ce1622083p183087jsn9b1ac0ee54b4")
                .header("X-RapidAPI-Host", "aerodatabox.p.rapidapi.com")
                .method("GET", java.net.http.HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;

        {
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

//        try (FileWriter fw = new FileWriter("c:\\departures.txt")) {
//
//            fw.write(response.body());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        responseApiAeroLines = response.body();

    }


}
