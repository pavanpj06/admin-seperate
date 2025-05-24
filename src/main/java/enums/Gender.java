package enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE, FEMALE,OTHER;

    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null) return null;
        switch (value.toLowerCase()) {
            case "male": return MALE;
            case "female": return FEMALE;
            case "other": return OTHER;
            default: throw new IllegalArgumentException("Invalid gender: " + value);
        }
    }

    @JsonValue
    public String toJson() {
        return name().charAt(0) + name().substring(1).toLowerCase(); // Returns "Male" or "Female"
    }
}
