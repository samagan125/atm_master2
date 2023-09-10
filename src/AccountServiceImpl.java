import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class AccountServiceImpl extends Exception implements AccountService {
    private final AccountDao accountDao = new AccountDao();
    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);
    private final Scanner scannerInt = new Scanner(System.in);
    private boolean checkError = true;

    public enum Color {
        ANSI_RESET("\u001B[0m"),
        ANSI_BLACK("\u001B[30m"),
        ANSI_RED("\u001B[31m"),
        ANSI_GREEN("\u001B[32m"),
        ANSI_YELLOW("\u001B[33m"),
        ANSI_BLUE("\u001B[34m"),
        ANSI_PURPLE("\u001B[35m"),
        ANSI_CYAN("\u001B[36m"),
        ANSI_WHITE("\u001B[37m");
        private final String color;

        Color(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return color;
        }
    }

    @Override
    public void singUp(String name, String lastName) {
        ArrayList<UserAccount> users = new ArrayList<>();
        int num;
        int code;
        num = random.nextInt(10000000, 99999999);
        code = random.nextInt(1000, 9999);
        String cardNumber = Integer.toString(num);
        String cardPinCode = Integer.toString(code);
        System.out.println(Color.ANSI_YELLOW + "картаныздын номери: " + cardNumber + "\nсиздин пин кодунуз: " + cardPinCode + Color.ANSI_RESET);
        users.add(new UserAccount("Husein", "Obama", "00000000", "0000", 0));
        users.add(new UserAccount(name, lastName, cardNumber, cardPinCode, 0));
        accountDao.setUserAccounts(users);
    }


    @Override
    public void singIn(String cardNumber, String pinCode) {
        UserAccount[] userAccounts = new UserAccount[1];
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            try {
                if (userAccount.getCardNumber().equals(cardNumber) && userAccount.getPinCode().equals(pinCode)) {
                    userAccounts[0] = userAccount;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                userAccounts[0] = null;
                System.out.println(Color.ANSI_RED + "Жараксыз пин картанын коду же номери!!!");
                checkError = false;
            }
        }
        do {
            if (userAccounts[0] != null) {
                System.out.println(Color.ANSI_YELLOW + "Просмотреть баланс {1}~" +
                        "\n депозит алу{2}~" +
                        "\nдосунузга акча жоноту {3}~" +
                        "\nакча алу {4}~" +
                        "\n баланс толтуру{5}~");
                System.out.print(Color.ANSI_CYAN + "канча акча алас: ");
                int action = scannerInt.nextInt();
                switch (action) {
                    case 1 -> {
                        System.out.print(Color.ANSI_CYAN + "картанын номерн жазыныз: ");
                        String cardNumber1 = scanner.nextLine();
                        System.out.print("пин кодду жазыныз: ");
                        String pinCode1 = scanner.nextLine();
                        balance(cardNumber1, pinCode1);
                    }
                    case 2 -> {
                        System.out.print(Color.ANSI_CYAN + "картанын номерин жазыныз: ");
                        String cardNumber2 = scanner.nextLine();
                        System.out.print("пин кодду жазыныз: ");
                        String pinCode2 = scanner.nextLine();
                        deposit(cardNumber2, pinCode2);
                    }
                    case 3 -> {
                        System.out.print(Color.ANSI_CYAN + "Введите номер карты друга: ");
                        String friendCardNumber = scanner.nextLine();
                        sendToFriend(friendCardNumber);
                    }
                    case 4 -> {
                        System.out.print(Color.ANSI_CYAN + "Введите сумму которую хотите снять: ");
                        int amount = scannerInt.nextInt();
                        withdrawMoney(amount);
                    }
                    case 5 -> {
                        System.out.print(Color.ANSI_CYAN + "Введите сумму которую хотите внести в счёт: ");
                        int amount = scannerInt.nextInt();
                        balance(amount);
                    }
                }
            } else {
                System.out.println(Color.ANSI_RED + "Неверный пин код или номер карты!!!");
                checkError = false;
            }
        } while (checkError);
    }

    @Override
    public void balance(String cardNumber, String pinCode) {
        UserAccount[] userAccounts = new UserAccount[1];
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            try {
                if (userAccount.getCardNumber().equals(cardNumber) && userAccount.getPinCode().equals(pinCode)) {
                    userAccounts[0] = userAccount;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                userAccounts[0] = null;
                System.out.println(Color.ANSI_RED + "Неверный пин код или номер карты!!!");
                checkError = false;
            }
        }

        if (userAccounts[0] != null) {
            System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccounts[0].getBalance());
        } else {
            System.out.println(Color.ANSI_RED + "Неверный пин код или номер карты!!!");
            checkError = false;
        }
    }

    @Override
    public void deposit(String cardNumber, String pinCode) {
        UserAccount[] userAccounts = new UserAccount[1];
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            try {
                if (userAccount.getCardNumber().equals(cardNumber) && userAccount.getPinCode().equals(pinCode)) {
                    userAccounts[0] = userAccount;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                userAccounts[0] = null;
                System.out.println(Color.ANSI_RED + "Неверный пин код или номер карты!!!");
                checkError = false;
            }
        }

        if (userAccounts[0] != null) {
            System.out.print("Введите сумму депозита для пополнения счёта: ");
            int depositMoney = scannerInt.nextInt();
            userAccounts[0].setBalance(userAccounts[0].getBalance() + depositMoney);
            System.out.println(Color.ANSI_YELLOW + "Вы успешно взяли депозит!" + Color.ANSI_BLUE + "\nВаш баланс : " + userAccounts[0].getBalance());
        } else {
            System.out.println(Color.ANSI_RED + "Неверный пин код или номер карты!!!");
            checkError = false;
        }

    }

    @Override
    public void sendToFriend(String friendCardNumber) {
        UserAccount[] userAccounts = new UserAccount[1];
        UserAccount[] friend = new UserAccount[1];
        System.out.print(Color.ANSI_CYAN + "Введите номер карты: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Введите пин код карты: ");
        String pinCode = scanner.nextLine();
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            try {
                if (userAccount.getCardNumber().equals(friendCardNumber)) {
                    friend[0] = userAccount;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                friend[0] = null;
                System.out.println(Color.ANSI_RED + "Неверный пин код или номер карты!!!");
                checkError = false;
            }
        }
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            try {
                if (userAccount.getCardNumber().equals(cardNumber) && userAccount.getPinCode().equals(pinCode)) {
                    userAccounts[0] = userAccount;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                userAccounts[0] = null;
                System.out.println(Color.ANSI_RED + "Неверный пин код или номер карты!!!");
                checkError = false;
            }
        }
        if (userAccounts[0] != null) {
            System.out.print(Color.ANSI_CYAN + "Введите сумму для перечисления: ");
            int amount = scannerInt.nextInt();
            if (userAccounts[0].getBalance() >= amount) {
                userAccounts[0].setBalance(userAccounts[0].getBalance() - amount);
                System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccounts[0].getBalance());
            } else {
                System.out.println(Color.ANSI_RED + "Недостаточно средств на карте!!!");
            }
            for (UserAccount userFriend : friend) {
                if (userFriend != null && userAccounts[0].getBalance() >= amount) {
                    userFriend.setBalance(userFriend.getBalance() + amount);
                    System.out.println(Color.ANSI_BLUE + "Баланс вашего друга: " + userFriend.getBalance());
                }
            }
        }
    }

    @Override
    public void withdrawMoney(int amount) {
        UserAccount[] userAccounts = new UserAccount[1];
        System.out.print(Color.ANSI_CYAN + "Введите номер карты: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Введите пин код карты: ");
        String pinCode = scanner.nextLine();
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            try {
                if (userAccount.getCardNumber().equals(cardNumber) && userAccount.getPinCode().equals(pinCode)) {
                    userAccounts[0] = userAccount;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                userAccounts[0] = null;
                System.out.println(Color.ANSI_RED + "Неверный пин код или номер карты!!!");
                checkError = false;
            }
        }
        if (userAccounts[0] != null) {
            if (userAccounts[0].getBalance() >= amount) {
                if (amount % 1000 == 0) {
                    System.out.println(Color.ANSI_YELLOW + "Вывести " + amount + " рублей по 1000 рублей - " + amount / 1000 + " (shtuk) [1]");
                }
                if (amount % 500 == 0) {
                    System.out.println("Вывести " + amount + " рублей по 500 рублей - " + amount / 500 + " (shtuk) [2]");
                }
                if (amount % 200 == 0) {
                    System.out.println("Вывести " + amount + " рублей по 200 рублей - " + amount / 200 + " (shtuk) [3]");
                }
                if (amount % 100 == 0) {
                    System.out.println("Вывести " + amount + " рублей по 100 рублей - " + amount / 100 + " (shtuk) [4]");
                }
                if (amount % 50 == 0) {
                    System.out.println("Вывести " + amount + " рублей по 50 рублей - " + amount / 50 + " (shtuk) [5]");
                }
                if (amount % 10 == 0) {
                    System.out.println("Вывести " + amount + " рублей по 10 рублей - " + amount / 10 + " (shtuk) [6]");
                }
                System.out.print(Color.ANSI_CYAN + "Выберите способ обналичивания денег: ");
                int action = scannerInt.nextInt();
                switch (action) {
                    case 1 -> {
                        if (amount % 1000 == 0) {
                            userAccounts[0].setBalance(userAccounts[0].getBalance() - amount);
                            System.out.println(Color.ANSI_YELLOW + "Выведено " + action + " рублей по 1000 рублей - " + amount / 1000 + " (shtuk)");
                            System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccounts[0].getBalance());
                        }
                    }
                    case 2 -> {
                        if (amount % 500 == 0) {
                            userAccounts[0].setBalance(userAccounts[0].getBalance() - amount);
                            System.out.println(Color.ANSI_YELLOW + "Выведенно " + amount + " рублей по 500 рублей - " + amount / 500 + " (shtuk)");
                            System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccounts[0].getBalance());
                        }
                    }
                    case 3 -> {
                        if (amount % 200 == 0) {
                            userAccounts[0].setBalance(userAccounts[0].getBalance() - amount);
                            System.out.println(Color.ANSI_YELLOW + "Выведенно " + amount + " рублей по 200 рублей - " + amount / 200 + " (shtuk)");
                            System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccounts[0].getBalance());
                        }
                    }
                    case 4 -> {
                        if (amount % 100 == 0) {
                            userAccounts[0].setBalance(userAccounts[0].getBalance() - amount);
                            System.out.println(Color.ANSI_YELLOW + "Выведенно " + amount + " рублей по 100 рублей - " + amount / 100 + " (shtuk)");
                            System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccounts[0].getBalance());
                        }
                    }
                    case 5 -> {
                        if (amount % 50 == 0) {
                            userAccounts[0].setBalance(userAccounts[0].getBalance() - amount);
                            System.out.println(Color.ANSI_YELLOW + "Выведенно " + amount + " рублей по 50 рублей - " + amount / 50 + " (shtuk)");
                            System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccounts[0].getBalance());
                        }
                    }
                    case 6 -> {
                        if (amount % 10 == 0) {
                            userAccounts[0].setBalance(userAccounts[0].getBalance() - amount);
                            System.out.println(Color.ANSI_YELLOW + "Выведенно " + amount + " рублей по 10 рублей - " + amount / 10 + " (shtuk)");
                            System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccounts[0].getBalance());
                        }
                    }
                }
            } else {
                System.out.println(Color.ANSI_RED + "Недостаточно средств на карте!!!");
                checkError = false;
            }
        } else {
            System.out.println(Color.ANSI_RED + "Неверный пин код или номер карты!!!");
            checkError = false;
        }
    }

    @Override
    public void balance(int amount) {
        UserAccount[] userAccounts = new UserAccount[1];
        System.out.print(Color.ANSI_CYAN + "Ведите номер карты: ");
        String cardNumber = scanner.nextLine();
        System.out.print(Color.ANSI_CYAN + "Введите пин код карты: ");
        String pinCode = scanner.nextLine();
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            try {
                if (userAccount.getCardNumber().equals(cardNumber) && userAccount.getPinCode().equals(pinCode)) {
                    userAccounts[0] = userAccount;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                userAccounts[0] = null;
                System.out.println(Color.ANSI_RED + "Неверный пароль или номер карты!!!");
                checkError = false;
            }
        }
        if (userAccounts[0] != null) {
            userAccounts[0].setBalance(userAccounts[0].getBalance() + amount);
            System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccounts[0].getBalance());
        } else {
            System.out.println(Color.ANSI_RED + "Неверный пин код или номер карты!!!");
            checkError = false;
        }
    }
}
