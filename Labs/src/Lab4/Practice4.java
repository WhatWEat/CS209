package Lab4;

import java.io.IOException;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Practice4 {

    public static class City {

        private String name;
        private String state;
        private int population;

        public City(String name, String state, int population) {
            this.name = name;
            this.state = state;
            this.population = population;
        }
        public City(int population){
            population = -1;
        }
        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }

        public int getPopulation() {
            return population;
        }

        @Override
        public String toString() {
            if(population == -1) return "";
            return "City{" + "name='" + name + '\'' + ", state='" + state + '\'' + ", population="
                + population + '}';
        }
    }

    public static Stream<City> readCities(String filename) throws IOException {
        return Files.lines(Paths.get(filename)).map(l -> l.split(", "))
            .map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
    }

    @SuppressWarnings({"checkstyle:WhitespaceAfter", "checkstyle:NeedBraces"})
    public static void main(String[] args) throws IOException, URISyntaxException {

        Stream<City> cities = readCities("cities.txt");
        // Q1: count how many cities there are for each state
        // TODO: Map<String, Long> cityCountPerState = ...
        System.out.println(
            cities.collect(Collectors.groupingBy(City::getState, Collectors.counting())));
        cities = readCities("cities.txt");
        // Q2: count the total population for each state
        // TODO: Map<String, Integer> statePopulation = ...
        System.out.println(cities.collect(
            Collectors.groupingBy(City::getState, Collectors.summingLong(City::getPopulation))));
        cities = readCities("cities.txt");
        // Q3: for each state, get the set of cities with >500,000 population
        Map<String, Set<City>> state = cities.collect(Collectors.groupingBy(City::getState, Collectors.toSet()));
        state.forEach((a,b) -> {
            System.out.print(a+"[");
            b.forEach(c -> {
                if (c.population > 500000) {
                    System.out.print(c);
                    System.out.print(", ");
                }
            }
            );
            System.out.println("]");
        });
        // TODO: Map<String, Set<City>> largeCitiesByState = ...

    }

}
