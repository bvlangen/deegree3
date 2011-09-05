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

package org.deegree.protocol.wfs.getfeaturewithlock;

import java.util.List;

import org.deegree.commons.tom.ResolveParams;
import org.deegree.commons.tom.ows.Version;
import org.deegree.protocol.wfs.getfeature.GetFeature;
import org.deegree.protocol.wfs.query.Query;
import org.deegree.protocol.wfs.query.StandardPresentationParams;

/**
 * Represents a <code>GetFeatureWithLock</code> request to a WFS.
 * 
 * @see GetFeature
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class GetFeatureWithLock extends GetFeature {

    private Integer expiry;

    /**
     * Creates a new {@link GetFeatureWithLock} request.
     * 
     * @param version
     *            protocol version, must not be <code>null</code>
     * @param handle
     *            client-generated identifier, may be <code>null</code>
     * @param presentationParams
     *            parameters for controlling the presentation of the result set, may be <code>null</code>
     * @param resolveParams
     *            parameters for controlling the resolution of references of the result set, may be <code>null</code>
     * @param queries
     *            the queries to be performed in the request, must not be <code>null</code> and must contain at least
     *            one entry
     * @param expiry
     *            expiry time (in minutes) before the features are unlocked automatically, may be null (unspecified)
     */
    public GetFeatureWithLock( Version version, String handle, StandardPresentationParams presentationParams,
                               ResolveParams resolveParams, List<Query> queries, Integer expiry ) {
        super( version, handle, presentationParams, resolveParams, queries );
        this.expiry = expiry;
    }

    /**
     * Returns the expiry time for the acquired locks.
     * 
     * @return the expiry time for the acquired locks, can be <code>null</code> (unspecified)
     */
    public Integer getExpiry() {
        return expiry;
    }
}
