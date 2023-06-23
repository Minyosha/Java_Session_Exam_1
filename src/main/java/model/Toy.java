package model;

public class Toy {
    private final int id;
    private final String name;
    private int amount;
    private int dropFrequency;

    public Toy(int id, String name, int amount, int dropFrequency) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.dropFrequency = dropFrequency;
    }

    public Toy(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDropFrequency() {
        return dropFrequency;
    }

    public void setDropFrequency(int drop) {
        this.dropFrequency = drop;
    }


    @Override
    public String toString() {
        return "\nИгрушка " +
                "№ " + id +
                ", '" + name + '\'' +
                ", количество: " + amount +
                ", шанс выпадения: " + dropFrequency + " %";
    }
}