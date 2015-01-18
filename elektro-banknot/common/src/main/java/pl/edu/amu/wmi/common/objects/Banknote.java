package pl.edu.amu.wmi.common.objects;

import java.io.Serializable;

/**
  * Created by Tomasz on 2015-01-15.
  **/

public class Banknote implements Serializable {

    private String value;

    public Banknote(String value) {
        this.value = value;
    }

    public String getValue() {

        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
