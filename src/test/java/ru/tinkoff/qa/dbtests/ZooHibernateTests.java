package ru.tinkoff.qa.dbtests;

import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.tinkoff.qa.hibernate.BeforeCreator;
import ru.tinkoff.qa.hibernate.HibernateSessionFactoryCreator;
import ru.tinkoff.qa.hibernate.models.Animal;
import ru.tinkoff.qa.hibernate.models.Places;
import ru.tinkoff.qa.hibernate.models.Workman;

import java.util.List;

public class ZooHibernateTests {

    @BeforeAll
    static void init() {
        BeforeCreator.createData();
    }

    /**
     * В таблице public.animal ровно 10 записей
     */
    @Test
    public void countRowAnimal() {
        Session session = HibernateSessionFactoryCreator.createSessionFactory().openSession();
        int tableSize = session.createNativeQuery("SELECT animal.id FROM public.animal animal")
                .list().size();
        Assertions.assertEquals(10, tableSize, "Check number of animals in table");

        session.close();
    }

    /**
     * В таблицу public.animal нельзя добавить строку с индексом от 1 до 10 включительно
     */
    @Test
    public void insertIndexAnimal() {
        Session session = HibernateSessionFactoryCreator.createSessionFactory().openSession();
        for (int i=1; i<=10; i++){
            Transaction transaction = session.beginTransaction();
            Animal animal = new Animal();
            animal.setId(i);
            animal.setAge(2);
            animal.setType(1);
            animal.setSex(1);
            animal.setPlace(1);
            animal.setName("zebra");
            session.save(animal);
            boolean isSucceed = true;
            try {
                transaction.commit();
            } catch (PersistenceException e) {
                isSucceed = false;
            }
            Assertions.assertFalse(isSucceed, "Check animal id  could not be from 1 to 10");
        }
        session.close();
    }

    /**
     * В таблицу public.workman нельзя добавить строку с name = null
     */
    @Test
    public void insertNullToWorkman() {
        Session session = HibernateSessionFactoryCreator.createSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Workman workman = new Workman();
        workman.setId(9);
        workman.setAge(23);
        workman.setPosition(1);
        session.save(workman);
        boolean isSucceed = true;
        try {
            transaction.commit();
        } catch (PersistenceException e) {
            isSucceed = false;
        }
        Assertions.assertFalse(isSucceed, "Check workman name could not be null");
        session.close();

    }

    /**
     * Если в таблицу public.places добавить еще одну строку, то в ней будет 6 строк
     */
    @Test
    public void insertPlacesCountRow() {
        Session session = HibernateSessionFactoryCreator.createSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Places places = new Places();
        places.setId(6);
        places.setRow(6);
        places.setPlace_num(6);
        places.setName("Пограничный");
        session.save(places);
        transaction.commit();
        int tableSize = session.createNativeQuery("SELECT places.id FROM public.places places")
                .list().size();
        Assertions.assertEquals(6, tableSize, "Check number of animals in table");

        session.close();
    }

    /**
     * В таблице public.zoo всего три записи с name 'Центральный', 'Северный', 'Западный'
     */
    @Test
    public void countRowZoo() {
        Session session = HibernateSessionFactoryCreator.createSessionFactory().openSession();
        List<String> table = session.createNativeQuery("SELECT \"name\" FROM public.zoo").list();

        Assertions.assertEquals(3, table.size());
        Assertions.assertTrue(table.contains("Центральный"));
        Assertions.assertTrue(table.contains("Северный"));
        Assertions.assertTrue(table.contains("Западный"));
        session.close();
    }
}
