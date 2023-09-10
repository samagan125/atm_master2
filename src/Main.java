import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AccountServiceImpl service = new AccountServiceImpl();
        Scanner scanner = new Scanner(System.in);
        boolean checkError = true;
        System.out.print("Атынызды жазыныз: ");
        String name = scanner.nextLine();
        System.out.print("фамилианы жазыныз: ");
        String lastName = scanner.nextLine();
        if (name.length() > 1 && lastName.length() > 1) {
            while (checkError) {
                service.singUp(name, lastName);
                break;
            }
        } else {
            System.out.println( "Туура эмес ысым же фамилия! Дагы байкап көрүңүз.");
            checkError = false;
        }
        if (checkError) {
            System.out.print( "Картанын номерин жазыныз: ");
            String cardNumber = scanner.nextLine();
            System.out.print("пин кодду жазыныз: ");
            String pinCode = scanner.nextLine();
            service.singIn(cardNumber, pinCode);
        }
    }
}
