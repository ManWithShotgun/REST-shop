
package ru.ilia.soap.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customError.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="customError">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DELETE"/>
 *     &lt;enumeration value="UPDATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "customError", namespace = "http://service.soap.ilia.ru/")
@XmlEnum
public enum CustomError {

    DELETE,
    UPDATE;

    public String value() {
        return name();
    }

    public static CustomError fromValue(String v) {
        return valueOf(v);
    }

}
