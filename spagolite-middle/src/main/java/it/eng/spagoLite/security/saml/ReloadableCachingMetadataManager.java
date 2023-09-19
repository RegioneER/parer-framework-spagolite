/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

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
