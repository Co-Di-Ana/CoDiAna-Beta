package edu.codiana.sql;



/**
 * Simple class representing user type
 * @author Jan Hybš
 * @version 1.0
 */
public enum SubjectType {

    STUDENT ("student"), GROUP ("group"), TEACHER ("teacher");
    private String value;



    private SubjectType (String value) {
	this.value = value;
    }



    @Override
    public String toString () {
	return value;
    }
}
