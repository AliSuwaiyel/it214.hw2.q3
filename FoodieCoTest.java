import java.util.ArrayList;
import java.util.List;

class AddOnLimitExceededException extends Exception {
    public AddOnLimitExceededException(String message) {
        super(message);
    }
}

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

interface PriceableItem {
    double getItemPrice();
}

// Abstract base class for all items
abstract class Item implements PriceableItem {
    String name;
    double basePrice;

    public Item(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    public abstract String getName();
}

// Drink Size Constants
class DrinkSizes {
    static final String REGULAR = "regular";
    static final String LARGE = "large";
    static final String SUPER_LARGE = "super-large";
}

// Drink Class
class Drink extends Item {
    private String type;
    private String size;
    private List<String> addOns;

    private static final int MAX_ADD_ONS = 3;

    // Prices for add-ons
    private static final double VANILLA_PRICE = 1.5;
    private static final double CARAMEL_PRICE = 2.0;
    private static final double HAZELNUT_PRICE = 2.5;
    private static final double WHIPPED_CREAM_PRICE = 1.0;

    public Drink(String type, double basePrice) {
        super(type, basePrice);
        this.type = type;
        this.size = DrinkSizes.REGULAR;
        this.addOns = new ArrayList<>();
    }

    public void setDrinkSize(String size) {
        switch (size.toLowerCase()) {
            case DrinkSizes.REGULAR:
                this.size = DrinkSizes.REGULAR;
                break;
            case DrinkSizes.LARGE:
                this.size = DrinkSizes.LARGE;
                break;
            case DrinkSizes.SUPER_LARGE:
                this.size = DrinkSizes.SUPER_LARGE;
                break;
            default:
                throw new IllegalArgumentException("Invalid drink size");
        }
    }

    public void addAddOn(String addOn) throws AddOnLimitExceededException, InvalidInputException {
        if (addOns.size() >= MAX_ADD_ONS) {
            throw new AddOnLimitExceededException("Maximum of 3 add-ons exceeded");
        }

        switch (addOn.toLowerCase()) {
            case "vanilla":
            case "caramel":
            case "hazelnut":
            case "whipped cream":
                addOns.add(addOn.toLowerCase());
                break;
            default:
                throw new InvalidInputException("Invalid add-on");
        }
    }

    @Override
    public double getItemPrice() {
        double price = basePrice;

        if (size.equals(DrinkSizes.LARGE)) {

        } else if (size.equals(DrinkSizes.SUPER_LARGE)) {
            price *= 1.10;
        }

        // Add-ons price
        for (String addOn : addOns) {
            price += getAddOnPrice(addOn);
        }

        return price;
    }

    private double getAddOnPrice(String addOn) {
        switch (addOn.toLowerCase()) {
            case "vanilla":
                return VANILLA_PRICE;
            case "caramel":
                return CARAMEL_PRICE;
            case "hazelnut":
                return HAZELNUT_PRICE;
            case "whipped cream":
                return WHIPPED_CREAM_PRICE;

            default:
                return 0.0;
        }
    }

    public String getName() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public List<String> getAddOns() {
        return addOns;
    }
}

// Sandwich Class
class Sandwich extends Item {
    public Sandwich(String name, double price) {
        super(name, price);
    }

    public double getItemPrice() {
        return basePrice;
    }

    public String getName() {
        return name;
    }
}

class Customer {

    private String name;
    private List<Item> orderItems;

    public Customer(String name) {
        this.name = name;
        this.orderItems = new ArrayList<>();
    }

    public void addItem(Item item) {
        orderItems.add(item);
    }

    public List<Item> getOrderItems() {
        return orderItems;
    }

    @Override
    public String toString() {
        double total = 0;
        String itemList = "";

        for (Item item : orderItems) {
            if (!itemList.isEmpty())
                itemList += ", ";

            if (item instanceof Drink) {
                Drink drink = (Drink) item;
                itemList += drink.getSize() + " " + drink.getName();
            } else {
                itemList += item.getName();
            }

            total += item.getItemPrice();
        }

        return "The order for " + name + " is: " + itemList + "\ntotal price: " + total + " SAR";
    }
}

class FoodieCoVendingMachine {

    private List<Customer> customers;

    public FoodieCoVendingMachine() {
        this.customers = new ArrayList<>();
    }

    public void makeOrder(Customer customer, List<Item> itemList) {
        for (Item item : itemList) {
            customer.addItem(item);
        }
        customers.add(customer);
    }

    public double generateInvoice(Customer customer, double discountPercentage) {
        double totalPrice = 0;

        for (Item item : customer.getOrderItems()) {
            totalPrice += item.getItemPrice();
        }

        // Apply discount if applicable
        return totalPrice * (1 - (discountPercentage / 100.0));
    }
}

// Test Class
public class FoodieCoTest {
    public static void main(String[] args) {
        try {

            FoodieCoVendingMachine vendingMachine = new FoodieCoVendingMachine();

            Customer Ali = new Customer("Ali");

            Drink latte = new Drink("Latte", 14.0);
            latte.setDrinkSize(DrinkSizes.LARGE);
            latte.addAddOn("vanilla");
            latte.addAddOn("hazelnut");
            latte.addAddOn("whipped cream");

            Sandwich chickenSandwich = new Sandwich("Chicken", 5.0);

            // Make order
            List<Item> orderItems = new ArrayList<>();
            orderItems.add(latte);
            orderItems.add(chickenSandwich);

            vendingMachine.makeOrder(Ali, orderItems);

            double finalBill = vendingMachine.generateInvoice(Ali, 0);

            System.out.println(Ali.toString());
            System.out.println("Final Bill: " + finalBill + " SAR");

            System.out.println("\n\n SECOND ORDER: \n");
            Customer Abdulaziz = new Customer("Abdulaziz");

            Sandwich beefSandwich = new Sandwich("Beef", 6.0);

            orderItems = new ArrayList<>();
            orderItems.add(beefSandwich);
            vendingMachine.makeOrder(Abdulaziz, orderItems);

            finalBill = vendingMachine.generateInvoice(Abdulaziz, 5);

            System.out.println(Abdulaziz.toString());
            System.out.println("Final Bill: " + finalBill + " SAR");

        } catch (AddOnLimitExceededException | InvalidInputException e) {
            e.printStackTrace();
        }
    }
}