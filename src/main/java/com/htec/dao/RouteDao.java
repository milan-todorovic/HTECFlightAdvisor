package com.htec.dao;

import com.htec.model.Airport;
import com.htec.model.Route;
import com.htec.util.DistanceCalculator;
import com.htec.util.FlightFileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to provide list of flight routes from included file, but filtered if required airport exists.
 */
public class RouteDao implements IRouteDao{

    private File routeFile;
    private static List<Route> routeList = new ArrayList<>();
    Logger lgr = Logger.getLogger(AirportDao.class.getName());

    /**
     * Finds all routes provided in txt file.
     * @param airportsMap map that is base for validity check of routes
     * @return
     */
    @Override
    public List<Route> findAll(HashMap<String, Airport> airportsMap) {
        routeFile = new File(RouteDao.class.getClassLoader().getResource("routes.txt").getPath());

        try {
            FlightFileReader.consumeNonEmptyLinesFromFile(routeFile, line -> {
                String[] params = line.split(",");
                if(airportsMap.get(params[2])!=null && airportsMap.get(params[4])!=null)
                    routeList.add(Route.builder()
                            .airline(params[0])
                            .airlineId(params[1])
                            .sourceAirport(params[2])
                            .sourceAirportId(params[3])
                            .destinationAirport(params[4])
                            .destinationAirportId(params[5])
                            .codeshare(params[6])
                            .stopsNumber(Integer.parseInt(params[7]))
                            .equipment(params[8])
                            .price(Double.parseDouble(params[9]))
                            .length(DistanceCalculator.distance(airportsMap.get(params[2]).getLatitude(),airportsMap.get(params[2]).getLongitude(),
                                    airportsMap.get(params[4]).getLatitude(),airportsMap.get(params[4]).getLongitude()))
                            .build());
            });
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return routeList;
    }
}
