package controller;

import model.Prize;
import model.Toy;
import service.CheckInput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ToyController {
    static final String SEPARATOR = ";";
    public static final String TOYS_IN_SHOP = "./src/main/java/db/ToysInShop.txt";
    public static final String PRIZE_TOYS = "./src/main/java/db/PrizeToysList.txt";
    public static final String TAKEN_TOYS = "./src/main/java/db/TakenToys.txt";
    static List<Toy> toys = new ArrayList<>();
    static List<Prize> prizes = new ArrayList<>();

    public static void addNewToy() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя игрушки: ");
        String name = scanner.nextLine();
        System.out.println("Введите количество игрушек: ");
        int amount = CheckInput.choice();
        System.out.println("Введите вес игрушки(шанс выпадения): ");
        int dropFrequency = CheckInput.choice();
        toys.add(new Toy(getId(), name, amount, dropFrequency));

        try (FileWriter writer = new FileWriter(TOYS_IN_SHOP, true)) {
            writer.append(String.valueOf(getId()));
            writer.append(SEPARATOR);
            writer.append(String.valueOf(name));
            writer.append(SEPARATOR);
            writer.append(String.valueOf(amount));
            writer.append(SEPARATOR);
            writer.append(String.valueOf(dropFrequency));
            writer.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static int getId() {
        int maxId = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(TOYS_IN_SHOP));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(";");
                int id = Integer.parseInt(values[0].replaceAll("\"", ""));
                if (id > maxId) {
                    maxId = id;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }

    public static List<Toy> readFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {

                String[] fields = line.split(SEPARATOR);

                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                int amount = Integer.parseInt(fields[2]);
                int dropFrequency = Integer.parseInt(fields[3]);


                Toy toy = new Toy(id, name, amount, dropFrequency);
                toys.add(toy);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return toys;
    }

    public static void changeToyFrequencyById(String fileName, int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            List<Toy> toys = new ArrayList<>();
            String line;

            boolean idFound = false;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(SEPARATOR);
                int toyId = Integer.parseInt(fields[0]);
                String name = fields[1];
                int amount = Integer.parseInt(fields[2]);
                int dropFrequency = Integer.parseInt(fields[3]);
                int changeWeight;
                if (toyId == id) {
                    System.out.println("Введите новый вес игрушки: ");
                    changeWeight = CheckInput.choice();
                    toys.add(new Toy(toyId, name, amount, changeWeight));
                    System.out.println("Вес игрушки изменён!");
                    idFound = true;
                } else {
                    toys.add(new Toy(toyId, name, amount, dropFrequency));
                }

            }
            if (!idFound) {
                System.out.println("Ошибка, нет игрушки с таким номером!");
            }

            overwriteFile(fileName, toys);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void overwriteFile(String fileName, List<Toy> toys) {
        try (FileWriter writer = new FileWriter(fileName)) {

            appendToFileLine(toys, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendToFileLine(List<Toy> toys, FileWriter writer) throws IOException {
        for (Toy toy : toys) {
            writer.append(String.valueOf(toy.getId()));
            writer.append(SEPARATOR);
            writer.append(toy.getName());
            writer.append(SEPARATOR);
            writer.append(String.valueOf(toy.getAmount()));
            writer.append(SEPARATOR);
            writer.append(String.valueOf(toy.getDropFrequency()));
            writer.append("\n");
        }

        writer.flush();
    }

    public static void removeToyFromShop(String fileName, int id) {
        boolean isToyFound = false;
        for (Toy toy : toys) {
            if (toy.getId() == id) {
                toys.remove(toy);
                System.out.println("Игрушка удалена!");
                isToyFound = true;
                break;
            }
        }
        if (!isToyFound) {
            System.out.println("Нет игрушки с таким номером!");
        }

        overwriteFile(fileName, toys);
    }

    public static void addRandomToyToPrizeFile(String fileName, String fileTwo) {
        if (toys.isEmpty()) {
            System.out.println("Ошибка: игрушек пока нет!");
            return;
        }
        Toy randomToy = getRandomToy(toys);
        assert randomToy != null;
        if (randomToy.getAmount() <= 0) {
            System.out.println("Ошибка: такой игрушки больше нет!");
            return;
        }
        randomToy.setAmount(randomToy.getAmount() - 1);
        overwriteFile(fileName, toys);
        List<Prize> prizeToys = new ArrayList<>();
        Prize prizeToy = new Prize(randomToy.getId(), randomToy.getName());
        prizeToys.add(prizeToy);
        writeToPrizeToys(fileTwo, prizeToys);
        System.out.println("Игрушка " + randomToy.getName() + " добавлена в список призовых игрушек");
    }

    public static Toy getRandomToy(List<Toy> toyList) {
        if (toyList.isEmpty()) {
            System.out.println("Ошибка: игрушек пока нет!");
            return null;
        }
        int totalWeight = 0;
        for (Toy toy : toyList) {
            totalWeight += toy.getDropFrequency();
        }
        int randomWeight = new Random().nextInt(totalWeight);
        int currentWeight = 0;
        for (Toy toy : toyList) {
            currentWeight += toy.getDropFrequency();
            if (currentWeight >= randomWeight) {
                return toy;
            }
        }
        return null;
    }

    public static void writeToPrizeToys(String fileName, List<Prize> prizes) {
        try (FileWriter writer = new FileWriter(PRIZE_TOYS, true)) {
            for (Prize prize : prizes) {
                writer.append(String.valueOf(prize.getId()));
                writer.append(SEPARATOR);
                writer.append(prize.getName());
                writer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Prize> readPrizeFile(String fileName) {
        prizes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(SEPARATOR);
                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                Prize prize = new Prize(id, name);
                prizes.add(prize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prizes;
    }

    public static void takeLastGrabbedToy() {
        List<Prize> prizeList = readPrizeFile(PRIZE_TOYS);
        if (prizeList.isEmpty()) {
            System.out.println("Ошибка: призов пока нет!");
            return;
        }

        try (FileWriter writer = new FileWriter(TAKEN_TOYS, true)) {
            writer.append(prizes.get(prizes.size() - 1).getName());
            writer.append("\n");
            prizes.remove(prizes.size() - 1);
            Files.writeString(Path.of(PRIZE_TOYS), "");
            writeToPrizeToys(PRIZE_TOYS, prizes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromFileByLines(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void wipeFile(String fileName) throws IOException {
        Files.writeString(Path.of(fileName), "");
    }

}
