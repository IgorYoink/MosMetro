package structure;

import java.util.List;
import java.util.Map;

public class Subway {

    Map<String,List<String>> stations;
    List<Line> lines;
    List<List<Station>> connections;

    public Subway(Map<String, List<String>> stations, List<Line> lines, List<List<Station>> connections) {
        this.stations = stations;
        this.lines = lines;
        this.connections = connections;
    }
}
