package ru.tinkoff.qa.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.tinkoff.qa.hibernate.models.Animal;
import ru.tinkoff.qa.hibernate.models.Places;
import ru.tinkoff.qa.hibernate.models.Workman;
import ru.tinkoff.qa.hibernate.models.Zoo;

public class HibernateSessionFactoryCreator {
    public static SessionFactory createSessionFactory(){
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Animal.class)
                .addAnnotatedClass(Places.class)
                .addAnnotatedClass(Workman.class)
                .addAnnotatedClass(Zoo.class)
                .buildSessionFactory();

    }
}
