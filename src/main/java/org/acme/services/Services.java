package org.acme.services;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Random;

@ApplicationScoped
public class Services {

    @ConfigProperty(name = "slash.http-port", defaultValue = "9000")
    int slashPort;

    @ConfigProperty(name = "slash.delay-in-ms", defaultValue = "100")
    int slashDelay;

    @ConfigProperty(name = "hendrix.http-port", defaultValue = "9001")
    int hendrixPort;

    @ConfigProperty(name = "hendrix.delay-in-ms", defaultValue = "500")
    int hendrixDelay;

    @ConfigProperty(name = "eddie.http-port", defaultValue = "9002")
    int eddiePort;

    @ConfigProperty(name = "eddie.failure-ratio", defaultValue = "20")
    int eddieFailureRatio;

    public void init(@Observes StartupEvent ev, Vertx vertx, Logger logger) throws IOException {
        var jHendrix = Files.readAllBytes(new File("/Users/auri/Pictures/2023-DevoxxUk/JimmyHendrix.jpg").toPath());
        var jEddie = Files.readAllBytes(new File("/Users/auri/Pictures/2023-DevoxxUk/EddieVanHalen.jpg").toPath());
        var jSlash = Files.readAllBytes(new File("/Users/auri/Pictures/2023-DevoxxUk/Slash.jpg").toPath());
        var jQuarkus = Files.readAllBytes(new File("/Users/auri/Pictures/2023-DevoxxUk/quarkus.jpg").toPath());
        Random random = new Random();
        vertx.createHttpServer()
                .requestHandler(req -> {
                    vertx.setTimer(slashDelay, x -> {
                        req.response().endAndForget(Base64.getEncoder().encodeToString(jEddie));
                    });
                })
                .listenAndAwait(slashPort);

        vertx.createHttpServer()
                .requestHandler(req -> {
                    vertx.setTimer(hendrixDelay, x -> {
                        req.response().endAndForget(Base64.getEncoder().encodeToString(jSlash));
//                        req.response().endAndForget("🟡");
                    });
                })
                .listenAndAwait(hendrixPort);

        vertx.createHttpServer()
                .requestHandler(req -> {
                    vertx.setTimer(5, x -> {
                        if (random.nextInt(100) > (100 - eddieFailureRatio)) {
                            req.response().endAndForget(Base64.getEncoder().encodeToString(jHendrix));
//                            req.response().endAndForget("❌");
                        } else {
                            req.response().endAndForget(Base64.getEncoder().encodeToString(jQuarkus));
//                            req.response().endAndForget("🟢");
                        }
                    });
                })
                .listenAndAwait(eddiePort);

        logger.infof("""
                Services Started:
                    - 🔵 Blue -> port: %d, delay: %dms
                    - 🟡 Yellow -> port: %d, delay: %dms
                    - 🟢 Green -> %d, failure ratio: %s
                """, slashPort, slashDelay, hendrixPort, hendrixDelay, eddiePort, eddieFailureRatio + "%");
    }


}
