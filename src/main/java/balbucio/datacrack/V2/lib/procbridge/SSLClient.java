package balbucio.datacrack.V2.lib.procbridge;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SSLClient extends Client {

	public SSLClient(@NotNull String host, int port, long timeout, @Nullable Executor executor) {
		super(host, port, timeout, executor);
	}

	public SSLClient(@NotNull String host, int port) {
		this(host, port, FOREVER, null);
	}

	@Override
	public @Nullable Object request(@Nullable String method, @Nullable Object payload) throws ClientException, TimeoutException, ServerException {
		final StatusCode[] respStatusCode = {null};
		final Object[] respPayload = {null};
		final Throwable[] innerException = {null};
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		try (final SSLSocket socket = (SSLSocket) factory.createSocket(getHost(), getPort())) {
			Runnable task = () -> {
				try (OutputStream os = socket.getOutputStream();
					 InputStream is = socket.getInputStream()) {

					Protocol.writeRequest(os, method, payload);
					Map.Entry<StatusCode, Object> entry = Protocol.readResponse(is);
					respStatusCode[0] = entry.getKey();
					respPayload[0] = entry.getValue();

				} catch (Exception ex) {
					innerException[0] = ex;
				}
			};

			if (getTimeout() <= 0) {
				task.run();
			} else {
				TimeoutExecutor guard = new TimeoutExecutor(getTimeout(), getExecutor());
				guard.execute(task);
			}
		} catch (IOException ex) {
			throw new ClientException(ex);
		}

		if (innerException[0] != null) {
			throw new RuntimeException(innerException[0]);
		}

		if (respStatusCode[0] != StatusCode.GOOD_RESPONSE) {
			throw new ServerException((String) respPayload[0]);
		}

		return respPayload[0];
	}
}
