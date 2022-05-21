# Server
Contains two threads, <a href="src/UDPServer.java">UDP Server</a> and <a href="src/ApplicationServer.java">Application Server</a>.

## ApplicationServer
Application server is the server that communicates with the client and
handles the task.
Upon receiving a request, calls the <a href="src/UDPServer.java">UDPServer:broadcast(String)</a>
to send a broadcast message to all services nearby.
After the timeout returns the best address available.
## UDPServer
UDPServer is the server that manages all services. It calls all
services with a broadcast message, and saves their responses to a map. For
each request generates a unique code corresponding to the client. Then waits
until the timeout. After that finds the service with the lowest load and
returns it's <a href="src/data/ServerResponse.java">Server Response</a>.
```java
    /**
     * Catches incoming answers to the UDP message and saves them to the server
     */
    public void run();

     /**
     * Generates a code for the client and triggers
     * {@link #sendBroadcastMessage(String, InetAddress)}
     *
     * @param type String type of server that is being requested
     * @return code the code to search asynchronously
     */
    public String broadcast(String type) throws Exception;

     /**
     * Waits until the timeout and then returns the best available {@link Address}
     *
     * @param code          String return value of
     *                      {@link UDPServer#broadcast(String)}
     * @param timeoutSecond int
     * @return Address IP address and port values of the server
     * @throws NoResponseException When no response is received while waiting
     */
    public Address getResponseAsync(String code, int timeoutSecond) throws
NoResponseException, InterruptedException;
```



