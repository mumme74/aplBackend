/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import nu.t4.services.global.APLService;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Daniel Nilsson
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(nu.t4.filters.CORSFilter.class);
        resources.add(nu.t4.services.elev.ElevAktivitetService.class);
        resources.add(nu.t4.services.elev.ElevLoggService.class);
        resources.add(nu.t4.services.elev.ElevMomentService.class);
        resources.add(nu.t4.services.elev.ElevNarvaroService.class);
        resources.add(nu.t4.services.global.APLService.class);
        resources.add(nu.t4.services.global.InfoService.class);
        resources.add(nu.t4.services.global.KommentarService.class);
        resources.add(nu.t4.services.handledare.HandledareAktivitetService.class);
        resources.add(nu.t4.services.handledare.HandledareMomentService.class);
        resources.add(nu.t4.services.handledare.HandledareService.class);
        resources.add(nu.t4.services.larare.LarareEleverService.class);
        resources.add(nu.t4.services.larare.LarareHandledareService.class);
        resources.add(nu.t4.services.larare.LarareKlassService.class);
        resources.add(nu.t4.services.larare.LarareMomentService.class);
        resources.add(nu.t4.services.larare.LarareNarvaroService.class);
        resources.add(nu.t4.services.larare.LarareOmdomeService.class);
        resources.add(nu.t4.services.larare.LarareRedigeraAnvService.class);
    }
    
}
