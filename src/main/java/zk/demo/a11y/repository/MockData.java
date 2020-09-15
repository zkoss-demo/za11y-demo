package zk.demo.a11y.repository;

import zk.demo.a11y.domain.Address;
import zk.demo.a11y.domain.Customer;
import zk.demo.a11y.domain.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public class MockData {

    public static List<String> ingredients = asList(
            "Salami", "Cheese", "Tomato", "Ham", "Pineapple",
            "Mushrooms", "Peperoni", "Paprika", "Onion", "Corn", "Beef"
    );

    public static List<OrderItem> items = asList(
            new OrderItem("Pizza Salami", "Salami, Onion, Cheese, Tomato", new BigDecimal("10.99")),
            new OrderItem("Pizza Hawaii", "Ham, Pineapple, Cheese, Tomato", new BigDecimal("8.99")),
            new OrderItem("Pizza Funghi", "Mushrooms, Cheese, Tomato", new BigDecimal("13.99")),
            new OrderItem("Pizza Volcano", "Peperoni, Salami, Cheese, Tomato", new BigDecimal("14.99")),
            new OrderItem("Pizza Mexico", "Beef, Corn, Beans, Tomato", new BigDecimal("14.99")),
            new OrderItem("Pizza Veggie", "Paprika, Onion, Corn, Tomato", new BigDecimal("11.99"))
    );

    public static List<String> names = asList(
            "Ottmar Schmitz", "Diethelm Ziegler", "Hugo Großer", "Nikola Stumpf", "Lucia Hölzer",
            "Gero Höfler", "Moritz Knepp", "Edelgard Fleischer", "Augustin Frei", "Hannah Wirnhier",
            "Wanda Hofmeister", "Othmar Kaube", "Gregor Albert", "Gisbert Klossner", "Karl Schlosser");

    public static List<Address> addresses = asList(
            new Address("Möhrenstrasse 5", "Dormettingen", "07427 611463", "72358"),
            new Address("Landsberger Allee 61", "München", "089 877648", "80606"),
            new Address("Ufnau Strasse 57", "Füssen", "08362 898798", "87618"),
            new Address("Kantstrasse 84", "Bayreuth", "0921 550195", "95409"),
            new Address("Jahnstrasse 68", "Tuntenhausen", "08061 531054", "83102"),
            new Address("Hochstrasse 64", "Wittdün", "04682 855148", "25942"),
            new Address("Luetzowplatz 70", "Dierfeld", "06572 271138", "54533"),
            new Address("Paderborner Strasse 21", "Türkheim", "08245 987737", "86842"),
            new Address("Scharnweberstrasse 32", "Schwalbach Am Taunus", "06196 673588", "65824"),
            new Address("Billwerder Neuer Deich 71", "Nordhalben", "09267 150940", "96365"),
            new Address("Joachimstaler Str. 45", "Mörschbach", "06764 936341", "55494"),
            new Address("Neuer Jungfernstieg 49", "Geisenhausen", "08705 876526", "84142"),
            new Address("An der Schillingbrucke 25", "Hermaringen", "07322 711650", "89568"),
            new Address("Boxhagener Str. 86", "Hamburg Eilbek", "040 676068", "22089"),
            new Address("Kurfuerstendamm 88", "Ulm Weststadt", "073 136663", "89081"),
            new Address("Koepenicker Str. 94", "Oberwallmenach", "06772 251325", "56357"),
            new Address("Brandenburgische Strasse 46", "Kleinfischlingen", "06347 679985", "67483"),
            new Address("Paderborner Strasse 55", "Hiltenfingen", "08232 734215", "86856"));

    public static List<Customer> customers = IntStream.range(0, MockData.names.size())
            .mapToObj(i -> new Customer(MockData.names.get(i), MockData.addresses.get(i)))
            .collect(Collectors.toList());

}
