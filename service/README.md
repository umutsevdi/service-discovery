# Service

This program takes two arguments:

- <b>--service</b>: The service that will run, <i>supports <a href="src/executor/DNSLookup.java">DNS_LOOKUP</a>,
  <a href="src/executor/DateTime.java">DATE_TIME</a>, <a href="src/executor/Encryptor.java">ENCRYPTOR</a>,
  <a href="src/executor/EvaluateExpression.java">CALCULATOR</a></i>
- <b>--port</b>: The port that the service run on

Exits when either of them are invalid.

Example:

```sh
java --jar service.jar --port 8089 --service DNS_LOOKUP
```

## UDPResponseServer

Receives UDP calls and responds them with the <a href="src/data/Address.java">Address</a>
of it's
<a href="src/GenericExecutionServer.java">GenericExecutionServer</a>.

```java
    /**
     * UDP Socket listener that responds with a TCP
     */
    public void run();

    /**
     * Formats incoming UDP Request
     *
     * @param message Message format
     * @return Formatted UDP Request
     * @throws InvalidRequestException upon receiving invalid request
     */
    public UDPRequest resolveMessage(String message) throws InvalidRequestException;

    /**
     * Generates a TCP message that contains port number and load
     * message format : "code ip:port load"
     *
     * @param request {@link UDPRequest}
     * @return message if server type matches, null if otherwise
     */
    private Optional<String> generateTCPResponse(Address address, UDPRequest request);

    /**
     * Responds incoming UDP request with given message
     *
     * @param address Address values
     * @param message Response message
     * @throws Exception Socket related exceptions
     */
    public void respondWithTCP(Address address, String message) throws Exception;

```

## GenericExecutionServer

A thread that executes given service on any request
Returns `OK` followed by the response upon success,
Returns `ERR` upon receiving <a href="src/exception/InvalidRequestException.java">InvalidRequestException</a>.

```java
    public void run();

    /**
     * getLoad returns a random number for test purposes.
     * Normally it should return how busy the service is.
     */
    public int getLoad();
```

## Executor

Executors are run in GenericExecutionServer. The executor implementations are:
- <a href="src/executor/DNSLookup.java">DNSLookup</a>
- <a href="src/executor/DateTime.java">DateTime</a>
- <a href="src/executor/Encryptor.java">Encryptor</a>
- <a href="src/executor/EvaluateExpression.java">EvaluateExpression</a>

```java
    /**
     * @param args String : Arguments received from the TCP Request
     * @return response String : Response from the class
     * @throws InvalidRequestException : When invalid arguments were entered
     */
    String execute(String args) throws InvalidRequestException;

    /**
     * @return serviceType {@link ServiceType} of the Executor implementation
     */
    ServiceType getServiceType();

```
