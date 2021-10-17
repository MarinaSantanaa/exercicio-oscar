package br.com.oscar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aplicacao {

    private final List<Oscar> oscarMale = converter(getFilepathFromResourceAsStream("oscar_age_male.csv"));
    private final List<Oscar> oscarFemale = converter(getFilepathFromResourceAsStream("oscar_age_female.csv"));
    private List<Oscar> oscars;

    public static void main(String[] args) {
        Aplicacao app = new Aplicacao();

        app.findAtorMaisJovem();
        app.findAtrizMaisPremiada();
        app.findAtrizEntreVinteETrintaAnos();
        app.findAtoresEAtrizesComMaisDeUmOscar();
        app.findResumoPorNome("Leonardo DiCaprio");
    }

    private void findAtorMaisJovem() {
        System.out.println("\nAtor mais jovem a ganhar um Oscar: ");
        oscarMale.stream()
                .min(Comparator.comparingInt(Oscar::getAge))
                .ifPresent(System.out::println);
    }

    private void findAtrizMaisPremiada() {
        System.out.println("\nAtriz que mais vezes foi premiada: ");
        oscarFemale.stream()
                .map(Oscar::getName)
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(System.out::println);
    }

    private void findAtrizEntreVinteETrintaAnos() {
        System.out.println("\nAtriz entre 20 e 30 anos que mais vezes foi premiada: ");
        oscarFemale.stream()
                .filter(age -> age.getAge() >= 20 && age.getAge() <= 30)
                .map(Oscar::getName)
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(System.out::println);
    }

    private void findAtoresEAtrizesComMaisDeUmOscar() {
        System.out.println("\nAtores e Atrizes que receberam mais de um Oscar: ");
        oscars = Stream
                .concat(oscarMale.stream(), oscarFemale.stream())
                .collect(Collectors.toList());

        oscars.stream()
                .map(Oscar::getName)
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(p -> p.getValue() > 1)
                .forEach(System.out::println);
    }

    private void findResumoPorNome(String name) {
        System.out.println("\nResumo por nome: " + name);
        oscars = Stream
                .concat(oscarMale.stream(), oscarFemale.stream())
                .collect(Collectors.toList());

        oscars.stream()
                .filter(p -> Objects.equals(p.getName(), name))
                .forEach(System.out::println);
    }

    private List<String> testeLeituraDeArquivoCsv(Path filepath) {
        try (Stream<String> lines = Files.lines(filepath)) {
            return lines
                    .skip(1)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Oscar> converter(Path filepath) {
        List<String> lines = testeLeituraDeArquivoCsv(filepath);

        List<Oscar> oscars = new ArrayList<>();
        for (String line : lines) {
            String[] split = line.split("; ");
            Oscar newOscar = new Oscar(split[0], split[1], Integer.parseInt(split[2]), split[3], split[4]);
            oscars.add(newOscar);
        }
        return oscars;
    }

    private Path getFilepathFromResourceAsStream(String fileName) {
        URL url = getClass().getClassLoader().getResource(fileName);
        File file = new File(url.getFile());
        return Path.of(file.getPath());
    }
}
