package enums;

public enum ServiceTypes {
    LANGUAGE_PROCESSING(0),
    HTTP_SERVER(1),
    IMAGE_SERVER(2),
    CALCULATOR_SERVER(3);

    ServiceTypes(int serviceType) {
        this.serviceType = serviceType;
    }

    private int serviceType;
}
