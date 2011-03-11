//$HeadURL: svn+ssh://mschneider@svn.wald.intevation.org/deegree/deegree3/trunk/deegree-client/deegree-jsf-console/src/main/java/org/deegree/client/wms/OpenLayers.java $
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
package org.deegree.console.wms;

import static java.lang.System.currentTimeMillis;
import static org.deegree.commons.utils.time.DateUtils.formatISO8601Date;
import static org.deegree.commons.utils.time.DateUtils.parseISO8601Date;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;

/**
 * <code>OpenLayers</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 29926 $, $Date: 2011-03-08 11:47:59 +0100 (Di, 08. Mär 2011) $
 */
@ManagedBean
@SessionScoped
public class OpenLayers implements Serializable {

    private static final long serialVersionUID = 6527271691461126514L;

    private static final Logger LOG = getLogger( OpenLayers.class );

    @Getter
    private String url;

    private LinkedList<Layer> layers = new LinkedList<Layer>();

    @Getter
    private String coordinateSystem;

    @Getter
    private double minx;

    @Getter
    private double miny;

    @Getter
    private double maxx;

    @Getter
    private double maxy;

    @Getter
    private boolean statisticsAvailable;

    @Getter
    @Setter
    private Date from;

    @Getter
    @Setter
    private Date until;

    private String fromString;

    private String untilString;

    private Date extent;

    private Layer statistics;

    @Getter
    @Setter
    private HtmlPanelGroup mapPanel;

    /**
     * 
     */
    public OpenLayers() {
        this.url = getServerURL() + "services";

        loadCapabilities();
    }

