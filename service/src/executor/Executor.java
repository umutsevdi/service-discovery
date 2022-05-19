package executor;

import exception.InvalidRequestException;

public interface Executor {
    /**
     * execute
     *
     * @param args String : Arguments received from the TCP Request
     * @return response String : Response from the class
     * @throws InvalidRequestException : When invalid arguments were entered
     */
    String execute(String args) throws InvalidRequestException;

    /**
     * @return serviceType {@link ServiceType} of the Executor implementation
     */
    ServiceType getServiceType();

    enum ServiceType {
        CALCULATOR("calc"), DNS_LOOKUP("dns_lookup"), DATE_TIME("date"), ENCRYPTOR("encryption");

        private final String name;

        ServiceType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

}
