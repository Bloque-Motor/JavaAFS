Aunque para toda la gestión del ciclo de desarrollo del código de la práctica se 
puede usar el IDE que se considere oportuno, para aquellos que prefieran no utilizar 
una herramienta de este tipo, se proporcionan una serie de scripts que permiten realizar toda 
la labor requerida. En esta sección, se explica cómo trabajar con estos scripts. 
Para probar la práctica, debería, en primer lugar, 
compilar todo el código desarrollado que se encuentra en el directorio, 
y paquete, afs, generando los ficheros JAR requeridos por el cliente y el servidor.

cd afs 
./compila_y_construye_JARS.sh 
A continuación, hay que compilar y ejecutar el servidor, activando previamente rmiregistry. 
cd servidor 
./compila_servidor.sh 
./arranca_rmiregistry 12345 & 
./ejecuta_servidor.sh 12345 
Por último, hay que compilar y ejecutar el cliente de prueba. 
cd cliente1 
./compila_test.sh 
export REGISTRY_HOST=triqui3.fi.upm.es 
export REGISTRY_PORT=12345 
export BLOCKSIZE=... # el tamaño que considere oportuno 
./ejecuta_test.sh 
Nótese que el servidor y el cliente pueden ejecutarse en distintas máquinas.
Además, tenga en cuenta que, si ejecuta varios clientes en la misma máquina, 
debería hacerlo en diferente directorio de cliente (cliente1, cliente2...).