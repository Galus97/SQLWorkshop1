package pl.coderslab.entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class MainDao {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        User secondUser = new User();
        secondUser.setUserName("marek");
        secondUser.setEmail("ja@coderslab.pl");
        secondUser.setPassword("pass");
        userDao.create(secondUser);
        User[] all = userDao.findAll();
        for (User u : all) {
            System.out.println(u);
        }


//        UserDao userToDelete = new UserDao();
//        userToDelete.delete(1);


//        UserDao userDao = new UserDao();
//
//        User read = userDao.read(1);
//        System.out.println(read);
//
//        read.setUserName("Monika");
//        read.setEmail("monika@wp.pl");
//        read.setPassword("Leon97");
//
//        userDao.update(read);
//
//        System.out.println(read);
//        User read2 = userDao.read(99);
//        System.out.println(read2);
//
//        read2.setUserName("Monika");
//        read2.setEmail("monika@wp.pl");
//        read2.setPassword("Leon97");
//
//        System.out.println(read2);
    }
}