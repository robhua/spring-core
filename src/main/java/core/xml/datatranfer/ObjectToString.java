package core.xml.datatranfer;

import java.util.List;
import org.springframework.core.convert.converter.Converter;
import core.xml.data.MyDataSource;

public class ObjectToString implements Converter<MyDataSource, List<String>> {
	@Override
	public List<String> convert(MyDataSource arg0) {
		return arg0.getListString();
	}
}
