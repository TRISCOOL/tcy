package tcy.common.utils;

import com.google.gson.Gson;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.*;

/**
 * author: user
 * create: 2018/3/9
 */
public class StringUtils {

    public static Gson gson = new Gson();

    public static Map xml2Map(String xml){
        Map<String,Object> map = new HashMap<>();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if(doc ==null)
            return map;
        Element root = doc.getRootElement();

        if(root.elementIterator().hasNext()){
            List<Map> list = new ArrayList<>();
            for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
                Element e = (Element) iterator.next();
                list.add(xml2Map(e.asXML()));
            }
            map.put(root.getName(),list);
        }else{
            map.put(root.getName(),root.getText());
        }

        return map;
    }

    public static String getNoWeekdayCron(Date date){
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1); //1天后过期
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return String.format("%d %d %d %d %d ? %d",
                second, minute, hour, day, month, year);
    }
}
