package ru.itis.helper;

public enum ReadingStatus {
    NOT_READING("notReading", "Не читаю"),
    READING("reading", "Читаю"),
    PLANNED("planned", "В планах"),
    COMPLETED("completed", "Прочитано"),
    DROPPED("dropped", "Брошено"),
    FAVORITE("favorite", "Любимое");

    private final String value;
    private final String label;

    ReadingStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
