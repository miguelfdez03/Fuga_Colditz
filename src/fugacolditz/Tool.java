package fugacolditz;

import java.util.ArrayList;

public class Tool {
    String type;
    ArrayList<Position> toolPositions;
    
    public Tool(String type) {
        this.type = type;
        this.toolPositions = new ArrayList<Position>();
    }
}
