package org.dieschnittstelle.mobile.android.dataaccess.remote;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/authenticate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ValidationAccessor {

	@POST
	public Response authenticate(MyJSONOject json) {
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis() - start < 1500){
			
		}
		if (validatePasswort(json.password) && validateEmail(json.email)) {
			return Response.status(200).build();
		} else {
			return Response.status(403).build();
		}

	}

	public boolean validatePasswort(String password) {
		try {
			Integer.parseInt(password);
		} catch (NumberFormatException nfe) {
			return false;
		}
		if (password.length() != 6) {
			return false;
		}
		return true;
	}

	public boolean validateEmail(String email) {
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			return false;
		}
		return true;
	}

}

@XmlRootElement
class MyJSONOject {
	@XmlElement(name = "email")
	public String email;
	@XmlElement(name = "password")
	public String password;
}
