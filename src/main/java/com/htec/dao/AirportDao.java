package com.htec.dao;

import com.htec.model.Airport;
import com.htec.model.City;
import com.htec.util.FlightFileReader;

import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to provide list of active airports from included file, but filtered based on provided cities in database
 */
public class AirportDao implements IAirportDao{

    @Inject
    ICityDao cityDao;

    private File airportFile;
    private static HashMap<String, Airport> airportsMap = new HashMap<>();
    Logger lgr = Logger.getLogger(AirportDao.class.getName());

    /**
     * Finds all airports in provided txt file, with corresponding city with comments in database. Airport that does not have
     * active city in database is not part of resulting <code>HashMap</code>.
     * @return <code></code>
     */
    @Override
    public HashMap<String, Airport> findAll() {

        List<City> cities = cityDao.findAll();

        airportFile = new File(AirportDao.class.getClassLoader().getResource("airports.txt").getPath());
        try {
            FlightFileReader.consumeNonEmptyLinesFromFile(airportFile, line -> {
                String[] params = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String airportID = params[0].replace("\"", "");
                String airportIATA = params[4].replace("\"", "");
                String airportICAO = params[5].replace("\"", "");
                String airportMapKey = (airportIATA.isEmpty() && airportICAO.isEmpty()) ? airportID : (airportIATA.equals("\\N") ? airportICAO : airportIATA);
                try {
                    if (isCityAvailable(cities, params[2].replace("\"", ""))) {
                        airportsMap.put(airportMapKey, Airport.builder()
                                .airportId(Integer.parseInt(params[0]))
                                .name(params[1].replace("\"", ""))
                                .city(params[2].replace("\"", ""))
                                .country(params[3].replace("\"", ""))
                                .iata(params[4].replace("\"", ""))
                                .icao(params[5].replace("\"", ""))
                                .latitude(Double.parseDouble(params[6]))
                                .longitude(Double.parseDouble(params[7]))
                                .altitude(Double.parseDouble(params[8]))
                                .timezone(params[9])
                                .dst(params[10].replace("\"", ""))
                                .tz(params[11].replace("\"", ""))
                                .type(params[12].replace("\"", ""))
                                .source(params[13].replace("\"", ""))
                                .build());
                    }
                } catch (Exception ex) {
                    lgr.log(Level.SEVERE, ex.getMessage(), ex);
                }
            });
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return airportsMap;
    }

    /**
     * Provide airport validation. On case airport city is also part of cities inside database, current airport will
     * be forwarded to resulting <code>HashMap</code>
     * @param cities list of active cities inside database
     * @param param name of city to which currently processed airport belongs
     * @return
     */
    private boolean isCityAvailable(List<City> cities, String param) {
        boolean flag = false;
        for (City city : cities){
            if(city.getName().equals(param.trim())){
                flag=true;
                break;
            }
        }
        return flag;
    }
}
