package com.aeternity.aecan.util.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Atributos anotados con esta anotación serán excluidos en el proceso de SERIALIZACION (unicamente)
 * que realiza GSON
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcludeSerialization {
}
