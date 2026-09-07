package com.undere418code.kanban_api.util;

import org.openapitools.jackson.nullable.JsonNullable;
import java.util.function.Consumer;

public final class JsonNullablePatch {

    private JsonNullablePatch() {
    }

    public static boolean isProvided(JsonNullable<?> field) {
        return field.isPresent();
    }

    public static <T> boolean applyPatch(JsonNullable<T> field, Consumer<T> setter) {
        if (field.isPresent()) {
            setter.accept(field.get()); 
            return true;// get() renvoie la valeur, y compris null si envoyé explicitement
        }
        return false;// si absent : on ne touche pas au champ, setter jamais appelé regarde uniquement si le patcha ete appliquer
    }

    public static boolean isNull(JsonNullable<?> field) {
        return field.isPresent() && field.get() == null;
    }

    public static boolean isEmptyString(JsonNullable<String> field) {
        return isNull(field) && field.get().isBlank();
    }

    public static <T> T value(JsonNullable<T> field) {
        return field.isPresent() ? field.get() : null;
    }
}
