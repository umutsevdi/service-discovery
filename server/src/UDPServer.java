import data.TcpServer;
import enums.ServiceTypes;
import exception.TimeoutException;

import java.util.List;

public interface UDPServer {
    /*
    UDP Server has timeout
    if timeout passes throw exception
    orElse fills a list
     */
    public List<TcpServer> broadCast(ServiceTypes type) throws TimeoutException;
}
