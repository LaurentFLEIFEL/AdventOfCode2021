package com.lfl.advent2021.days.day12;

import com.lfl.advent2021.LinesConsumer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class Day12 implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        Map<String, Set<String>> adjacentsByCave = buildAdjacents(lines);

        Set<MutableList<String>> paths = computePaths(adjacentsByCave);

        log.info("Size = {}", paths.size());
    }

    private Set<MutableList<String>> computePaths(Map<String, Set<String>> adjacentsByCave) {
        Set<MutableList<String>> paths = Sets.mutable.empty();
        Set<MutableList<String>> paths2 = Sets.mutable.<MutableList<String>>empty()
                                                      .with(Lists.mutable.of("start"));

        while (!paths.equals(paths2)) {
            paths = paths2;
            paths2 = Sets.mutable.empty();
            for (MutableList<String> path : paths) {
                String last = path.getLast();
                if ("end".equals(last)) {
                    paths2.add(path);
                    continue;
                }

                adjacentsByCave.get(last)
                               .stream()
                               .filter(cave -> filterPart2(path, cave))
                               .map(cave -> Lists.mutable.withAll(path).with(cave))
                               .forEach(paths2::add);
            }
        }
        return paths;
    }

    private Map<String, Set<String>> buildAdjacents(List<String> lines) {
        Map<String, Set<String>> adjacentsByCave = Maps.mutable.empty();

        lines.stream()
             .map(line -> line.split("-"))
             .forEach(split -> {
                 adjacentsByCave.computeIfAbsent(split[0], k -> Sets.mutable.empty()).add(split[1]);
                 adjacentsByCave.computeIfAbsent(split[1], k -> Sets.mutable.empty()).add(split[0]);
             });
        return adjacentsByCave;
    }

    private boolean filterPart1(MutableList<String> path, String cave) {
        return cave.toUpperCase().equals(cave) || !path.contains(cave);
    }

    private boolean filterPart2(MutableList<String> path, String cave) {
        if (cave.toUpperCase().equals(cave)) {
            return true;
        }
        if ("start".equals(cave)) {
            return false;
        }

        Map<String, Integer> frequencies = Maps.mutable.empty();
        path.stream()
            .filter(cave2 -> cave2.toLowerCase().equals(cave2))
            .forEach(cave2 -> frequencies.merge(cave2, 1, Integer::sum));
        if (frequencies.containsValue(2)) {
            return !path.contains(cave);
        }
        return true;
    }
}
