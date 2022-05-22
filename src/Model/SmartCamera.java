package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SmartCamera extends SmartDevices {

    private String resolution;

    public static HashMap<String, Float> getResolucoes() {
        return resolucoes;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getResolution() {
        return resolution;
    }

    private static final HashMap<String,Float> resolucoes = new HashMap<>();
    static {
        resolucoes.put("144p",144f);
        resolucoes.put("240p",240f);
        resolucoes.put("480p",480f);
        resolucoes.put("720p",720f);
        resolucoes.put("1080p",1080f);
        resolucoes.put("2K",1440f);
        resolucoes.put("4K",2160f);
        resolucoes.put("8K",4320f);
    }

    public SmartCamera(String resolution) {
        this.resolution = resolution;
        super.price = 50f;
        super.manufacturerId = id;
        id++;
        super.changeState(false);
    }

    @Override
    public float getDailyComsumption() {
        float sizeOfFile = 10f;
        return sizeOfFile * resolucoes.get(resolution);
    }
}
