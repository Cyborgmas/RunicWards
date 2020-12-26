package com.cyborgmas.runic_wards;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class RunicWardsConfig {
    private static final Marker MARKER = MarkerManager.getMarker("RUNIC_WARDS_CONFIG_VERIFIER");
    private static final Logger LOGGER = LogManager.getLogger();

    private static class Client {
        private Client(ForgeConfigSpec.Builder builder) {

        }
    }

    private static class Server {
        private Server(ForgeConfigSpec.Builder builder) {

        }
    }

    private static final ForgeConfigSpec CLIENT_SPEC;
    private static final ForgeConfigSpec SERVER_SPEC;
    private static final Client CLIENT;
    private static final Server SERVER;

    static {
        Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
        Pair<Server, ForgeConfigSpec> server = new ForgeConfigSpec.Builder().configure(Server::new);
        CLIENT_SPEC = client.getRight();
        SERVER_SPEC = server.getRight();
        CLIENT = client.getLeft();
        SERVER = server.getLeft();
    }

    public static Client getClientConfig() {
        return CLIENT;
    }

    public static Server getServerConfig() {
        return SERVER;
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }
}
