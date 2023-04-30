package balbucio.datacrack.V2.lib.procbridge;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SSLServer extends Server {

	public SSLServer(int port, @NotNull IDelegate delegate) {
		super(port, delegate);
	}

	@Override
	public synchronized void start() {
		if (started) {
			throw new IllegalStateException("server already started");
		}

		SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

		final SSLServerSocket serverSocket;
		try {
			serverSocket = (SSLServerSocket) factory.createServerSocket(port);
		} catch (IOException e) {
			throw new ServerException(e);
		}
		this.serverSocket = serverSocket;

		final ExecutorService executor = Executors.newCachedThreadPool();
		this.executor = executor;
		executor.execute(() -> {
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					Connection conn = new Connection(socket, delegate);
					synchronized (SSLServer.this) {
						if (!started) {
							return; // finish listener
						}
						executor.execute(conn);
					}
				} catch (IOException ignored) {
					return; // finish listener
				}
			}
		});

		started = true;
	}

	@Override
	public synchronized void stop() {
		if (!started) {
			throw new IllegalStateException("server does not started");
		}

		executor.shutdown();
		executor = null;

		try {
			serverSocket.close();
		} catch (IOException ignored) {
		}
		serverSocket = null;

		this.started = false;
	}
}
