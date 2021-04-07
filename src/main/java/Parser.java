import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import structure.Line;
import structure.Station;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Parser {

    private static List<Line> lines = new ArrayList<>();
    private static List<List<Station>> connectionsList = new ArrayList<>();
    private static Map<String, List<String>> stations = new HashMap<>();

    private static String dataFile = "src/main/resources/map.json";
    static List<Line> lineList = new ArrayList<>();

    static void createLines(List<String> lineNumber, String lineName)
    {
        Line line = new Line(lineNumber.get(0), lineName);
        if (!lines.contains(line))lines.add(line);
    }
    static void createStations(List<String> lineNumber, String stationName, List<String> doubleRoute)
    {
        String number = lineNumber.get(0);
        if (!stations.containsKey(number))
        {
            stations.put(number, new ArrayList<>());
        }
        stations.get(number).add(stationName);

        if (doubleRoute.size() == 2)
        {
            String number2 = lineNumber.get(1);
            if (!stations.containsKey(number2))
            {
                stations.put(number2, new ArrayList<>());
            }
            stations.get(number2).add(stationName);
        }
    }

    public static void createConnections(List<String> lineNumber, String stationName,List<String> connections, List<String>conStationName, List<String> doubleRoutes)
    {
        List<Station> list = new ArrayList<>();
        list.add(new Station(lineNumber.get(0),stationName));

        for (int i = 0; i < connections.size(); i++)
        {
            list.add(new Station(connections.get(i), conStationName.get(i)));
        }
        connectionsList.add(list);

        if (doubleRoutes.size() == 2)
        {
            List<Station> list2 = new ArrayList<>();
            list2.add(new Station(lineNumber.get(1),stationName));
            for (int i = 0; i < connections.size(); i++)
            {
                list2.add(new Station(connections.get(i),conStationName.get(i)));
            }
            connectionsList.add(list2);
        }
    }

    private static String getJsonFile()
    {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(dataFile));
            lines.forEach(line -> builder.append(line));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }

    public static void parseJSONFile() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());

        JSONArray linesArray = (JSONArray) jsonData.get("lines");
        parseLines(linesArray);

        Map<String, List<String>> stations = (Map<String, List<String>>) jsonData.get("stations");
        Map<String, List<String>> sortedStations = new TreeMap<>(stations);

        parseStations(sortedStations);
    }
    private static void parseLines(JSONArray linesArray)
    {
        linesArray.forEach(lineObject -> {
            JSONObject lineJsonObject = (JSONObject) lineObject;
            Line line = new Line(
                    ((String) lineJsonObject.get("number")),
                    (String) lineJsonObject.get("name")
            );
            lineList.add(line);
        });
    }

    private static void parseStations(Map<String, List<String>> stations)
    {
        for (Map.Entry station : stations.entrySet())
        {
            JSONArray stationArray = (JSONArray) station.getValue();
            lineList.forEach(line ->
            {
                if (line.getNumber().equals(station.getKey()))
                {
                    System.out.println("Линия " + line.getNumber().replaceFirst("^0+(?!$)", "") + " - " + line.getName()+ " - количество станций: " + stationArray.size() + " станций");
                }
            });
        }
    }

    public static List<Line> getLines() {
        return lines;
    }

    public static Map<String, List<String>> getStations() {
        return stations;
    }

    public static List<List<Station>> getConnectionsList() {
        return connectionsList;
    }
}
