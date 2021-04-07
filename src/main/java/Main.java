import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import structure.Subway;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    private static String dataFile = "src/main/resources/map.json";

    public static void main(String[] args) throws IOException, ParseException {

        Document document = Jsoup.connect("https://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена")
                .userAgent("Chrome/80.0.3987.132 YaBrowser/20.3.2.242 Yowser/2.5 Safari/537.36")
                .referrer("https://yandex.ru/")
                .get();

        Elements elements = document.select("table.standard ").select("tr");

        for (Element element : elements) {
            if (!element.getElementsByTag("th").isEmpty())
            {
                continue;
            }
            List<String> lineNum = element.select("td:eq(0)").select("span.sortkey").eachText();
            String lineName = element.select("td:eq(0)").select("span").attr("title");
            String stationName = element.select("td:eq(1) a:first-of-type:not(small > a)").text();
            List<String> doubleRouteStation = element.select("td:eq(0)").select("span").eachAttr("title");
            List<String> connections = element.select("td:eq(3)").select("span.sortkey").eachText();
            List<String> connectionStationName = element.select("td:eq(3)").select("span").eachAttr("title");
            Parser.createLines(lineNum, lineName);
            Parser.createStations(lineNum, stationName, doubleRouteStation);
            if (connections.size() != 0)
            {
                Parser.createConnections(lineNum, stationName, connections, connectionStationName, doubleRouteStation);
            }
        }

        Subway subway = new Subway(Parser.getStations(),Parser.getLines(),Parser.getConnectionsList());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter file = new FileWriter(dataFile)) {

            file.write(gson.toJson(subway));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Parser.parseJSONFile();
    }

}
