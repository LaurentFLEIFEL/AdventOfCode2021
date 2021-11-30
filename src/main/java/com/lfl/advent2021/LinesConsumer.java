package com.lfl.advent2021;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public interface LinesConsumer {

    void consume(List<String> lines);

    @SuppressWarnings("ConstantConditions")
    static List<String> readAllInput(String input) throws URISyntaxException, IOException {
        URI resource = LinesConsumer.class.getClassLoader().getResource("input/" + input).toURI();
        return Files.readAllLines(Paths.get(resource));
    }
}
