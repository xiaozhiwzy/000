package work.sajor.wechatpush.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @ClassName JiNianRi
 * @Description TODO
 * @Author zwy
 * @Date 2022/8/25
 */
public class JiNianRi {

    /**
     * 距离date还有多少天
     * @param:date
     * @return
     */
    public static int before(String data){
        try {
            return calculationBirthday(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 已经过去date多少天
     * 输入阳历生日，转为阴历，按照阴历跳转到现在，将阴历转为阳历，然后计算
     * @param date
     * @return
     */
    public static int after(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        int day = 0;
        try {
            long time = System.currentTimeMillis() - simpleDateFormat.parse(date).getTime();
            day = (int) (time / 86400000L);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    public static int calculationBirthday(String clidate) throws ParseException {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMdd");
        Calendar cToday = Calendar.getInstance(); // 存今天
        Calendar cBirth = Calendar.getInstance(); // 存生日
        int days=0;
        try {
            String lunarDate=CalendarUtil.solarToLunar(clidate);
            String date = String.valueOf(cToday.get(Calendar.YEAR))+lunarDate.substring(4, 8);
            String SolarDate = CalendarUtil.lunarToSolar(date);

            cBirth.setTime(myFormatter.parse(SolarDate)); // 设置生日
            cBirth.set(Calendar.YEAR, cToday.get(Calendar.YEAR)); // 修改为本年


            if (cBirth.get(Calendar.DAY_OF_YEAR) < cToday.get(Calendar.DAY_OF_YEAR)) {
                // 生日已经过了，要算明年的了
                days = cToday.getActualMaximum(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
                days += cBirth.get(Calendar.DAY_OF_YEAR);
            } else {
                // 生日还没过
                days = cBirth.get(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        // 输出结果
        if (days == 0) {
            return 0;
        } else {
            return days;
        }
    }

    public static int getLianAi(String lianAi) {
        return after(lianAi);
    }

    public static int getShengRi(String shengRi){
        return before(shengRi);
    }



}
