package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.ProfilesRepository;
import org.example.model.Clan;
import org.example.repository.impl.DatabaseClanRepositoryImpl;
import org.example.repository.impl.InMemoryClanRepositoryImpl;
import org.example.service.ClanService;
import org.example.service.TaskService;
import org.example.service.UserService;
import org.example.service.impl.ClanServiceImpl;
import org.example.service.impl.TaskServiceImpl;
import org.example.service.impl.UserServiceImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        log.info("main started");
        Properties properties = getProperties();
        int serverPort = Integer.parseInt(properties.getProperty("server.port"));
        String profileRepository = properties.getProperty("repository.profile");
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        ClanService clanService = getClanServiceImpl(profileRepository);
        createContextForClan(server, profileRepository, clanService);
        createContextAddMoney(server, clanService);

        server.setExecutor(null);
        server.start();
        System.out.println("Server started");
    }

    private static void createContextAddMoney(HttpServer server, ClanService clanService) {
        log.info("try create context for ADD MONEY");

        server.createContext("/api/testmoney", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                TaskService taskService = new TaskServiceImpl(clanService);
                UserService userService = new UserServiceImpl(clanService);
                for (int i = 0; i < 50; i++) {
                    Runnable r = ()->{
                        taskService.completeTask(1);
                    };
                    Thread myThread = new Thread(r);
                    myThread.start();

                    Runnable r2 = ()->{
                        userService.addGoldForClan(1,1);
                    };
                    Thread myThread2 = new Thread(r2);
                    myThread2.start();
                }

                exchange.sendResponseHeaders(201, 0);
                OutputStream output = exchange.getResponseBody();
                output.write("".getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        log.info("created context for ADD MONEY");

    }

    static Properties getProperties() {
        log.info("try get properties");

        String appConfigPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "application.properties";
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
            log.info("properties found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return appProps;
    }

    ;

    private static void createContextForClan(HttpServer server, String profileRepository, ClanService clanService) {
        log.info("try create context for CLAN");
        ObjectMapper mapper = new ObjectMapper();
        server.createContext("/api/clans", (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                String respText;
                if (haveParam(exchange)) {
                    long idClan = Long.parseLong(exchange.getRequestURI().toString().substring(11));
                    respText = mapper.writeValueAsString(clanService.getClanById(idClan));
                } else {
                    respText = mapper.writeValueAsString(clanService.getAll());
                }
                exchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else if ("POST".equals(exchange.getRequestMethod())) {
                String s = new String(exchange.getRequestBody().readAllBytes());
                Clan clan = mapper.readValue(s, Clan.class);
                String respText = mapper.writeValueAsString(clanService.save(clan));
                exchange.sendResponseHeaders(201, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                long idClan = Long.parseLong(exchange.getRequestURI().toString().substring(11));
                clanService.deleteClanById(idClan);
                exchange.sendResponseHeaders(201, 0);
                OutputStream output = exchange.getResponseBody();
                output.write("".getBytes());
                output.flush();
            } else if ("PUT".equals(exchange.getRequestMethod())) {
                String s = new String(exchange.getRequestBody().readAllBytes());
                Clan clan = mapper.readValue(s, Clan.class);
                String respText = mapper.writeValueAsString(clanService.save(clan));
                exchange.sendResponseHeaders(201, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        log.info("created context for CLAN");
    }

    private static ClanService getClanServiceImpl(String profileRepository) {
        if (profileRepository.equals(ProfilesRepository.MEMORY.toString())) {
            return new ClanServiceImpl(new InMemoryClanRepositoryImpl());
        } else if (profileRepository.equals(ProfilesRepository.DATABASE.toString())) {
            return new ClanServiceImpl(new DatabaseClanRepositoryImpl());
        }
        throw new RuntimeException("Incorrect repository profile");
    }

    static boolean haveParam(HttpExchange exchange) {
        return exchange.getRequestURI().toString().substring(10).length() != 0;
    }
}