package it.eng.spagoLite.security.saml;

import java.util.List;

import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.springframework.security.saml.metadata.CachingMetadataManager;

/**
 * @author Quaranta_M
 *
 */
public class ReloadableCachingMetadataManager extends CachingMetadataManager {

    public ReloadableCachingMetadataManager(List<MetadataProvider> providers) throws MetadataProviderException {
        super(providers);
    }

    /**
     * Flag indicating whether configuration of the metadata should be reloaded.
     *
     * @return true if reload is required
     */
    @Override
    public boolean isRefreshRequired() {
        if (getProviders().isEmpty()) {
            return true;
        } else {
            return super.isRefreshRequired();
        }
    }

}
