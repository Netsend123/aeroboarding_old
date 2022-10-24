import io.restassured.path.json.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeRange {

    public String createTimeForApi(String icao) {
        String currentAirportTime;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://aerodatabox.p.rapidapi.com/airports/icao/"+icao+"/time/local"))
                .header("X-RapidAPI-Key", "2ea3972599mshf337a6ce1622083p183087jsn9b1ac0ee54b4")
                .header("X-RapidAPI-Host", "aerodatabox.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null; //получили Json запрос с текущим временем нужного аэропорта по коду ICAO
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(response.body());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        currentAirportTime = JsonPath.from(String.valueOf(jsonObject)).getString("localTime"); //достали из json запроса параметр со временем

        System.out.println(currentAirportTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date localTime= null; //создали дату на основе строки со временем из запроса
        try {
            localTime = formatter.parse(currentAirportTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(localTime);   //установили это время  в календаре
        calendar.add(Calendar.HOUR, 8); // прибавляем 8 часов от текущего времени аэропорта
        String forwardTime = formatter.format(calendar.getTime()).replace(' ', 'T');
        calendar.add(Calendar.HOUR, -12);// отнимаем 12 часов
        String backTime =  formatter.format(calendar.getTime()).replace(' ', 'T');
        String createApiTimeString = backTime+"/"+forwardTime; //формируем окончательную строку времени для последующей вставки в Api запрос, для получения списка рейсов аэропорта из нужного временного диапазона
        System.out.println(createApiTimeString);
        return createApiTimeString;
    }
}
