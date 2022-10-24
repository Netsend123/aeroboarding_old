import io.restassured.path.json.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


public class SearchAeroport {
    String aeroportFromSearchLine;
    String encodeURLFromSearchLine;
    String icao;

    public String inputSearchAeroport() {

        try { //читаем строку поисковый запрос по названию аэропорта или города
            BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
            return is.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void encodeURL() {
        try {
            aeroportFromSearchLine = inputSearchAeroport();
            encodeURLFromSearchLine = URLEncoder.encode(aeroportFromSearchLine, StandardCharsets.UTF_8.toString()); //при наличии кирилических символов в запросе кодируем их в URL код

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchAeroportToAPI() { //формируем на основе полученой строки поиска Api запрос, на поиск нужного аэропорта
        java.net.http.HttpRequest request;
        request = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create("https://aerodatabox.p.rapidapi.com/airports/search/term?q=" + encodeURLFromSearchLine + "&limit=10"))
                .header("X-RapidAPI-Key", "2ea3972599mshf337a6ce1622083p183087jsn9b1ac0ee54b4")
                .header("X-RapidAPI-Host", "aerodatabox.p.rapidapi.com")
                .method("GET", java.net.http.HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        icao = response.body();

    }


    public String getIcao() {
        return icao;
    }


    public void parse() { //парсинг json ответа. на выходе получаем код icao нужного аэропорта для дальнейшей вставки в Api запрос списка рейсов по этому аэропрту


        encodeURL();
        searchAeroportToAPI();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(icao);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = 0;
        while (JsonPath.from(String.valueOf(jsonObject)).getString("items[0].name") == null){
            System.out.println("Not found. Try one more time");
            encodeURL();
            searchAeroportToAPI();
            try {
                jsonObject = (JSONObject) jsonParser.parse(icao);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        while (JsonPath.from(String.valueOf(jsonObject)).getString("items[" + i + "].name") != null) { //выводим нумерованый список аэропортов по поисковому запросу если их несколько
            System.out.print((i + 1) + "  " + JsonPath.from(String.valueOf(jsonObject)).getString("items[" + i + "].name") + "   ");
            System.out.println((JsonPath.from(String.valueOf(jsonObject)).getString("items[" + i + "].icao")));
            i++;
        }
        if (i == 1) {
            icao = JsonPath.from(String.valueOf(jsonObject)).getString("items[0].icao"); //если результат поиска один аэропорт, то сразу возвращаем его код ICAO

        } else if (i > 1) { //если по запросу найдено несколько аэпопротов то просим ввести его порядковый номер и возвращаем код icao cоответсвующий этому аэропорту
            System.out.println("Input list number of aerоport");
            int aeroNumber = Integer.parseInt(inputSearchAeroport()); //вводим порядковый номер
            icao = JsonPath.from(String.valueOf(jsonObject)).getString("items[" + (aeroNumber - 1) + "].icao");
        }
    }

}

