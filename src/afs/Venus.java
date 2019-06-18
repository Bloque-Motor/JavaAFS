// Clase de cliente que inicia la interacción con el servicio de
// ficheros remotos
package afs;

import java.rmi.*; 


public class Venus {
    public String host;
    public String port;
    public String tam;
    public Vice rec;
    public VenusCB callback;

    public Venus() throws Exception{
        this.host = System.getenv().get("REGISTRY_HOST");
        this.port = System.getenv().get("REGISTRY_PORT");
        this.tam = System.getenv().get("BLOCKSIZE");
        rec = (Vice)Naming.lookup("rmi://"+ this.host + ":" + this.port +"/AFS");
        callback = new VenusCBImpl();
    }
}
