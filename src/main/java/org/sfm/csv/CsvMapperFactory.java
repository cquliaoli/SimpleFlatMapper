package org.sfm.csv;

import org.sfm.csv.impl.CellValueReaderFactoryImpl;
import org.sfm.csv.impl.CsvColumnDefinitionProviderImpl;
import org.sfm.csv.impl.DynamicCsvMapper;
import org.sfm.map.*;
import org.sfm.reflect.TypeReference;
import org.sfm.reflect.meta.ClassMeta;

import java.lang.reflect.Type;

/**
 * CsvMapperFactory is not Thread-Safe but the mappers are.
 * It is strongly advised to instantiate one mapper per class for the life of your application.
 * <p>
 * You can instantiate dynamic mapper which will use the first line of the csv file
 * to figure out the list of the columns or a static one using a builder.
 * <p>
 * <code>
 *     // create a dynamic mapper targeting MyClass<br>
 *     CsvMapperFactory<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;.newInstance()<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;.newMapper(MyClass.class);<br>
 *     <br>
 *     // create a static mapper targeting MyClass<br>
 *     CsvMapperFactory<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;.newInstance()<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;.newBuilder(MyClass.class)<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;.addMapping("id")<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;.addMapping("field1")<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;.addMapping("field2")<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;.mapper();<br>
 *     <br>
 * </code>
 */
public final class CsvMapperFactory extends AbstractMapperFactory<CsvColumnKey, CsvColumnDefinition, CsvMapperFactory> {

	/**
	 * instantiate a new JdbcMapperFactory
	 * @return a new JdbcMapperFactory
	 */
	public static CsvMapperFactory newInstance() {
		return new CsvMapperFactory();
	}
	
	private CellValueReaderFactory cellValueReaderFactory = new CellValueReaderFactoryImpl();

	private String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

	private CsvMapperFactory() {
		super(new CsvColumnDefinitionProviderImpl(), CsvColumnDefinition.IDENTITY);
	}

	public CsvMapperFactory defaultDateFormat(final String defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
		return this;
	}

	public CsvMapperFactory cellValueReaderFactory(final CellValueReaderFactory cellValueReaderFactory) {
		this.cellValueReaderFactory = cellValueReaderFactory;
		return this;
	}

	public CsvMapperFactory addCustomValueReader(String key,	CellValueReader<?> cellValueReader) {
		return addColumnDefinition(key, CsvColumnDefinition.customReaderDefinition(cellValueReader));
	}

	/**
	 * 
	 * @param target the targeted class for the mapper
     * @param <T> the targeted type
	 * @return a jdbc mapper that will map to the targeted class.
	 * @throws MapperBuildingException if an error occurs building the mapper
	 */
	public <T> CsvMapper<T> newMapper(final Class<T> target) throws MapperBuildingException {
		return newMapper((Type)target);
	}

    public <T> CsvMapper<T> newMapper(final TypeReference<T> target) throws MapperBuildingException {
        return newMapper(target.getType());
    }

    public <T> CsvMapper<T> newMapper(final Type target) throws MapperBuildingException {
		ClassMeta<T> classMeta = getClassMeta(target);
		return new DynamicCsvMapper<T>(
				target,
				classMeta,
				defaultDateFormat,
				cellValueReaderFactory,
				mapperConfig());
	}

	/**
	 * Will create a instance of ResultSetMapperBuilder 
	 * @param target the target class of the mapper
     * @param <T> the targeted type
	 * @return a builder ready to instantiate a mapper or to be customized
     * @throws MapperBuildingException if an error occurs building the mapper
	 */
	public <T> CsvMapperBuilder<T> newBuilder(final Class<T> target) {
		return newBuilder((Type)target);
	}

    public <T> CsvMapperBuilder<T> newBuilder(final TypeReference<T> target) {
        return newBuilder(target.getType());
    }

    public <T> CsvMapperBuilder<T> newBuilder(final Type target) {
		ClassMeta<T> classMeta = getClassMeta(target);
		CsvMapperBuilder<T> builder =
				new CsvMapperBuilder<T>(target, classMeta,0, cellValueReaderFactory, mapperConfig());
		builder.setDefaultDateFormat(defaultDateFormat);
		return builder;
	}
}
