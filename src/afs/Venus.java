// Clase de cliente que inicia la interacción con el servicio de
// ficheros remotos
package afs;

import java.rmi.*; 


public class Venus {

    private String host;
    private String port;
    private Vice cl;
    private String size;
    private VenusCB callback;

    public Venus() throws Exception{

        this.host = System.getenv().get("REGISTRY_HOST");
        this.port = System.getenv().get("REGISTRY_PORT");
        cl = (Vice)Naming.lookup("rmi://"+ this.host + ":" + this.port +"/AFS");
        this.size = System.getenv().get("BLOCKSIZE");
        callback = new VenusCBImpl();
    }
}