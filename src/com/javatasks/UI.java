package com.javatasks;

import com.javatasks.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UI {

    private static final String ERROR_INPUT_MESSAGE = "Incorrect value. Try again: ";

    static void startMenu() {
        int buffer;

        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("1) Add user");
            System.out.println("2) Remove user(s)");
            System.out.println("3) Change user");
            System.out.println("4) Show users");
            System.out.println("5) Sort users");
            System.out.println("6) Filter users");
            System.out.println("7) Import");
            System.out.println("8) Export");
            System.out.println("0) Exit");
            System.out.print("Enter a number: ");


            buffer = inputInt(in);

            switch (buffer) {
                case 1:
                    add(in);
                    break;
                case 2:
                    remove(in);
                    break;
                case 3:
                    change(in);
                    break;
                case 4:
                    show();
                    break;
                case 5:
                    sort(in);
                    break;
                case 6:
                    filter(in);
                    break;
                case 7:
                    importData(in);
                    break;
                case 8:
                    exportData(in);
                    break;
                case 0:
                    return;
                default:
                    System.out.println(ERROR_INPUT_MESSAGE);
            }

        }
    }

    private static void exportData(Scanner in) {
        System.out.println("Input filename: ");
        in.nextLine();
        String file = in.nextLine();
        try {
            DBUtils.exportData(file + ".csv");
        } catch (SQLException e) {
            System.err.println("Error export!");
            System.err.println(e);
        }
    }

    private static void importData(Scanner in) {
        System.out.println("Input filename:");
        in.nextLine();
        String file = in.nextLine();
        try {
            DBUtils.importData(file + ".csv");
        } catch (SQLException e) {
            System.err.println("Error import!");
        }
    }


    private static void change(Scanner in) {
        try {
            DBUtils.getUsers();
            System.out.println("Choose User:");
            int id = inputInt(in);
            User user = DBUtils.getUser(id);
            if (user == null) {
                System.out.println("User with id " + id + " not found!");
            } else {
                changeUser(user);
                DBUtils.updateUser(id, user);
            }
        } catch (SQLException e) {
            System.err.println("Error change!");
        }
    }

    private static void remove(Scanner in) {

        System.out.println("Enter count of deleting users: ");

        int count = inputInt(in);

        System.out.println("Enter id(s) of the product via space: ");

        System.out.println("Input " + count + " ids");
        try {
            for (int i = 0; i < count; i++) {
                DBUtils.deleteUser(inputInt(in));
            }
        } catch (SQLException e) {
            System.err.println("Error remove!");
        }
    }

    private static void add(Scanner in) {
        System.out.print("Name: ");
        String name = in.next();

        System.out.print("Surname: ");
        String surname = in.next();

        System.out.print("Login: ");
        String login = in.next();

        System.out.print("E-mail: ");
        String email = in.next();

        while (!CheckEmail(email)) {
            System.out.println(ERROR_INPUT_MESSAGE);
            email = in.nextLine();
        }
        try {
            DBUtils.addUser(new User(name, surname, login, email));
        } catch (SQLException e) {
            System.err.println("Error add!");
        }
    }

    private static void show() {
        try {
            printUsers(DBUtils.getUsers());
        } catch (SQLException e) {
            System.err.println("Error show!");
            System.err.println(e);
        }
    }

    private static void filter(Scanner in) {
        System.out.println("Filter by:");
        System.out.println("1 - Name");
        System.out.println("2 - Surname");
        int command = inputInt(in);
        while (command > 2 || command < 1) {
            System.out.println("Repeat input");
            System.out.println("1 - name, 2 - surname:");
            command = inputInt(in);
        }

        System.out.print("Enter mask: ");
        String mask = in.next();
        try {
            printUsers(DBUtils.filterUsers(command, mask));
        } catch (SQLException e) {
            System.err.println("Error filter!");
        }
    }

    private static void sort(Scanner in) {
        System.out.println("Sort by:");
        System.out.println("1 - Name");
        System.out.println("2 - Surname");
        int command = inputInt(in);
        while (command > 2 || command < 1) {
            System.out.println("Repeat input");
            System.out.println("1 - name, 2 - surname:");
            command = inputInt(in);
        }

        try {
            printUsers(DBUtils.sortUsers(command));
        } catch (SQLException e) {
            System.err.println("Error sort!");
        }
    }

    private static int inputInt(Scanner in) {
        int buffer = -1;
        while (buffer < 0) {
            if (in.hasNextInt()) {
                buffer = in.nextInt();
                if (buffer < 0) {
                    System.out.println(ERROR_INPUT_MESSAGE);
                }
            } else {
                in.next();
                System.out.println(ERROR_INPUT_MESSAGE);
            }
        }
        return buffer;
    }


    private static void changeUser(User user) {
        Scanner in = new Scanner(System.in);
        System.out.println("Change name ( old value " + user.getName() + "): ");
        user.setName(in.nextLine());

        System.out.println("Change surname ( old value " + user.getSurname() + "): ");
        user.setSurname(in.nextLine());

        System.out.println("Change login ( old value " + user.getLogin() + "): ");
        user.setLogin(in.nextLine());

        System.out.println("Change email ( old value " + user.getEmail() + "): ");

        String email = in.nextLine();
        while (!CheckEmail(email)) {
            System.out.println(ERROR_INPUT_MESSAGE);
            email = in.nextLine();
        }
        user.setEmail(email);
    }

    private static void printUsers(List<User> list) {
        if (list.isEmpty())
            System.out.print("List is empty!");
        else
            for (User user : list)
                System.out.print(user);
    }

    private static boolean CheckEmail(String email) {
        final String patternStr = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\" +
                "x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])" +
                "?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|" +
                "[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b" +
                "\\x0c\\x0e-\\x7f])+)\\])";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
