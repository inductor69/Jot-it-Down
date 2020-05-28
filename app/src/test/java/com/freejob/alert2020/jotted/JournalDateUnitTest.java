package com.freejob.alert2020.jotted;

import com.freejob.alert2020.jotted.util.JournalDate;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class JournalDateUnitTest {
    private Calendar cal;
    private Date date;

    @Before
    public void runBefore() {
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 5);
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 32);
        cal.set(Calendar.SECOND, 15);

        date = cal.getTime();    // September 25, 2017 18:32:15
    }

    @Test
    public void testFormatDateToString() {
        assertEquals("September 5, 2017", JournalDate.formatDateToString(date));
    }

    @Test
    public void testFormatDateToDataString() {
        assertEquals("2017-09-05 18:32:15", JournalDate.formatDateToDataString(date));
    }

    @Test
    public void testDataStringToDate() {
        String str = "2019-03-04 13:21:03";
        Date ac = JournalDate.dataStringToDate(str);
        cal.setTime(ac);
        assertEquals(2019, cal.get(Calendar.YEAR));
        assertEquals(Calendar.MARCH, cal.get(Calendar.MONTH));
        assertEquals(4, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(13, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(21, cal.get(Calendar.MINUTE));
        assertEquals(3, cal.get(Calendar.SECOND));
    }

    @Test
    public void testCurrentDate() {
        Date ex = Calendar.getInstance().getTime();
        Date ac = JournalDate.currentDate();
        assertEquals(JournalDate.formatDateToDataString(ex), JournalDate.formatDateToDataString(ac));
    }

    @Test
    public void testWelcomeString() {
        testWelcomeStringMorning();
        testWelcomeStringAfternoon();
        testWelcomeStringEvening();
    }

    private void testWelcomeStringMorning() {
        for (int i = 4; i <= 11; i++) {
            cal.set(Calendar.HOUR_OF_DAY, i);
            Date d = cal.getTime();
            assertEquals("Good morning!", JournalDate.welcomeString(d));
        }
    }

    private void testWelcomeStringAfternoon() {
        for (int i = 12; i <= 17; i++) {
            cal.set(Calendar.HOUR_OF_DAY, i);
            Date d = cal.getTime();
            assertEquals("Good afternoon!", JournalDate.welcomeString(d));
        }
    }

    private void testWelcomeStringEvening() {
        for (int i = 18; i < 24; i++) {
            cal.set(Calendar.HOUR_OF_DAY, i);
            Date d = cal.getTime();
            assertEquals("Good evening!", JournalDate.welcomeString(d));
        }
        for (int i = 0; i <= 3; i++) {
            cal.set(Calendar.HOUR_OF_DAY, i);
            Date d = cal.getTime();
            assertEquals("Good evening!", JournalDate.welcomeString(d));
        }
    }
}