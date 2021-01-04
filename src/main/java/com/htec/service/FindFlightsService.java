package com.htec.service;

import com.htec.dao.IAirportDao;
import com.htec.dao.IRouteDao;
import com.htec.model.Airport;
import com.htec.model.FlightResult;
import com.htec.model.RootAttributes;
import com.htec.model.Route;
import com.htec.util.GraphOfRoutes;

import javax.inject.Inject;
import java.util.*;

public class FindFlightsService implements IFindFlightsService {

    @Inject
    IAirportDao airportDao;
    @Inject
    IRouteDao routeDao;

    /**
     * Performs search for all available flight routes between corresponding source and destination airports. Depth or better say number
     * of allowed transits between source airport until arrival to destination airport is controlled by <code>NUMBER_OF_TRANSITES</code>
     * parameter inside properties file. Initially all airports are added to graph representation of flight routes. After this
     * in second phase all connection between airports is added with data about individual length of route and its cost.
     * In the end results are sorted by implemented <code>SortByCost</code> comparator.
     * @param source source airport where travel begins
     * @param destination destination airport where travel ends
     * @return ordered list of available flights, were order is ascending by cost of flight
     */
    @Override
    public List<FlightResult> findBySourceAndDestination(String source, String destination) {
        GraphOfRoutes<String> graphOfRoutes = new GraphOfRoutes<String>();

        HashMap<String, Airport> airportMap = airportDao.findAll();

        List<Route> routeList = routeDao.findAll(airportMap);
        for (Route route : routeList) {
            graphOfRoutes.addNode(route.getSourceAirport());
            graphOfRoutes.addNode(route.getDestinationAirport());
        }

        for (Route route : routeList) {
            graphOfRoutes.addEdge(route.getSourceAirport(), route.getDestinationAirport(),
                    RootAttributes.builder().cost(route.getPrice()).length(route.getLength()).build());
        }

        FindRoutes<String> findRoutes = new FindRoutes<String>(graphOfRoutes);

        Map<List<String>, RootAttributes> pathWithCost = findRoutes.getAllRoutsWithCost(source, destination);
        List<FlightResult> flightResults = new ArrayList<>();
        for (Map.Entry<List<String>, RootAttributes> s : pathWithCost.entrySet()) {
            flightResults.add(FlightResult.builder().travelRoute(s.getKey().toString())
                    .cost(s.getValue().getCost())
                    .length(s.getValue().getLength())
                    .build());
        }
        Collections.sort(flightResults, new FlightResult.SortingByCost());

        return flightResults;
    }
}
