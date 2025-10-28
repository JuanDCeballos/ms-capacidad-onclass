package co.onclass.api.constants;

public final class ApiConstants {

    private ApiConstants() {

    }

    private static final String API_VERSION_BASE = "/api/v1";
    private static final String CAPACIDAD_BASE = API_VERSION_BASE + "/capacidad";

    public static final String ID_CAPACIDAD_PATH_VARIABLE = "idCapacidad";

    public static final String GUARDAR_CAPACIDAD = CAPACIDAD_BASE;
    public static final String CAPACIDAD_BY_ID = CAPACIDAD_BASE + "/{" + ID_CAPACIDAD_PATH_VARIABLE + "}";
    public static final String AGREGAR_TECNOLOGIAS = CAPACIDAD_BY_ID + "/tecnologias";
    public static final String OBTENER_CAPACIDADES = CAPACIDAD_BASE;
    public static final String ASIGNAR_CAPACIDADES = CAPACIDAD_BASE + "/asignar-capacidades";
}
