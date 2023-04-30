package balbucio.datacrack.V2.lib.procbridge;

public final class ProtocolException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2109438689380863981L;
	static final String UNRECOGNIZED_PROTOCOL = "unrecognized protocol";
    static final String INCOMPATIBLE_VERSION = "incompatible protocol version";
    static final String INCOMPLETE_DATA = "incomplete data";
    static final String INVALID_STATUS_CODE = "invalid status code";
    static final String INVALID_BODY = "invalid body";

    ProtocolException(String message) {
        super(message);
    }

}
