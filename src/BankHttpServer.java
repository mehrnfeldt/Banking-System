import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class BankHttpServer {
	private final Bank bank;
	private final CommandValidator commandValidator;
	private final CommandProcessor commandProcessor;
	private final CommandStorage commandStorage;
	private final HttpServer server;

	public BankHttpServer(int port) throws IOException {
		this.bank = new Bank();
		this.commandValidator = new CommandValidator(bank);
		this.commandProcessor = new CommandProcessor(bank);
		this.commandStorage = new CommandStorage();
		this.server = HttpServer.create(new InetSocketAddress(port), 0);
		this.server.createContext("/api/accounts", this::handleAccounts);
		this.server.createContext("/api/commands", this::handleCommands);
		this.server.createContext("/api/state", this::handleState);
	}

	public static void main(String[] args) throws IOException {
		BankHttpServer app = new BankHttpServer(8080);
		app.start();
		System.out.println("Bank API running at http://localhost:8080");
	}

	public void start() {
		server.start();
	}

	private void handleAccounts(HttpExchange exchange) throws IOException {
		addCorsHeaders(exchange);

		if ("OPTIONS".equals(exchange.getRequestMethod())) {
			send(exchange, 204, "");
			return;
		}

		if (!"GET".equals(exchange.getRequestMethod())) {
			send(exchange, 405, "{\"error\":\"Method not allowed\"}");
			return;
		}

		send(exchange, 200, accountsJson());
	}

	private void handleState(HttpExchange exchange) throws IOException {
		addCorsHeaders(exchange);

		if ("OPTIONS".equals(exchange.getRequestMethod())) {
			send(exchange, 204, "");
			return;
		}

		if (!"GET".equals(exchange.getRequestMethod())) {
			send(exchange, 405, "{\"error\":\"Method not allowed\"}");
			return;
		}

		String response = String.format(
				"{\"accounts\":%s,\"invalidCommands\":%s}",
				accountsJson(),
				stringListJson(commandStorage.getInvalidCommands()));
		send(exchange, 200, response);
	}

	private void handleCommands(HttpExchange exchange) throws IOException {
		addCorsHeaders(exchange);

		if ("OPTIONS".equals(exchange.getRequestMethod())) {
			send(exchange, 204, "");
			return;
		}

		if (!"POST".equals(exchange.getRequestMethod())) {
			send(exchange, 405, "{\"error\":\"Method not allowed\"}");
			return;
		}

		String command = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8).trim();
		if (command.startsWith("{")) {
			command = parseCommandJson(command);
		}

		boolean valid = false;
		try {
			valid = commandValidator.validate(command);
		} catch (RuntimeException ignored) {
			valid = false;
		}

		if (valid) {
			commandProcessor.process(command);
		} else {
			commandStorage.addInvalidCommand(command);
		}

		String response = String.format(
				"{\"valid\":%s,\"command\":\"%s\",\"accounts\":%s,\"invalidCommands\":%s}",
				valid,
				escapeJson(command),
				accountsJson(),
				stringListJson(commandStorage.getInvalidCommands()));
		send(exchange, valid ? 200 : 400, response);
	}

	private String parseCommandJson(String body) {
		int keyIndex = body.indexOf("\"command\"");
		if (keyIndex < 0) {
			return "";
		}

		int colonIndex = body.indexOf(":", keyIndex);
		int startQuote = body.indexOf("\"", colonIndex + 1);
		int endQuote = body.indexOf("\"", startQuote + 1);
		if (colonIndex < 0 || startQuote < 0 || endQuote < 0) {
			return "";
		}

		return body.substring(startQuote + 1, endQuote).replace("\\\"", "\"");
	}

	private String accountsJson() {
		StringBuilder json = new StringBuilder("[");
		List<Account> accounts = bank.getAccounts();

		for (int i = 0; i < accounts.size(); i++) {
			Account account = accounts.get(i);
			if (i > 0) {
				json.append(",");
			}
			json.append("{");
			json.append("\"type\":\"").append(account.getClass().getSimpleName().toLowerCase()).append("\",");
			json.append("\"accountNumber\":\"").append(escapeJson(account.getAccountNumber())).append("\",");
			json.append("\"balance\":").append(String.format(Locale.US, "%.2f", account.getBalance())).append(",");
			json.append("\"apr\":").append(String.format(Locale.US, "%.2f", account.getApr())).append(",");
			json.append("\"transactionHistory\":").append(stringListJson(account.getTransactionHistory()));
			if (account instanceof CD) {
				json.append(",\"monthsPassed\":").append(((CD) account).getMonthsPassed());
			}
			if (account instanceof Savings) {
				json.append(",\"withdrawnThisMonth\":").append(((Savings) account).hasWithdrawnThisMonth());
			}
			json.append("}");
		}

		json.append("]");
		return json.toString();
	}

	private String stringListJson(List<String> values) {
		StringBuilder json = new StringBuilder("[");
		for (int i = 0; i < values.size(); i++) {
			if (i > 0) {
				json.append(",");
			}
			json.append("\"").append(escapeJson(values.get(i))).append("\"");
		}
		json.append("]");
		return json.toString();
	}

	private String escapeJson(String value) {
		return value.replace("\\", "\\\\").replace("\"", "\\\"");
	}

	private void addCorsHeaders(HttpExchange exchange) {
		exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
		exchange.getResponseHeaders().add("Content-Type", "application/json");
	}

	private void send(HttpExchange exchange, int statusCode, String body) throws IOException {
		byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
		exchange.sendResponseHeaders(statusCode, bytes.length);
		try (OutputStream outputStream = exchange.getResponseBody()) {
			outputStream.write(bytes);
		}
	}
}