    public static String getServerURL() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        URL url;
        try {
            url = new URL( ctx.getRequestScheme(), ctx.getRequestServerName(), ctx.getRequestServerPort(),
                           ctx.getRequestContextPath() );
            return url.toExternalForm() + "/";
        } catch ( MalformedURLException e ) {
            LOG.debug( "Constructing the url was a problem..." );
            LOG.trace( "Stack trace:", e );
        }
        return null;
    }

    private void loadCapabilities() {
        String getCapas = url + "?request=GetCapabilities&service=WMS&version=1.1.1";

        boolean bboxRead = false;

        InputStream in = null;
        try {
            in = new URL( getCapas ).openStream();
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader( in );
            while ( reader.hasNext() ) {
                reader.next();

                if ( !bboxRead && reader.isStartElement() && reader.getLocalName().equals( "SRS" )
                     && coordinateSystem == null ) {
                    coordinateSystem = reader.getElementText();
                    continue;
                }
                if ( !bboxRead && coordinateSystem != null && reader.isStartElement()
                     && reader.getLocalName().equals( "BoundingBox" )
                     && reader.getAttributeValue( null, "SRS" ).equals( coordinateSystem ) ) {
                    minx = Double.parseDouble( reader.getAttributeValue( null, "minx" ) );
                    miny = Double.parseDouble( reader.getAttributeValue( null, "miny" ) );
                    maxx = Double.parseDouble( reader.getAttributeValue( null, "maxx" ) );
                    maxy = Double.parseDouble( reader.getAttributeValue( null, "maxy" ) );
                    LOG.debug( "Detected srs '{}' with bbox {}, {}, {}, {}", new Object[] { coordinateSystem, minx,
                                                                                           miny, maxx, maxy } );
                    bboxRead = true;
                    continue;
                }

                if ( reader.isStartElement() && reader.getLocalName().equals( "Layer" ) ) {
                    Layer l = new Layer();
                    while ( !( reader.isStartElement() && ( reader.getLocalName().equals( "Name" ) || reader.getLocalName().equals( "Title" ) ) ) ) {
                        reader.next();
                        if ( reader.isEndElement() && reader.getLocalName().equals( "Layer" ) ) {
                            break;
                        }
                    }
                    if ( reader.isStartElement() && reader.getLocalName().equals( "Name" ) ) {
                        l.name = reader.getElementText();
                    }
                    while ( !( reader.isStartElement() && reader.getLocalName().equals( "Title" ) ) ) {
                        reader.next();
                        if ( reader.isEndElement() && reader.getLocalName().equals( "Layer" ) ) {
                            break;
                        }
                    }
                    if ( reader.isStartElement() && reader.getLocalName().equals( "Title" ) ) {
                        l.title = reader.getElementText();
                    }
                    if ( l.name != null ) {
                        l.jsName = l.name.replace( ":", "" );
                        layers.add( l );
                        if ( l.name.equals( "statistics" ) ) {
                            statisticsAvailable = true;
                            while ( !( reader.isStartElement() && reader.getLocalName().equals( "Extent" ) && reader.getAttributeValue( null,
                                                                                                                                        "name" ).equals( "time" ) ) ) {
                                reader.next();
                            }
                            String def = reader.getAttributeValue( null, "default" );
                            String ext = reader.getElementText();
                            try {
                                extent = parseISO8601Date( ext.split( "/" )[0] );
                                from = parseISO8601Date( def.split( "/" )[0] );
                                until = new Date( currentTimeMillis() );
                                statistics = l;
                            } catch ( ParseException e ) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            LOG.debug( "Loaded capabilities of server at '{}'.", url );
            return;
        } catch ( MalformedURLException e ) {
            LOG.debug( "Getting the layers/capabilities was a problem: '{}'", e.getLocalizedMessage() );
            LOG.trace( "Stack trace:", e );
        } catch ( IOException e ) {
            LOG.debug( "Getting the layers/capabilities was a problem: '{}'", e.getLocalizedMessage() );
            LOG.trace( "Stack trace:", e );
        } catch ( XMLStreamException e ) {
            LOG.debug( "Getting the layers/capabilities was a problem: '{}'", e.getLocalizedMessage() );
            LOG.trace( "Stack trace:", e );
        } catch ( FactoryConfigurationError e ) {
            LOG.debug( "Getting the layers/capabilities was a problem: '{}'", e.getLocalizedMessage() );
            LOG.trace( "Stack trace:", e );
        } finally {
            if ( in != null ) {
                try {
                    in.close();
                } catch ( IOException e ) {
                    LOG.debug( "Closing the capabilities connection was a problem: '{}'", e.getLocalizedMessage() );
                    LOG.trace( "Stack trace:", e );
                }
            }
        }
        LOG.debug( "Failed to load capabilities from '{}', trying demo.deegree.org.", url );
        if ( !url.equals( "http://demo.deegree.org/deegree-wms/services" ) ) {
            url = "http://demo.deegree.org/deegree-wms/services";
            loadCapabilities();
        }
    }

    /**
     * @return the layers of the WMS service
     */
    public LinkedList<Layer> getLayers() {
        layers.clear();
        loadCapabilities();
        return layers;
    }

    /**
     * @param evt
     */
    public void timeChanged( ValueChangeEvent evt ) {
        // from = new Date( max( from.getTime(), extent.getTime() ) );
        fromString = formatISO8601Date( from );
        untilString = formatISO8601Date( until );
        System.out.println( fromString + "''''''''''''''''''''''''''''''''''''''''''''''" );
        // statistics.extraParams = ", time: '" + formatISO8601Date( from ) + "/" + formatISO8601Date( until ) + "'";
        // Map map = ( (Map) mapPanel.getChildren().get( 0 ) );
        // map.setValid(false);
    }

    public String getFromString() {
        return fromString = formatISO8601Date( from );
    }

    public String getUntilString() {
        return untilString = formatISO8601Date( until );
    }

    /**
     * <code>Layer</code>
     * 
     * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
     * @author last edited by: $Author: mschneider $
     * 
     * @version $Revision: 29926 $, $Date: 2011-03-08 11:47:59 +0100 (Di, 08. Mär 2011) $
     */
    public static class Layer implements Serializable {
        private static final long serialVersionUID = -1563797064644908196L;

        @Getter
        String name;

        @Getter
        String title;

        @Getter
        String jsName;

        @Getter
        String extraParams = "";
    }

}