package controller;

import service.CheckInput;
import view.MainView;

import java.io.IOException;
import java.util.Scanner;

import static controller.ToyController.*;

public class MainController {
    Scanner scanner = new Scanner(System.in);

    public void run() throws IOException {
        ToyController.readFromFile(TOYS_IN_SHOP);
        MainView mainView = new MainView();
        while (true) {
            mainView.showMenu();
            int menuIndex = scanner.nextInt();
            switch (menuIndex) {
                case 0 -> System.exit(0);
                case 1 -> showToysInShop();
                case 2 -> addToyToShop();
                case 3 -> changeToyFrequencyById();
                case 4 -> removeToyFromShop();
                case 5 -> tryLuckToGrabAToy();
                case 6 -> showGrabbedToys();
                case 7 -> takeLastGrabbedToy();
                case 8 -> showTakenToys();
                case 9 -> wipeAll();
                default -> {
                    System.out.println("Ошибка ввода");
                }
            }

        }
    }

    public void showToysInShop() {
        toys.clear();
        ToyController.readFromFile(TOYS_IN_SHOP);
        System.out.println(ToyController.toys);
        System.out.println("*".repeat(50));
    }

    public void addToyToShop() {
        ToyController.addNewToy();
        System.out.println("*".repeat(50));
    }

    public void changeToyFrequencyById() {
        System.out.println("Введите № игрушки для изменения веса игрушки: ");
        int idToy = CheckInput.choice();
        ToyController.changeToyFrequencyById(TOYS_IN_SHOP, idToy);
        toys.clear();
        ToyController.readFromFile(TOYS_IN_SHOP);
        System.out.println("*".repeat(50));
    }

    public void tryLuckToGrabAToy() {
        ToyController.addRandomToyToPrizeFile(TOYS_IN_SHOP, PRIZE_TOYS);
        System.out.println("*".repeat(50));
    }

    public void showGrabbedToys() {
        System.out.println(ToyController.readPrizeFile(PRIZE_TOYS));
        System.out.println("*".repeat(50));
    }

    public void takeLastGrabbedToy() {
        ToyController.takeLastGrabbedToy();
        System.out.println("*".repeat(50));
    }

    public void removeToyFromShop() {
        System.out.println("Введите № игрушки для удаления: ");
        int idToy = CheckInput.choice();
        ToyController.removeToyFromShop(TOYS_IN_SHOP, idToy);
        System.out.println("*".repeat(50));
    }

    public void showTakenToys() {
        System.out.println("Выданные игрушки:");
        ToyController.readFromFileByLines(TAKEN_TOYS);
        System.out.println("*".repeat(50));
    }

    public void wipeAll() throws IOException {
        System.out.println("Вайп");
        ToyController.wipeFile(TOYS_IN_SHOP);
        ToyController.wipeFile(PRIZE_TOYS);
        ToyController.wipeFile(TAKEN_TOYS);
        System.out.println("*".repeat(50));
    }

}


