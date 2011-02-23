//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.metadata.persistence.iso.testclasses;

import java.io.IOException;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.deegree.commons.config.WorkspaceInitializationException;
import org.deegree.metadata.persistence.MetadataInspectorException;
import org.deegree.metadata.persistence.MetadataQuery;
import org.deegree.metadata.persistence.iso.ISOMetadataStore;
import org.deegree.metadata.persistence.iso.ISOMetadataStoreProvider;
import org.deegree.metadata.persistence.iso.helper.AbstractISOTest;
import org.deegree.metadata.persistence.iso.helper.TstConstants;
import org.deegree.metadata.persistence.iso.helper.TstUtils;
import org.deegree.protocol.csw.MetadataStoreException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the AnyTextElement
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class TstAnyTextElement extends AbstractISOTest {
    private static Logger LOG = LoggerFactory.getLogger( TstAnyTextElement.class );

    @Test
    public void testAnyTextElement_ALL()
                            throws MetadataStoreException, XMLStreamException, FactoryConfigurationError, IOException,
                            MetadataInspectorException, WorkspaceInitializationException {
        LOG.info( "START Test: test anyText element 'ALL' for one metadataRecord " );

        if ( jdbcURL != null && jdbcUser != null && jdbcPass != null ) {
            store = (ISOMetadataStore) new ISOMetadataStoreProvider().create( TstConstants.configURL_ANYTEXT_ALL );
        }
        if ( store == null ) {
            LOG.warn( "Skipping test (needs configuration)." );
            return;
        }
        List<String> ids = TstUtils.insertMetadata( store, TstConstants.tst_10 );
        if ( ids != null ) {
            // test query
            MetadataQuery query = new MetadataQuery( null, null, 1 );
            resultSet = store.getRecords( query );
            String anyText = null;
            while ( resultSet.next() ) {
                anyText = resultSet.getRecord().getAnyText();
            }

            String compare = "d0e5c36eec7f473b91b8b249da87d522 eng UTF 8 dataset European Commission, Joint Research Centre cid-contact@jrc.ec.europa.eu pointOfContact 2007-01-23 ISO 19115:2003/19139 1.0 2 6000 6000 true false 10.2295939511158 52.6984540463519 10.4685111911662 53.2174450795883 9.34255616300099 52.8445914851784 9.57111840348035 53.3646726482873 center 4326 EPSG SPOT 5 RAW 2007-01-23T10:25:14 2007-01-23T10:25:14 d0e5c36eec7f473b91b8b249da87d522 Raw (source) image from CwRS campaigns. European Commission, Joint Research Centre, IPSC, MARS Unit cid-contact@jrc.ec.europa.eu pointOfContact SPOT 5 PATH 50 ROW 242 Orthoimagery GEMET - INSPIRE themes, version 1.0 2008-06-01 publication http://cidportal.jrc.ec.europa.eu/home/idp/info/license/ec-jrc-fc251603/ otherRestrictions (e) intellectual property rights; license unclassified 10.0 eng imageryBaseMapsEarthCover 9.342556163 10.4685111912 52.6984540464 53.3646726483 9.57111840348035 53.3646726482873 9.34255616300099 52.8445914851784 10.2295939511158 52.6984540463519 10.4685111911662 53.2174450795883 9.57111840348035 53.3646726482873 2007-01-23T10:25:14 2007-01-23T10:25:14 Detailed image characteristics XS1 8 XS2 8 XS3 8 SWIR 8 16.129405 163.631838 0.0 RAW RAW N/A ECW N/A http://cidportal.jrc.ec.europa.eu/imagearchive/ Raw (Source) image as delivered by image provider. ";

            Assert.assertEquals( "anyText ALL: ", compare, anyText );

        } else {
            throw new MetadataStoreException( "something went wrong in creation of the metadataRecord" );
        }
    }

    @Test
    public void testAnyTextElement_CORE()
                            throws MetadataStoreException, XMLStreamException, FactoryConfigurationError, IOException,
                            MetadataInspectorException, WorkspaceInitializationException {
        LOG.info( "START Test: test anyText element 'CORE' for one metadataRecord " );

        if ( jdbcURL != null && jdbcUser != null && jdbcPass != null ) {
            store = (ISOMetadataStore) new ISOMetadataStoreProvider().create( TstConstants.configURL_ANYTEXT_CORE );
        }
        if ( store == null ) {
            LOG.warn( "Skipping test (needs configuration)." );
            return;
        }
        List<String> ids = TstUtils.insertMetadata( store, TstConstants.tst_10 );
        if ( ids != null ) {
            // test query
            MetadataQuery query = new MetadataQuery( null, null, 1 );
            resultSet = store.getRecords( query );
            String anyText = null;
            while ( resultSet.next() ) {
                anyText = resultSet.getRecord().getAnyText();
            }

            String compare = "Raw (source) image from CwRS campaigns. RAW ECW d0e5c36eec7f473b91b8b249da87d522 eng Tue Jan 23 00:00:00 CET 2007 SPOT 5 RAW 2007-01-23T10:25:14 dataset SPOT 5 PATH 50 ROW 242 Orthoimagery imageryBaseMapsEarthCover true otherRestrictions license Raw (Source) image as delivered by image provider. ";

            Assert.assertEquals( "anyText CORE: ", compare, anyText );

        } else {
            throw new MetadataStoreException( "something went wrong in creation of the metadataRecord" );
        }
    }

    @Test
    public void testAnyTextElement_CUSTOM()
                            throws MetadataStoreException, XMLStreamException, FactoryConfigurationError, IOException,
                            MetadataInspectorException, WorkspaceInitializationException {
        LOG.info( "START Test: test anyText element 'CUSTOM' for one metadataRecord " );

        if ( jdbcURL != null && jdbcUser != null && jdbcPass != null ) {
            store = (ISOMetadataStore) new ISOMetadataStoreProvider().create( TstConstants.configURL_ANYTEXT_CUSTOM );
        }
        if ( store == null ) {
            LOG.warn( "Skipping test (needs configuration)." );
            return;
        }
        List<String> ids = TstUtils.insertMetadata( store, TstConstants.tst_10 );
        if ( ids != null ) {
            // test query
            MetadataQuery query = new MetadataQuery( null, null, 1 );
            resultSet = store.getRecords( query );
            String anyText = null;
            while ( resultSet.next() ) {
                anyText = resultSet.getRecord().getAnyText();
            }

            String compare = "d0e5c36eec7f473b91b8b249da87d522 SPOT 5 PATH 50 ROW 242 Orthoimagery ";

            Assert.assertEquals( "anyText CUSTOM: ", compare, anyText );

        } else {
            throw new MetadataStoreException( "something went wrong in creation of the metadataRecord" );
        }
    }

}
