package bulltrack.com.optionanalyzer.utility;

import bulltrack.com.optionanalyzer.constants.Constants;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer implements JsonSerializer<Date> {
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(new SimpleDateFormat(Constants.DT_FMT_yyyy_MM_dd_HH_m_ss).format(date));
    }
}
