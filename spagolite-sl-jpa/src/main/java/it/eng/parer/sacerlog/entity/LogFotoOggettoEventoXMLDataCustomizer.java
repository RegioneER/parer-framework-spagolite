package it.eng.parer.sacerlog.entity;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.xdb.DirectToXMLTypeMapping;

/**
 *
 * @author Iacolucci_M
 */
public class LogFotoOggettoEventoXMLDataCustomizer implements DescriptorCustomizer {

    public void customize(final ClassDescriptor descriptor) throws Exception {
        descriptor.removeMappingForAttributeName("blFotoOggetto");
        DirectToXMLTypeMapping mapping = new DirectToXMLTypeMapping();
        mapping.setAttributeName("blFotoOggetto"); // nome dell'attributo dell'entita'
        mapping.setFieldName("BL_FOTO_OGGETTO"); // nome della colonna del DBname of the data base column
        mapping.setFieldType(java.sql.Types.NVARCHAR);
        descriptor.addMapping(mapping);
    }

}
