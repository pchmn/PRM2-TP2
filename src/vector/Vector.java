package vector;


import java.util.Map;

public class Vector {

    public static double norme(Map<String, Integer> vector) {
        int result = 0;

        for (Map.Entry<String, Integer> entry : vector.entrySet()) {
            result += entry.getValue() * entry.getValue();
        }

        return Math.sqrt(result);
    }

    public static int produitScalaire(Map<String, Integer> v1, Map<String, Integer> v2) {
        int result = 0;

        for (Map.Entry<String, Integer> entry : v1.entrySet()) {
            result += entry.getValue() * v2.get(entry.getKey());
        }

        return result;
    }

    public static double cos(Map<String, Integer> v1, Map<String, Integer> v2) {
        int scalaire = produitScalaire(v1, v2);
        double normeV1 = norme(v1);
        double normeV2 = norme(v2);

        return scalaire / (normeV1 * normeV2);
    }

    public static double angle(Map<String, Integer> v1, Map<String, Integer> v2) {
        return Math.acos(cos(v1, v2));
    }
}
