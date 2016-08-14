package org.simpleflatmapper.map.mapper;

import org.junit.Test;
import org.simpleflatmapper.map.SampleFieldKey;
import org.simpleflatmapper.map.property.FieldMapperColumnDefinition;
import org.simpleflatmapper.util.BiConsumer;
import org.simpleflatmapper.util.ConstantUnaryFactory;
import org.simpleflatmapper.util.Predicate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FieldMapperColumnDefinitionProviderImplTest {


    @Test
    public void testIdentity() {
        assertEquals(FieldMapperColumnDefinition.<SampleFieldKey>identity(), new FieldMapperColumnDefinitionProviderImpl<SampleFieldKey>().identity());
    }

    @Test
    public void testProvides() {

        final Object prop1 = "prop1";
        final Object prop2 = "prop2";
        final ConstantUnaryFactory<SampleFieldKey, Object> prop1Fact = new ConstantUnaryFactory<>(prop1);
        final ConstantUnaryFactory<SampleFieldKey, Object> prop2Fact = new ConstantUnaryFactory<>(prop2);

        FieldMapperColumnDefinitionProviderImpl<SampleFieldKey> provider =
                new FieldMapperColumnDefinitionProviderImpl<SampleFieldKey>();


        Predicate<SampleFieldKey> prop1Predicate = new Predicate<SampleFieldKey>() {
            @Override
            public boolean test(SampleFieldKey sampleFieldKey) {
                return sampleFieldKey.getIndex() == 0;
            }
        };

        Predicate<SampleFieldKey> prop2Predicate = new Predicate<SampleFieldKey>() {
            @Override
            public boolean test(SampleFieldKey sampleFieldKey) {
                return sampleFieldKey.getIndex() == 1;
            }
        };


        provider.addColumnProperty(prop1Predicate, prop1Fact);
        provider.addColumnProperty(prop2Predicate, prop2Fact);


        List<AbstractColumnDefinitionProvider.PredicatedColunnPropertyFactory<FieldMapperColumnDefinition<SampleFieldKey>, SampleFieldKey>> properties =
                provider.getProperties();

        assertEquals(2, properties.size());
        assertEquals(prop1Predicate, properties.get(0).getPredicate());
        assertEquals(prop2Predicate, properties.get(1).getPredicate());
        assertEquals(prop1Fact, properties.get(0).getColumnPropertyFactory());
        assertEquals(prop2Fact, properties.get(1).getColumnPropertyFactory());

        final ArrayList<String> props = new ArrayList<String>();

        provider.addColumnProperty(prop1Predicate, new ConstantUnaryFactory<SampleFieldKey, Object>(new Object()));

        provider.forEach(String.class, new BiConsumer<Predicate<? super SampleFieldKey>, String>() {
            @Override
            public void accept(Predicate<? super SampleFieldKey> predicate, String s) {
                if (s == prop1) {
                    assertEquals(predicate, prop1Predicate);
                }
                if (s == prop2) {
                    assertEquals(predicate, prop2Predicate);
                }
                props.add(s);
            }
        });

        assertArrayEquals(new String[] {"prop1", "prop2"}, props.toArray(new String[0]));
    }

}