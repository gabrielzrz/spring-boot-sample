package br.com.gabrielzrz.enums;

/**
 * @author Zorzi
 */
public enum DataType {

    ATTACHMENT("Attachment", "%a"),
    BOOLEAN("Logical value (true/false)", "%b"),
    DATE("Date value (year, month, day)", "%tD"),
    DATETIME("Date and time value", "%tDT"),
    DECIMAL("Decimal number with fractional part", "%f"),
    INTEGER("Whole number", "%d"),
    LIST("Collection of values", "%l"),
    TEXT("Free-form textual data", "%s");

    // Variables
    private final String name;
    private final String placeholder;

    // Constructors
    DataType(String name, String placeholder) {
        this.name = name;
        this.placeholder = placeholder;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
