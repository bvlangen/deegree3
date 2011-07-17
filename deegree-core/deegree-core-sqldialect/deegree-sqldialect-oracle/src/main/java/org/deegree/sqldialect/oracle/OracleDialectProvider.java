//$HeadURL: svn+ssh://criador.lat-lon.de/srv/svn/deegree-intern/trunk/latlon-sqldialect-oracle/src/main/java/de/latlon/deegree/sqldialect/oracle/OracleDialectProvider.java $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
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
package org.deegree.sqldialect.oracle;

import static org.deegree.commons.jdbc.ConnectionManager.Type.Oracle;
import static org.deegree.commons.utils.JDBCUtils.close;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.deegree.commons.config.DeegreeWorkspace;
import org.deegree.commons.config.ResourceInitException;
import org.deegree.commons.jdbc.ConnectionManager;
import org.deegree.commons.jdbc.ConnectionManager.Type;
import org.deegree.sqldialect.SQLDialect;
import org.deegree.sqldialect.SQLDialectProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link SQLDialectProvider} for Oracle spatial databases.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: schmitz $
 * 
 * @version $Revision: 301 $, $Date: 2011-06-14 11:20:48 +0200 (Di, 14. Jun 2011) $
 */
public class OracleDialectProvider implements SQLDialectProvider {

    private static Logger LOG = LoggerFactory.getLogger( OracleDialectProvider.class );

    @Override
    public Type getSupportedType() {
        return Oracle;
    }

    @Override
    public SQLDialect create( String connId, DeegreeWorkspace ws )
                            throws ResourceInitException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String username = null;
        String version = null;
        try {
            conn = ConnectionManager.getConnection( connId );
            username = conn.getMetaData().getUserName();
            stmt = conn.createStatement();
            rs = stmt.executeQuery( "select banner from v$version where banner like 'Oracle%'" );
            if ( rs.next() ) {
                Pattern p = Pattern.compile( "(\\d+)[.]\\d+[.]\\d+[.]\\d+[.]\\d+" );
                Matcher m = p.matcher( rs.getString( 1 ) );
                if ( m.find() ) {
                    version = m.group( 1 );
                }
            }
            LOG.info( "Instantiating Oracle dialect for version {}.", version );
        } catch ( SQLException e ) {
            LOG.debug( e.getMessage(), e );
            throw new ResourceInitException( e.getMessage(), e );
        } finally {
            close( rs, stmt, conn, LOG );
        }
        return new OracleDialect( username, version );
    }
}