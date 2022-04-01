package org.affluentproductions.idlepokemon.vote;

import org.affluentproductions.idlepokemon.IdlePokemon;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class VoteServer {
    private static Thread serverThread;
    private static ServerSocket serverSocket;
    private static final HashMap<Long, Socket> clients = new HashMap<>();
    private static long id_ = 0;

    public static void close(long id) {
        Socket socket = clients.get(id);
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void start() {
        if (IdlePokemon.test) return;
        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(5420);
                System.out.println("Started Vote Server on port " + serverSocket.getLocalPort());
                load();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        serverThread.setName("VoteServerThread");
        serverThread.setDaemon(true);
        serverThread.start();
    }

    private static boolean stopping = false;

    public static void stop() {
        if (IdlePokemon.test) return;
        stopping = true;
        try {
            if (serverSocket == null) return;
            for (Socket socket : clients.values()) {
                if (socket != null && !socket.isClosed()) socket.close();
            }
            if (!serverSocket.isClosed()) serverSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void load() {
        try {
            while (!serverSocket.isClosed()) {
                try {
                    final Socket socket2 = serverSocket.accept();
                    try {
                        socket2.setSoTimeout(60000);
                        id_++;
                        clients.put(id_, socket2);
                        new VoteClient(socket2, socket2.getInputStream(), id_);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex) {
                    if (stopping) return;
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex2) {
            if (stopping) return;
            ex2.printStackTrace();
        }
    }
}