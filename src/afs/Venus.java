package afs;

import java.rmi.*; 

public class Venus {

    private Vice vice;
    private int size;
    private VenusCB venusCallback;

    public Venus() throws Exception{

        String host = System.getenv().get("REGISTRY_HOST");
        String port = System.getenv().get("REGISTRY_PORT");
        vice = (Vice) Naming.lookup("rmi://" + host + ":" + port + "/AFS");
        size = Integer.parseInt(System.getenv().get("BLOCKSIZE"));
        venusCallback = new VenusCBImpl();
    }

    int getSize() {
        return size;
    }

    Vice getVice() {
        return vice;
    }

    VenusCB getVenusCB() {
        return venusCallback;
    }
}