package android.bigplan.lego.model;

import java.io.Serializable;

public class BankModule implements Serializable {   
    private String Code;
	private String Name;
	
	
    public String getCode() {
        return Code;
    }

    public void setId(String code) {
        Code = code;
    }
	
   
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
