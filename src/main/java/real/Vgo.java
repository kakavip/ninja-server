package real;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import threading.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vgo {
    public short minX;
    public short minY;
    public short maxX;
    public short maxY;
    public int mapid;
    public short goX;
    public short goY;
    

    @Override
    public String toString() {
        return "[" + minX + "," + minY + "," + maxX + "," + maxY + "," + mapid + "," + goX + "," + goY + "]";
    }
}
