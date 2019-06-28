// Clase de cliente que inicia la interacción con el servicio de
// ficheros remotos
package afs;

import java.rmi.*; 

public class Venus {

    public String host;
    public String port;
    public Vice cl;
    public String size;
    public VenusCB callback;

    public Venus() throws Exception{

        this.host = System.getenv().get("REGISTRY_HOST");
        this.port = System.getenv().get("REGISTRY_PORT");
        cl = (Vice)Naming.lookup("rmi://"+ this.host + ":" + this.port +"/AFS");
        this.size = System.getenv().get("BLOCKSIZE");
        callback = new VenusCBImpl();
    }
}