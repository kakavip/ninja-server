package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;

public class util {
    public static boolean debug = false;
    private static final Locale locale;
    private static final NumberFormat en;
    private static final Random rand;
    private static final SimpleDateFormat dateFormat;
    private static final SimpleDateFormat dateFormatWeek;
    public static final SimpleDateFormat dateFormatDay;

    public static synchronized Date getDate(final String dateString) {
        if (dateString == null || dateString.equals("")) return new Date();
        try {
            return util.dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static long TimeDay(final int nDays) {
        return System.currentTimeMillis() + nDays * 86400000L;
    }

    public static long TimeHours(final int nHours) {
        return System.currentTimeMillis() + nHours * 3600000L;
    }

    public static long TimeMinutes(final int nMinutes) {
        return System.currentTimeMillis() + nMinutes * 60000L;
    }

    public static long TimeSeconds(final long nSeconds) {
        return System.currentTimeMillis() + nSeconds * 1000L;
    }

    public static long TimeMillis(final long nMillis) {
        return System.currentTimeMillis() + nMillis;
    }

    public static Date DateDay(final int nDays) {
        final Date dat = new Date();
        dat.setTime(dat.getTime() + nDays * 86400000L);
        return dat;
    }

    public static String toDateString(final Date date) {
        return util.dateFormat.format(date);
    }

    public static Date DateHours(final int nHours) {
        final Date dat = new Date();
        dat.setTime(dat.getTime() + nHours * 3600000L);
        return dat;
    }

    public static Date DateMinutes(final int nMinutes) {
        final Date dat = new Date();
        dat.setTime(dat.getTime() + nMinutes * 60000L);
        return dat;
    }

    public static Date DateSeconds(final long nSeconds) {
        final Date dat = new Date();
        dat.setTime(dat.getTime() + nSeconds * 1000L);
        return dat;
    }

    public static String getFormatNumber(final long num) {
        return util.en.format(num);
    }

    public static boolean compare_Week(final Date now, final Date when) {
        try {
            final Date date1 = util.dateFormatWeek.parse(util.dateFormatWeek.format(now));
            final Date date2 = util.dateFormatWeek.parse(util.dateFormatWeek.format(when));
            return !date1.equals(date2) && !date1.before(date2);
        } catch (ParseException p) {
            p.printStackTrace();
            return false;
        }
    }

    public static short[] concatArray(short[]... arr1) {
        int size = 0;
        for (short[] shorts : arr1) {
            size += shorts.length;
        }
        short[] result = new short[size];
        int i = 0;
        for (short[] shorts : arr1) {
            for (short aShort : shorts) {
                result[i] = aShort;
                i++;
            }
        }
        return result;
    }

    public static synchronized boolean compare_Day(final Date now, final Date when) {
        try {
            final Date date1 = util.dateFormatDay.parse(util.dateFormatDay.format(now));
            final Date date2 = util.dateFormatDay.parse(util.dateFormatDay.format(when));
            return !date1.equals(date2) && !date1.before(date2);
        } catch (ParseException p) {
            p.printStackTrace();
            return false;
        }
    }

    public static boolean checkNumInt(final String num) {
        return Pattern.compile("^[0-9]+$").matcher(num).find();
    }

    public static int UnsignedByte(final byte b) {
        final int ch = b;
        if (ch < 0) {
            return ch + 256;
        }
        return ch;
    }

    public static String parseString(final String str, final String wall) {
        return str.contains(wall) ? str.substring(str.indexOf(wall) + 1) : null;
    }

    public static boolean CheckString(final String str, final String regex) {
        return Pattern.compile(regex).matcher(str).find();
    }

    public static String strSQL(final String str) {
        return str.replaceAll("['\"\\\\]", "\\\\$0");
    }

    public static int nextInt(final int x1, final int x2) {
        int to = x2;
        int from = x1;
        if (x2 < x1) {
            to = x1;
            from = x2;
        }
        return from + util.rand.nextInt(to + 1 - from);
    }

    public static int nextInt(final int max) {
        return util.rand.nextInt(max);
    }

    public static void setDebug(final boolean v) {
        util.debug = v;
    }

    @SneakyThrows
    public static void Debug(final Object v) {
        if (util.debug) {
            System.out.println(v);
        }
    }

    static {
        util.debug = false;
        locale = new Locale("vi");
        en = NumberFormat.getInstance(util.locale);
        rand = new Random();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatWeek = new SimpleDateFormat("yyyy-MM-ww");
        dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static boolean percent(int maxPercent, int percent) {
        return percent >= util.nextInt(1, maxPercent);
    }

    public static List<Ip> ReadIp(){
        ArrayList<Ip> list = new ArrayList<>();
        try{
            FileReader fr = new FileReader("log/ip.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while(true){
                line = br.readLine();
                if (line == null){
                    break;
                }
                /*String txt[] = line.split(";");
                String name = txt[0];
                byte log = Byte.parseByte(txt[1]);*/
                list.add(new Ip(line));
            }
            fr.close();
            br.close();
        }catch (Exception e){
        }
        return list;
    }
    
    public static void WriteIp(String IP){
        try{
            FileWriter fw = new FileWriter("log/ip.txt");
            BufferedWriter bs = new BufferedWriter(fw);
            bs.write(IP);
            bs.newLine();
            bs.close();
            fw.close();
        }catch (Exception e){
        }
    }
}
